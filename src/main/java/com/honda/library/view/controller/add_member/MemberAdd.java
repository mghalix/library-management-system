package com.honda.library.view.controller.add_member;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.AlertMaker;
import com.honda.library.model.Book;
import com.honda.library.model.Member;
import com.honda.library.model.Modes;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MemberAdd implements Initializable {
  DatabaseHandler handler;
  Modes mode;
  @FXML
  private MFXButton btnCancel;

  @FXML
  private MFXButton btnSave;

  @FXML
  private MFXTextField txtEmail;

  @FXML
  private MFXTextField txtId;

  @FXML
  private MFXTextField txtMobile;

  @FXML
  private MFXTextField txtName;
  @FXML
  private AnchorPane paneRoot;

  @FXML
  void btnCancel_Click(ActionEvent event) {
    Stage stage = (Stage) paneRoot.getScene().getWindow();
    stage.close();
//    if(mode == Modes.EDIT) refresh
  }

  @FXML
  void btnSave_Click(ActionEvent event) {
    String id = txtId.getText();
    String name = txtName.getText();
    String mobile = txtMobile.getText();
    String email = txtEmail.getText();
//    if(handler.idExists(id, "members")) {
//      Alert alert = new Alert(Alert.AlertType.ERROR);
//      alert.setHeaderText(null);
//      alert.setContentText("Member with id " + id + " and name " + name +"
//      already exists");
//      alert.showAndWait();
//      return;
//    }
    if (id.isEmpty() || name.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
      AlertMaker.showErrorMessage(null, "Please enter in all the fields");
      return;
    }
    if (mode == Modes.EDIT) {
      handleUpdateMember();
      return;
    }
    String st = "INSERT INTO members VALUES("
            + "'" + id + "',"
            + "'" + name + "',"
            + "'" + mobile + "',"
            + "'" + email + "'"
            + ")";
    System.out.println(st);
    if (!handler.execAction(st)) {
      AlertMaker.showErrorMessage(null, "Error Occurred");
      return;
    }
    AlertMaker.showSimpleAlert(null, "Saved");
  }

  private void handleUpdateMember() {
    Member member = new Member(txtName.getText(), txtId.getText(),
            txtMobile.getText(), txtEmail.getText());
    if (!DatabaseHandler.updateMember(member)) {
      AlertMaker.showErrorMessage("Failed",
              "Cannot update member " + member.getName());
      return;
    }
    AlertMaker.showSimpleAlert("Success", "Member " + member.getName() + " " +
            "has been updated");
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    txtEmail.setFloatMode(FloatMode.INLINE);
    try {
      handler = DatabaseHandler.getInstance();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void inflateUI(Member member) {
    txtName.setText(member.getName());
    txtId.setText(member.getId());
    txtId.setEditable(false);
    txtMobile.setText(member.getMobile());
    txtEmail.setText(member.getEmail());
    mode = Modes.EDIT;
  }
}

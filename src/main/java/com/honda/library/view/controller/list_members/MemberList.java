package com.honda.library.view.controller.list_members;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.AlertMaker;
import com.honda.library.model.Book;
import com.honda.library.view.controller.add_book.BookAdd;
import com.honda.library.view.controller.add_member.MemberAdd;
import com.honda.library.view.controller.ui.assistant.AssistantUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.honda.library.model.Member;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberList implements Initializable {
  ObservableList<Member> list = FXCollections.observableArrayList();

  @FXML
  private TableColumn<Member, String> colEmail;

  @FXML
  private TableColumn<Member, String> colID;

  @FXML
  private TableColumn<Member, String> colMobile;

  @FXML
  private TableColumn<Member, String> colName;

  @FXML
  private TableView<Member> tblMemberList;

  private void initCol() {
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initCol();
    try {
      loadData();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadData() throws SQLException {
    list.clear();
    DatabaseHandler handler = DatabaseHandler.getInstance();
    String query = "SELECT * FROM members";
    try (ResultSet res = handler.execQuery(query)) {
      while (res.next()) {
        String id, name, mobile, email;
        id = res.getString("id");
        name = res.getString("name");
        mobile = res.getString("mobile");
        email = res.getString("email");

        list.add(new Member(name, id, mobile, email));
      }
    }
    tblMemberList.getItems().setAll(list);
  }

  public void mnuRefresh(ActionEvent event) throws SQLException {
    loadData();
  }

  public void mnuEdit(ActionEvent event) throws IOException {
    // fetching the selected row
    Member selectedForEdit =
            tblMemberList.getSelectionModel().getSelectedItem();
    if (selectedForEdit == null) {
      AlertMaker.showWarningMessage("No Member Selected", "Please select a " +
              "member or update");
      return;
    }
    FXMLLoader loader =
            new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/com/honda/library/view/member-add/member-add.fxml")));
    Parent root = loader.load();
    MemberAdd controller = loader.getController();
    controller.inflateUI(selectedForEdit);

    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setScene(new Scene(root));
    stage.setTitle("Edit Member");
    stage.show();
    AssistantUtil.setStageIcon(stage);
    stage.setOnCloseRequest((e) -> {
      try {
        mnuRefresh(new ActionEvent());
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

  public void mnuDelete(ActionEvent event) throws SQLException {
    // fetching the selected row
    Member selectedForDeletion =
            tblMemberList.getSelectionModel().getSelectedItem();
    if (selectedForDeletion == null) {
      AlertMaker.showWarningMessage("No member Selected", "Please select a " +
              "member for deletion");
      return;
    }

    if (DatabaseHandler.getInstance().memberHasAnyBook(selectedForDeletion)) {
      AlertMaker.showWarningMessage("Book Deletion Error", "This book is " +
              "already issued and cannot be deleted");
      return;
    }

    Optional<ButtonType> response = AlertMaker.showConfirmationMessage(
            "Deleting Member",
            "Are you sure you want to delete member " + selectedForDeletion.getName() + "?");
    if (response.orElse(null) != ButtonType.OK) {
      return;
    }

    boolean res =
            DatabaseHandler.getInstance().deleteMember(selectedForDeletion);
    if (!res) {
      AlertMaker.showErrorMessage("Failed", selectedForDeletion.getName() +
              " could not be deleted");
      return;
    }

    AlertMaker.showSimpleAlert("Member deleted",
            selectedForDeletion.getName() + " was deleted successfully");
    list.remove(selectedForDeletion);
  }
}
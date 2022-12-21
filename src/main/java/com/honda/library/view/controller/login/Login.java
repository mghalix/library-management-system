package com.honda.library.view.controller.login;

import com.honda.library.model.AlertMaker;
import com.honda.library.model.Preferences;
import com.honda.library.view.controller.ui.assistant.AssistantUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {

  @FXML
  private MFXButton btnSignIn;

  @FXML
  private MFXButton btnSignUp;

  @FXML
  private MFXTextField txtPass;
  @FXML
  private MFXTextField txtUser;
  private Preferences preferences;

  @FXML
  void btnSignIn_Click(ActionEvent event) throws IOException {
    String username = txtUser.getText();
    String password = DigestUtils.sha1Hex(txtPass.getText());
    if (!username.equals(preferences.getUsername()) || !password.equals(preferences.getPassword())) {
      AlertMaker.showErrorMessage("Invalid Credentials", "Wrong username or password, please try again");
      return;
    }
    ((Stage) txtUser.getScene().getWindow()).close();
    loadWindow("/com/honda/library/view/home/home.fxml", "Home");
  }

  @FXML
  void btnSignUp_Click(ActionEvent event) {

  }

  void loadWindow(String location, String title) throws IOException {
    Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(location)));
    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setTitle(title);
    stage.setScene(new Scene(parent));
    stage.show();
    AssistantUtil.setStageIcon(stage);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    preferences = Preferences.getPreferences();
  }


}

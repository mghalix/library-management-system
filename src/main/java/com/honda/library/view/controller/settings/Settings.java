package com.honda.library.view.controller.settings;

import com.honda.library.model.Preferences;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.*;
import javafx.event.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

  @FXML
  private MFXButton btnCancel;

  @FXML
  private MFXButton btnSave;

  @FXML
  private MFXTextField txtDaysWithoutFine;

  @FXML
  private MFXTextField txtFinePerDay;

  @FXML
  private MFXPasswordField txtPass;

  @FXML
  private MFXTextField txtUser;
  @FXML
  void btnCancel_Click(ActionEvent event) {
    ((Stage) txtDaysWithoutFine.getScene().getWindow()).close();
  }
  @FXML
  public void btnSave_Click(ActionEvent event) {
    Preferences preferences = Preferences.getPreferences();
    preferences.setPreferences(
            Integer.parseInt(txtDaysWithoutFine.getText()),
            Float.parseFloat(txtFinePerDay.getText()),
            txtUser.getText(),
            txtPass.getText()
    );
    Preferences.writePreferencesToFile(preferences);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initDefaultValues();
  }

  private void initDefaultValues() {
    Preferences preferences = Preferences.getPreferences();
    txtDaysWithoutFine.setText(String.valueOf(preferences.getDaysWithoutFine()));
    txtFinePerDay.setText(String.valueOf(preferences.getFinePerDay()));
    txtUser.setText(preferences.getUsername());
    txtPass.setText(preferences.getPassword());
  }
}
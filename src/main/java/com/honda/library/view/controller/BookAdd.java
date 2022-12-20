package com.honda.library.view.controller;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.AlertMaker;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookAdd {
  @FXML
  private void btnCancel_Click(ActionEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void btnSave_Click(ActionEvent event) throws SQLException {
    String id, title, author, publisher;
    id = txtId.getText();
    title = txtTitle.getText();
    author = txtAuthor.getText();
    publisher = txtPublisher.getText();
    if (databaseHandler.idExists(id, "books")) {
      AlertMaker.showErrorMessage("Book adding failure", "Book with id " + id + " and name " + title + " already exists");
      return;
    }
    if (id.isEmpty() || title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
      AlertMaker.showErrorMessage(null, "Please enter in all the fields");
      return;
    }
    String query = "INSERT INTO books VALUES("
            + "'" + id + "',"
            + "'" + title + "',"
            + "'" + author + "',"
            + "'" + publisher + "',"
            + "" + "true" + ""
            + ")";
    if (!databaseHandler.execAction(query)) {
      AlertMaker.showErrorMessage(null, "Failed");
      return;
    }
    AlertMaker.showSimpleAlert(null, "Book " + title + " has been added successfully!");
  }

  @FXML
  private MFXButton btnCancel;

  @FXML
  private MFXButton btnSave;

  @FXML
  private MFXTextField txtAuthor;

  @FXML
  private MFXTextField txtId;

  @FXML
  private MFXTextField txtPublisher;

  @FXML
  private MFXTextField txtTitle;

  @FXML
  private AnchorPane rootPane;

  private final DatabaseHandler databaseHandler;

  public BookAdd() throws SQLException {
    databaseHandler = DatabaseHandler.getInstance();
  }

  private void checkData() throws SQLException {
    String query = "SELECT title FROM books";
    ResultSet res = databaseHandler.execQuery(query);
    while (res.next()) {
      String title = res.getString("title");
      System.out.println(title);
    }
  }
}
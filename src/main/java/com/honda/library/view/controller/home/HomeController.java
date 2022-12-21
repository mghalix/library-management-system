package com.honda.library.view.controller.home;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.AlertMaker;
import com.honda.library.model.Validation;
import com.jfoenix.effects.JFXDepthManager;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
  @FXML
  private HBox hbxBookInfo;

  @FXML
  private HBox hbxMemberInfo;

  @FXML
  private Text txtBookAuthor;

  @FXML
  private MFXTextField txtBookID;

  @FXML
  private Text txtBookName;

  @FXML
  private Text txtBookStatus;

  @FXML
  private Text txtMemberContact;

  @FXML
  private MFXTextField txtMemberID;

  @FXML
  private Text txtMemberName;

  @FXML
  MFXTextField txtBookID2;

  @FXML
  private StackPane pnRoot;

  @FXML
  private ListView<String> lsvIssueData;
  DatabaseHandler databaseHandler;
  boolean readyForSubmission = false;

  @FXML
  void loadAddBook(ActionEvent event) throws IOException {
    loadWindow("/com/honda/library/view/book-add/book-add.fxml", "Add Book");
  }

  @FXML
  void loadAddMember(ActionEvent event) throws IOException {
    loadWindow("/com/honda/library/view/member-add/member-add.fxml", "Add New Member");
  }

  @FXML
  void loadBookList(ActionEvent event) throws IOException {
    loadWindow("/com/honda/library/view/book-list/book-list.fxml", "Book List");
  }

  @FXML
  void loadMemberList(ActionEvent event) throws IOException {
    loadWindow("/com/honda/library/view/member-list/member-list.fxml", "Member List");
  }

  @FXML
  void loadSettings(ActionEvent event) throws IOException {
    loadWindow("/com/honda/library/view/settings/settings.fxml", "Settings");
  }

  void loadWindow(String location, String title) throws IOException {
    Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(location)));
    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setTitle(title);
    stage.setScene(new Scene(parent));
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    JFXDepthManager.setDepth(hbxBookInfo, 1);
    JFXDepthManager.setDepth(hbxMemberInfo, 1);
    try {
      databaseHandler = DatabaseHandler.getInstance();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  void clearBookCache() {
    txtBookName.setText("");
    txtBookAuthor.setText("");
    txtBookStatus.setText("");
  }

  void clearMemberCache() {
    txtMemberName.setText("");
    txtMemberContact.setText("");
  }

  @FXML
  void loadBookInfo(ActionEvent event) throws SQLException {
    clearBookCache();
    String id = txtBookID.getText();
    if (Validation.validateID(id)) {
      AlertMaker.showWarningMessage(null, "Use of forbidden character");
      return;
    }
    String query = "SELECT * FROM books WHERE id='" + id + "'";
    ResultSet rs = databaseHandler.execQuery(query);
    boolean available = false;
    while (rs.next()) {
      txtBookName.setText(rs.getString("title"));
      txtBookAuthor.setText(rs.getString("author"));
      txtBookStatus.setText(rs.getBoolean("availability") ? "Available" : "Unavailable");
      available = true;
    }
    if (!available) {
      txtBookName.setText("No such book available");
      return;
    }
    txtMemberID.requestFocus();
  }

  @FXML
  void loadMemberInfo(ActionEvent event) throws SQLException {
    clearMemberCache();
    String id = txtMemberID.getText();
    if (Validation.validateID(id)) {
      AlertMaker.showWarningMessage(null, "Use of forbidden character");
      return;
    }
    String query = "SELECT * FROM members WHERE id='" + id + "'";
    ResultSet rs = databaseHandler.execQuery(query);
    boolean found = false;
    while (rs.next()) {
      txtMemberName.setText(rs.getString("name"));
      txtMemberContact.setText(rs.getString("email"));
      found = true;
    }
    if (!found) {
      txtMemberName.setText("No such member");
      txtMemberContact.setText(null);
    }
  }

  @FXML
  private void loadIssueOperation(ActionEvent event) {
    String bookID, memberID;
    bookID = txtBookID.getText();
    memberID = txtMemberID.getText();
    if (bookID.isEmpty() || memberID.isEmpty()) {
      AlertMaker.showWarningMessage(null, "Please fill in all the fields before issuing the book");
      return;
    }
    if (txtMemberName.getText().equals("Member Name") || txtMemberContact.getText().isEmpty()) {
      AlertMaker.showWarningMessage(null, "You cannot issue a book to a non-existing member");
      return;
    }
    if (txtBookName.getText().equals("Book Name") || txtBookAuthor.getText().isEmpty()) {
      AlertMaker.showWarningMessage(null, "Book doesn't exist");
      return;
    }
    if (txtBookStatus.getText().equals("Unavailable")) {
      AlertMaker.showWarningMessage(null, "Sorry the book you are trying to issue is currently unavailable");
      return;
    }
    if (Validation.validateName(bookID) || Validation.validateName(memberID)) {
      AlertMaker.showWarningMessage(null, "Use of forbidden character");
      return;
    }
    Optional<ButtonType> response = AlertMaker.showConfirmationMessage("Confirm Issue Operation",
            "Are you sure you want to issue book " +
                    txtBookName.getText() + " to "
                    + txtMemberName.getText() + "?"
    );
    if (response.orElse(null) != ButtonType.OK) return;

    if (!issueBook(bookID, memberID)) {
      AlertMaker.showErrorMessage("Failed", "Issue Operation Failed");
      return;
    }
    AlertMaker.showSimpleAlert("Success", "Book Issue Complete");
  }

  boolean issueBook(String bookID, String memberID) {
    String axc1 = "INSERT INTO issues(book_id, member_id) VALUES('" + bookID + "', '" + memberID + "'" + ")";
    String axc2 = "UPDATE books SET availability=false WHERE id='" + bookID + "'";
    System.out.println(axc1 + " and " + axc2);

    return (databaseHandler.execAction(axc1) && databaseHandler.execAction(axc2));
  }

  boolean returnBook(String bookID) {
    String axn1 = "UPDATE books SET availability = TRUE WHERE id='" + bookID + "'";
    String axn2 = "DELETE FROM issues WHERE book_id='" + bookID + "'";
    return (databaseHandler.execAction(axn1) && databaseHandler.execAction(axn2));
  }

  @FXML
  private void loadBookInfo2(ActionEvent event) throws SQLException {
    ObservableList<String> issueData = FXCollections.observableArrayList();
    readyForSubmission = false;
    String id = txtBookID2.getText();

    String query = "SELECT * FROM issues WHERE book_id='" + id + "'";
    ResultSet rs = databaseHandler.execQuery(query);
    if (rs.next()) {
      String bookID, memberID;
      Timestamp issueTime;
      int renewCount;

      bookID = id;
      memberID = rs.getString("member_id");
      issueTime = rs.getTimestamp("issue_time");
      renewCount = rs.getInt("renew_count");

      issueData.add("Issue Date and Time: " + issueTime.toString());
      issueData.add("Renew Count: " + renewCount);
      issueData.add("Book Information:-");

      // fetching book data from books table
      query = "SELECT * FROM books WHERE id = '" + bookID + "'";
      ResultSet rs2 = databaseHandler.execQuery(query);

      while (rs2.next()) {
        issueData.add("Book Name: " + rs2.getString("title"));
        issueData.add("Book ID: " + rs2.getString("id"));
        issueData.add("Book Author: " + rs2.getString("author"));
        issueData.add("Book Publisher: " + rs2.getString("publisher"));
      }

      // fetching member data from members table
      issueData.add("Member Information:-");
      query = "SELECT * FROM members WHERE id = '" + memberID + "'";
      rs2 = databaseHandler.execQuery(query);

      while (rs2.next()) {
        issueData.add("Member Name: " + rs2.getString("name"));
        issueData.add("Member ID: " + rs2.getString("id"));
        issueData.add("Member Mobile: " + rs2.getString("mobile"));
        issueData.add("Member Email: " + rs2.getString("email"));
      }
      readyForSubmission = true;
    }
    lsvIssueData.getItems().setAll(issueData);
  }

  @FXML
  private void loadSubmissionOperation(ActionEvent event) {
    if (!readyForSubmission || txtBookID2.getText().isEmpty()) {
      AlertMaker.showErrorMessage("Renewal Failed", "Please select a book to renew");
      return;
    }

    String id = txtBookID2.getText();
    Alert alert;
    alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Submission Operation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to submit the book?");
    Optional<ButtonType> response = alert.showAndWait();

    if (response.orElse(null) != ButtonType.OK) return;
    if (!returnBook(id)) {
      AlertMaker.showErrorMessage("Failure", "Submission has failed");
      return;
    }
    AlertMaker.showSimpleAlert("Success", "The book has been submitted successfully");
    txtBookID2.clear();
    lsvIssueData.setItems(null);
    readyForSubmission = false;
  }

  boolean renewBook(String bookID) {
    String axn = "UPDATE issues SET issue_time=CURRENT_TIMESTAMP, renew_count=(renew_count+1) WHERE book_id ='" + bookID + "'";
    return (databaseHandler.execAction(axn));
  }

  @FXML
  private void loadRenewOperation(ActionEvent event) throws SQLException {
    if (!readyForSubmission || txtBookID2.getText().isEmpty()) {
      AlertMaker.showErrorMessage("Submission Failed", "Please select a book to submit");
      return;
    }
    String id = txtBookID2.getText();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Renew Operation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to renew the book?");
    Optional<ButtonType> response = alert.showAndWait();
    if (response.orElse(null) != ButtonType.OK) return;
    if (!renewBook(id)) {
      AlertMaker.showErrorMessage("Renewal Failure", "Renewing the book has failed");
      return;
    }
    AlertMaker.showSimpleAlert("Renewal Successful", "The book has been renewed successfully");
    loadBookInfo2(event); // to refresh the list view
  }


  @FXML
  void mnuClose(ActionEvent event) {
    ((Stage) pnRoot.getScene().getWindow()).close();
  }

  @FXML
  void mnuAddMember(ActionEvent event) throws IOException {
    loadAddMember(event);
  }

  @FXML
  void mnuAddBook(ActionEvent event) throws IOException {
    loadAddBook(event);
  }

  @FXML
  void mnuViewBook(ActionEvent event) throws IOException {
    loadBookList(event);
  }

  @FXML
  void mnuViewMember(ActionEvent event) throws IOException {
    loadMemberList(event);
  }

  @FXML
  void mnuFullscreen(ActionEvent event) {
    Stage stage = ((Stage) pnRoot.getScene().getWindow());
    stage.setFullScreen(!stage.isFullScreen());
  }
}

package com.honda.library.view.controller.list_books;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.AlertMaker;
import com.honda.library.model.Book;
import com.honda.library.view.controller.add_book.BookAdd;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookList implements Initializable {
  ObservableList<Book> list = FXCollections.observableArrayList();

  @FXML
  private TableColumn<Book, String> colAuthor;

  @FXML
  private TableColumn<Book, Boolean> colAvailability;

  @FXML
  private TableColumn<Book, String> colID;

  @FXML
  private TableColumn<Book, String> colPublisher;

  @FXML
  private TableColumn<Book, String> colTitle;

  @FXML
  private AnchorPane paneRoot;

  @FXML
  private TableView<Book> tblBookList;

  @Override // initialize method is called after all @FXML annotated members have been injected, unlike constructor.
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initCol();
    try {
      loadData();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadData() throws SQLException {
    DatabaseHandler handler = DatabaseHandler.getInstance();
    String query = "SELECT * FROM books";
    ResultSet rs = handler.execQuery(query);
    while (rs.next()) {
      String id = rs.getString("id");
      String title = rs.getString("title");
      String author = rs.getString("author");
      String publisher = rs.getString("publisher");
      boolean availability = rs.getBoolean("availability");

      list.add(new Book(title, id, author, publisher, availability));
    }
    tblBookList.setItems(list);
  }

  private void initCol() {
    colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
  }


  @FXML
  void mnuDelete(ActionEvent event) throws SQLException {
    // fetching the selected row
    Book selectedForDeletion = tblBookList.getSelectionModel().getSelectedItem();
    if (selectedForDeletion == null) {
      AlertMaker.showWarningMessage("No Book Selected", "Please select a book for deletion");
      return;
    }

    if (DatabaseHandler.getInstance().isBookIssued(selectedForDeletion)) {
      AlertMaker.showWarningMessage("Book Deletion Error", "This book is already issued and cannot be deleted");
      return;
    }

    Optional<ButtonType> response = AlertMaker.showConfirmationMessage("Deleting Book", "Are you sure you want to delete the book " + selectedForDeletion.getTitle() + "?");
    if (response.orElse(null) != ButtonType.OK) {
      return;
    }

    boolean res = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
    if (!res) {
      AlertMaker.showErrorMessage("Failed", selectedForDeletion.getTitle() + " could not be deleted");
      return;
    }

    AlertMaker.showSimpleAlert("Book deleted", selectedForDeletion.getTitle() + " was deleted successfully");
    list.remove(selectedForDeletion);
  }

  @FXML
  private void mnuEdit(ActionEvent event) throws IOException {
    // TODO fix this method
    // fetching the selected row
    Book selectedForEdit = tblBookList.getSelectionModel().getSelectedItem();
    if (selectedForEdit == null) {
      AlertMaker.showWarningMessage("No Book Selected", "Please select a book for deletion");
      return;
    }
    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/honda/library/view/book-add/book-add.fxml")));
    Parent root = loader.load();
    BookAdd controller = loader.getController();
    controller.inflateUI(selectedForEdit);

    Stage stage = new Stage(StageStyle.DECORATED);
    stage.setScene(new Scene(root));
    stage.setTitle("Edit Book");
    stage.show();
  }
}

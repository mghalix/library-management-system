package com.honda.library.view.controller;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    ResultSet res = handler.execQuery(query);
    while (res.next()) {
      String id = res.getString("id");
      String title = res.getString("title");
      String author = res.getString("author");
      String publisher = res.getString("publisher");
      boolean availability = res.getBoolean("availability");

      list.add(new Book(id, title, author, publisher, availability));
    }
    tblBookList.getItems().setAll(list);
  }

  private void initCol() {
    colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
    colID.setCellValueFactory(new PropertyValueFactory<>("id"));
    colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
  }
}

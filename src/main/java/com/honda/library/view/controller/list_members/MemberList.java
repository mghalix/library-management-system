package com.honda.library.view.controller.list_members;

import com.honda.library.control.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.honda.library.model.Member;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
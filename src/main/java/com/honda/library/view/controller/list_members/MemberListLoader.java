package com.honda.library.view.controller.list_members;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.view.controller.list_books.BookListLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class MemberListLoader extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(Objects.requireNonNull(BookListLoader.class.getResource("/com/honda/library/view/member-list/member-list.fxml")));

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Member List");
    primaryStage.show();

    new Thread(() -> {
      try {
        DatabaseHandler.getInstance();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }
}

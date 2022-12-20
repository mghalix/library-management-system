package com.honda.library.loaders;

import com.honda.library.control.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class BookAddLoader extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(BookAddLoader.class.getResource("/com/honda/library/view/book-add/book-add.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Add Book");
    stage.setScene(scene);
    stage.show();

    new Thread(() -> {
      try {
        DatabaseHandler.getInstance();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  public static void main(String[] args) {
    launch();
  }
}
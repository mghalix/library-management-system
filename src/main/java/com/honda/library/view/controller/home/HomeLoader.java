package com.honda.library.view.controller.home;

import com.honda.library.control.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class HomeLoader extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/honda/library/view/home/home.fxml")));

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Home");
    primaryStage.show();

    // to reduce overhead time when clicking on buttons
    new Thread(() -> {
      try {
        DatabaseHandler.getInstance();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }).start();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

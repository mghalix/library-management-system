package com.honda.library;

import com.honda.library.control.DatabaseHandler;
import com.honda.library.view.controller.ui.assistant.AssistantUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Objects;

public class App extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/login/login.fxml")));

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
//    AssistantUtil.setStageIcon(primaryStage);

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

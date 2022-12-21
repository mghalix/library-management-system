package com.honda.library.view.controller.add_member;

import com.honda.library.control.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MemberAddLoader extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(MemberAddLoader.class.getResource("/com/honda/library/view/member-add/member-add.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Add Member");
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
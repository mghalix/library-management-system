package com.honda.library.view.controller.ui.assistant;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;


public class AssistantUtil {
  private static final String IMAGE_LOC = "/resources/com/honda/library/icons/library.png";
  public static void setStageIcon(Stage stage) {
    stage.getIcons().add(new Image(new File(IMAGE_LOC).toURI().toString()));
  }
}

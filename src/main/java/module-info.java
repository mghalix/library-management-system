module com.honda.library {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires org.kordamp.bootstrapfx.core;
  requires MaterialFX;
  requires java.sql;
  requires com.jfoenix;
  requires com.google.gson;

  exports com.honda.library.loaders;
  opens com.honda.library.loaders to javafx.fxml;
  exports com.honda.library;
  opens com.honda.library to javafx.fxml;
  exports com.honda.library.view.controller;
  opens com.honda.library.view.controller to javafx.fxml;
  opens com.honda.library.model;
}
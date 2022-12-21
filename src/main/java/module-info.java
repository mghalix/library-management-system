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
  requires org.apache.commons.codec;

  exports com.honda.library;
  opens com.honda.library to javafx.fxml;
  opens com.honda.library.model;
  exports com.honda.library.model;
  exports com.honda.library.view.controller.add_book;
  opens com.honda.library.view.controller.add_book to javafx.fxml;
  exports com.honda.library.view.controller.add_member;
  opens com.honda.library.view.controller.add_member to javafx.fxml;
  exports com.honda.library.view.controller.list_books;
  opens com.honda.library.view.controller.list_books to javafx.fxml;
  exports com.honda.library.view.controller.list_members;
  opens com.honda.library.view.controller.list_members to javafx.fxml;
  exports com.honda.library.view.controller.settings;
  opens com.honda.library.view.controller.settings to javafx.fxml;
  exports com.honda.library.view.controller.home;
  opens com.honda.library.view.controller.home to javafx.fxml;
  exports com.honda.library.view.controller.login;
  opens com.honda.library.view.controller.login to javafx.fxml;
  opens com.honda.library.images;
}
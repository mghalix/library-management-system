package com.honda.library.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
  private final SimpleStringProperty title, id, author, publisher;
  private final SimpleBooleanProperty availability;

  public Book(String title, String id, String author, String publisher, boolean availability) {
    this.title = new SimpleStringProperty(title);
    this.id = new SimpleStringProperty(id);
    this.author = new SimpleStringProperty(author);
    this.publisher = new SimpleStringProperty(publisher);
    this.availability = new SimpleBooleanProperty(availability);
  }

  public String getTitle() {
    return title.get();
  }

  public String getId() {
    return id.get();
  }

  public String getAuthor() {
    return author.get();
  }

  public String getPublisher() {
    return publisher.get();
  }

  public boolean getAvailability() {
    return availability.get();
  }
}

package com.honda.library.model;

import javafx.beans.property.SimpleStringProperty;

public class Member {
  private final SimpleStringProperty name, id, mobile, email;

  public Member(String name, String id, String mobile, String email) {
    this.name = new SimpleStringProperty(name);

    this.id = new SimpleStringProperty(id);
    this.mobile = new SimpleStringProperty(mobile);
    this.email = new SimpleStringProperty(email);
  }

  public String getName() {
    return name.get();
  }

  public String getId() {
    return id.get();
  }

  public String getMobile() {
    return mobile.get();
  }

  public String getEmail() {
    return email.get();
  }
}

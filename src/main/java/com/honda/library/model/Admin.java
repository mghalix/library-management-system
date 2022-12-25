package com.honda.library.model;

public class Admin {
  private String name;
  private String username;
  private String password;
  private String email;
  private AppUserRole appUserRole;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) throws Exception {
    Validation.validateUsernameLogin(username);
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public AppUserRole getAppUserRole() {
    return appUserRole;
  }

  public void setAppUserRole(AppUserRole appUserRole) {
    this.appUserRole = appUserRole;
  }
}
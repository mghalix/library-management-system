package com.honda.library.model;


import com.google.gson.Gson;

import java.io.*;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

public class Preferences {
  private int daysWithoutFine;
  private float finePerDay;
  private String username, password;
  private static final String CONFIG_FILE = "config.txt";

  public Preferences() {
    daysWithoutFine = 14;
    finePerDay = 2;
    username = password = "admin";
  }

  public static Preferences getPreferences() {
    Preferences preferences = new Preferences();
    Gson gson = new Gson();

    try {
      preferences = gson.fromJson(new FileReader(CONFIG_FILE), preferences.getClass());
    } catch (FileNotFoundException e) {
      getLogger(Preferences.class.getName()).info("Config file is missing. Creating new one with default config");
      initConfig();
    }
    return preferences;
  }

  public void setPreferences(int daysWithoutFine, float finePerDay, String username, String password) {
    setDaysWithoutFine(daysWithoutFine);
    setFinePerDay(finePerDay);
    setUsername(username);
    setPassword(password);
  }

  public int getDaysWithoutFine() {
    return daysWithoutFine;
  }

  public void setDaysWithoutFine(int daysWithoutFine) {
    this.daysWithoutFine = daysWithoutFine;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String user) {
    this.username = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public float getFinePerDay() {
    return finePerDay;
  }

  public void setFinePerDay(float finePerDay) {
    this.finePerDay = finePerDay;
  }
  private static Writer writer = null;

  public static void initConfig() {
    try {
      Preferences preferences = new Preferences();
      writer = new FileWriter(CONFIG_FILE);
      Gson gson = new Gson();
      gson.toJson(preferences, writer);
    } catch (IOException ex) {
      getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        assert writer != null;
        writer.close();
      } catch (IOException ex) {
        getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);

      }
    }
  }

  public static void writePreferencesToFile(Preferences preferences) {
    try {
      writer = new FileWriter(CONFIG_FILE);
      Gson gson = new Gson();
      gson.toJson(preferences, writer);
      AlertMaker.showSimpleAlert("Success", "Settings has been update successfully");
    } catch (IOException ex) {
      getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
      AlertMaker.showErrorMessage(ex, "Failed", "Cannot save configuration file");
    } finally {
      try {
        assert writer != null;
        writer.close();
      } catch (IOException ex) {
        getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}

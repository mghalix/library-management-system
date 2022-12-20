package com.honda.library.control;

import javax.swing.*;
import java.sql.*;

public class DatabaseHandler {
  private static DatabaseHandler handler = null;
  private static final String DB_URL = "jdbc:mysql://localhost:3306/library", username = "root", password = "3791";
  private static Connection conn = null;
  private static Statement stmt = null;

  private DatabaseHandler() throws SQLException {
    createConnection();
    setUpBookTable();
    setUpMemberTable();
    setUpIssueTable();
  }

  // Singleton Pattern
  public static DatabaseHandler getInstance() throws SQLException {
    if (handler == null)
      handler = new DatabaseHandler();
    return handler;
  }

  void createConnection() throws SQLException {
    conn = DriverManager.getConnection(DB_URL, username, password);
  }

  void setUpBookTable() throws SQLException {
    String TABLE_NAME = "books";
    stmt = conn.createStatement();
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    if (tables.next()) {
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
      return;
    }
    stmt.execute("CREATE TABLE " + TABLE_NAME + "("
            + "id VARCHAR(200) PRIMARY KEY,\n"
            + "title VARCHAR(200),\n"
            + "author VARCHAR(200),\n"
            + "publisher VARCHAR(200),\n"
            + "availability boolean default true"
            + ")");
  }

  public ResultSet execQuery(String query) {
    ResultSet result;
    try {
      stmt = conn.createStatement();
      result = stmt.executeQuery(query);
    } catch (SQLException ex) {
      System.out.println("Exception at execQuery:databaseHandler " + ex.getLocalizedMessage());
      return null;
    }
    return result;
  }

  public boolean execAction(String query) {
    try {
      stmt = conn.createStatement();
      stmt.execute(query);
      return true;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
      System.out.println("Exception at execAction:databaseHandler " + ex.getLocalizedMessage());
      return false;
    }
  }
  public boolean idExists(String id, String tableName) throws SQLException {
    stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "'");
    return rs.next();
  }

  private void setUpMemberTable() throws SQLException {
    String TABLE_NAME = "members";
    stmt = conn.createStatement();
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    if (tables.next()) {
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
      return;
    }
    stmt.execute("CREATE TABLE " + TABLE_NAME + "("
            + "id VARCHAR(200) PRIMARY KEY,\n"
            + "name VARCHAR(200),\n"
            + "mobile VARCHAR(20),\n"
            + "email VARCHAR(100)\n"
            + ")");
  }

  void setUpIssueTable() throws SQLException {
    String TABLE_NAME = "issues";
    Statement stmt = conn.createStatement();
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    if (tables.next()) {
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to go!");
      return;
    }
    stmt.execute("CREATE TABLE " + TABLE_NAME + " ("
            + "book_id VARCHAR(200) PRIMARY KEY,\n"
            + "member_id VARCHAR(200),\n"
            + "issue_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "renew_count INTEGER DEFAULT 0,\n"
            + "FOREIGN KEY (book_id) REFERENCES books(id),\n"
            + "FOREIGN KEY (member_id) REFERENCES members(id)"
            + ")");
  }
}

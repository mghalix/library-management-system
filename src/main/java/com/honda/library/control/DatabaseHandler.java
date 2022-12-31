package com.honda.library.control;

import com.honda.library.model.AlertMaker;
import com.honda.library.model.Book;
import com.honda.library.model.Member;

import javax.swing.*;
import java.sql.*;

public class DatabaseHandler {
  private static DatabaseHandler handler = null;
  private static final String DB_URL = "jdbc:mysql://localhost:3306/library",
          username = "root", password = "3791";
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

  public static boolean updateMember(Member member) {
    try {
      String updateMemberStatement = "UPDATE members SET name=?, mobile=?, " +
              "email=? WHERE id=?";
      PreparedStatement stmt = conn.prepareStatement(updateMemberStatement);
      stmt.setString(1, member.getName());
      stmt.setString(2, member.getMobile());
      stmt.setString(3, member.getEmail());
      stmt.setString(4, member.getId());
      int res = stmt.executeUpdate();
      return (res > 0);
    } catch (SQLException e) {
      return false;
    }
  }

  void createConnection() {
    try {
      conn = DriverManager.getConnection(DB_URL, username, password);
    } catch (SQLException e) {
      AlertMaker.showErrorMessage("Database Error", "Cannot load database");
      e.printStackTrace();
    }
  }

  void setUpBookTable() throws SQLException {
    final String TABLE_NAME = "books";
    stmt = conn.createStatement();
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    if (tables.next()) {
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to " +
              "go!");
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

  public boolean execAction(String axn) {
    try {
      stmt = conn.createStatement();
      stmt.execute(axn);
      return true;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error" +
              " Occurred", JOptionPane.ERROR_MESSAGE);
      System.out.println("Exception at execAction:databaseHandler " + ex.getLocalizedMessage());
      return false;
    }
  }

  public boolean idExists(String id, String tableName) throws SQLException {
    stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE " +
            "id='" + id + "'");
    return rs.next();
  }

  private void setUpMemberTable() throws SQLException {
    String TABLE_NAME = "members";
    stmt = conn.createStatement();
    DatabaseMetaData dbm = conn.getMetaData();
    ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);
    if (tables.next()) {
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to " +
              "go!");
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
      System.out.println("Table " + TABLE_NAME + " already exists. Ready to " +
              "go!");
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

  public boolean deleteBook(Book book) {
    try {
      String delStmt = "DELETE FROM books WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(delStmt);
      stmt.setString(1, book.getId());
      int res = stmt.executeUpdate();
      if (res == 1)
        return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isBookIssued(Book book) {
    String query = "SELECT COUNT(*) FROM issues WHERE book_id = ?";
    try {
      PreparedStatement checkStmt = conn.prepareStatement(query);
      checkStmt.setString(1, book.getId());
      ResultSet rs = checkStmt.executeQuery();
      if (!rs.next()) {
        return false;
      }
      int count = rs.getInt(1);
      System.out.println(count);
      return (count > 0);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public static boolean memberHasAnyBook(Member member) {
    String query = "SELECT COUNT(*) FROM issues WHERE member_id = ?";
    try {
      PreparedStatement checkStmt = conn.prepareStatement(query);
      checkStmt.setString(1, member.getId());
      ResultSet rs = checkStmt.executeQuery();
      if (!rs.next()) {
        return false;
      }
      int count = rs.getInt(1);
      System.out.println(count);
      return (count > 0);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public boolean updateBook(Book book) {
    try {
      String update = "UPDATE books SET title=?, author=?, publisher=? WHERE " +
              "id=?";
      PreparedStatement stmt = conn.prepareStatement(update);
      stmt.setString(1, book.getTitle());
      stmt.setString(2, book.getAuthor());
      stmt.setString(3, book.getPublisher());
      stmt.setString(4, book.getId());
      int res = stmt.executeUpdate();
      return (res > 0);
    } catch (SQLException e) {
      return false;
    }
  }

  public boolean deleteMember(Member member) {
    try {
      String deleteStatement = "DELETE FROM members WHERE id=?";
      PreparedStatement stmt = conn.prepareStatement(deleteStatement);
      stmt.setString(1, member.getId());
      int res = stmt.executeUpdate();
      if (res == 1) return true;
    } catch (SQLException ex) {
      return false;
    }
    return false;
  }
}

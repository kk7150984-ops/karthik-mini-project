package LibraryManagement;

import java.sql.*;
import java.time.LocalDate;

public class LibraryOperations {
    public static final Connection c = DBConnection.getConnection();

    public static void addBook(int id, String title, String author) {
        String query = "INSERT INTO Books(book_id, title, author, available) VALUES(?,?,?,?)";

        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setBoolean(4, true);
            ps.executeUpdate();

            System.out.println("Book Added");
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMember(int id, String name, String phone) {
        String query = "INSERT INTO Members(member_id, name, phone) VALUES(?,?,?)";

        try {
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.executeUpdate();

            System.out.println("Member Added");
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void issueBook(int bookId, int memberId) {
        String check = "SELECT available FROM Books WHERE book_id=?";
        String insert = "INSERT INTO IssuedBooks(book_id, member_id, issue_date) VALUES(?,?,?)";
        String update = "UPDATE Books SET available=false WHERE book_id=?";

        try {
            PreparedStatement checkPs = c.prepareStatement(check);
            checkPs.setInt(1, bookId);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                System.out.println("Book not found");
                return;
            }

            if (!rs.getBoolean("available")) {
                System.out.println("Book is already issued");
                return;
            }

            PreparedStatement issuePs = c.prepareStatement(insert);
            issuePs.setInt(1, bookId);
            issuePs.setInt(2, memberId);
            issuePs.setDate(3, Date.valueOf(LocalDate.now()));
            issuePs.executeUpdate();

            PreparedStatement updatePs = c.prepareStatement(update);
            updatePs.setInt(1, bookId);
            updatePs.executeUpdate();

            System.out.println("Book Issued");

            rs.close();
            checkPs.close();
            issuePs.close();
            updatePs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void returnBook(int bookId) {
        String updateIssue = """
        UPDATE IssuedBooks
        SET return_date=?
        WHERE book_id=? AND return_date IS NULL
        """;

        String updateBook = "UPDATE Books SET available=true WHERE book_id=?";

        try {
            PreparedStatement ps1 = c.prepareStatement(updateIssue);
            ps1.setDate(1, Date.valueOf(LocalDate.now()));
            ps1.setInt(2, bookId);

            int rows = ps1.executeUpdate();

            if (rows == 0) {
                System.out.println("No active issue found for this book");
                return;
            }

            PreparedStatement ps2 = c.prepareStatement(updateBook);
            ps2.setInt(1, bookId);
            ps2.executeUpdate();

            System.out.println("Book Returned");

            ps1.close();
            ps2.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showAvailableBooks() {
        String query = "SELECT * FROM Books WHERE available=true";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);

            System.out.println("\nAvailable Books:");
            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("book_id") +
                    ", Title: " + rs.getString("title") +
                    ", Author: " + rs.getString("author")
                );
            }

            rs.close();
            s.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Sample operations
        addBook(101, "Java Programming", "James Gosling");
        addBook(102, "Python Basics", "Guido van Rossum");

        addMember(1, "Student One", "9876543210");

        showAvailableBooks();

        // Issue a book:
        // issueBook(101, 1);

        // Return a book:
        // returnBook(101);
    }
}

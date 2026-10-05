package LibraryManagement;

import java.sql.*;

public class CreateTables {
    public static void main(String[] args) throws Exception {
        Connection c = DBConnection.getConnection();
        Statement s = c.createStatement();

        String booksTable = """
        CREATE TABLE IF NOT EXISTS Books(
            book_id INT PRIMARY KEY,
            title VARCHAR(200),
            author VARCHAR(150),
            available BOOLEAN DEFAULT TRUE
        )
        """;

        String membersTable = """
        CREATE TABLE IF NOT EXISTS Members(
            member_id INT PRIMARY KEY,
            name VARCHAR(150),
            phone VARCHAR(20)
        )
        """;

        String issueTable = """
        CREATE TABLE IF NOT EXISTS IssuedBooks(
            issue_id INT PRIMARY KEY AUTO_INCREMENT,
            book_id INT,
            member_id INT,
            issue_date DATE,
            return_date DATE,
            FOREIGN KEY(book_id) REFERENCES Books(book_id),
            FOREIGN KEY(member_id) REFERENCES Members(member_id)
        )
        """;

        s.executeUpdate(booksTable);
        s.executeUpdate(membersTable);
        s.executeUpdate(issueTable);

        System.out.println("Tables Created");

        s.close();
        c.close();
    }
}

package LibraryManagement;

import java.sql.*;

public class CreateDatabase {
    public static void main(String[] args) throws Exception {
        String database = "CREATE DATABASE IF NOT EXISTS Library";

        // Connect to MySQL server without selecting a database
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/",
                "root",
                "YOUR_MYSQL_PASSWORD"
        );

        Statement s = c.createStatement();
        s.executeUpdate(database);

        System.out.println("Database Created");

        s.close();
        c.close();
    }
}

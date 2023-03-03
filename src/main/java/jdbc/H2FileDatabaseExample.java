package jdbc;
import org.h2.tools.DeleteDbFiles;

import java.sql.*;

public class H2FileDatabaseExample {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) throws Exception {
        try {
            // delete the H2 database named 'test' in the user home directory

            DeleteDbFiles.execute("~", "test", true);
            insertWithStatement();
            DeleteDbFiles.execute("~", "test", true);
            insertWithPreparedStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // H2 SQL Prepared Statement Example
    private static void insertWithPreparedStatement() throws SQLException {
        Connection connection = getDBConnection();
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement selectPreparedStatement = null;

        String CreateQuery = "CREATE TABLE PERSON(id int primary key, name varchar(255))";
        String InsertQuery = "INSERT INTO PERSON" + "(id, name) values" + "(?,?)";
        String SelectQuery = "select * from PERSON";
        try {
            connection.setAutoCommit(false);
           
            createPreparedStatement = connection.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();
           
            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setString(2, "John");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.setInt(1, 2);
            insertPreparedStatement.setString(2, "Ali");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.setInt(1, 3);
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
           
            selectPreparedStatement = connection.prepareStatement(SelectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            System.out.println("H2 Database inserted through PreparedStatement");
            System.out.println("Number of columns : "+rs.getMetaData().getColumnCount());
            System.out.println("First column name : "+rs.getMetaData().getColumnName(1));
            System.out.println("First column type : "+rs.getMetaData().getColumnTypeName(1));
            System.out.println("First column name : "+rs.getMetaData().getColumnName(2));
            System.out.println("First column type : "+rs.getMetaData().getColumnTypeName(2));
            while (rs.next()) {
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString(2));
            }
            selectPreparedStatement.close();
           
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    // H2 SQL Statement Example
    private static void insertWithStatement() throws SQLException {
        Connection connection = getDBConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stmt.execute("CREATE TABLE PERSON(id int primary key, name varchar(255))");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(1, 'Ali')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(2, 'Sonia')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Jane')");

            ResultSet rs = stmt.executeQuery("select * from PERSON");
            System.out.println("H2 Database inserted through Statement");
            //rs.beforeFirst();
            while (rs.next()) {
                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
            }
//            test Update
//            rs.absolute(2);
//            System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
//            rs.updateString(2,"XXX");
//            System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));


//            rs.afterLast();
//            while (rs.previous()) {
//                System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
//            }
//            rs.absolute(1);
//            System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));
//            rs.relative(2);
//            System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("name"));

            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
}
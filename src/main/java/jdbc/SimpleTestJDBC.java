package jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleTestJDBC {

    public static void main(String[] args)  {

            var url = "jdbc:h2:mem:test";
           // Class.forName("org.h2.Driver" );
            try (var con = DriverManager.getConnection(url);
                 var stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE
                         ,ResultSet.CONCUR_UPDATABLE);
                 var rs = stm.executeQuery("SELECT 1+1")) {

                if (rs.next()) {

                    System.out.println(rs.getInt(1));
                }

            } catch (SQLException ex) {

                var lgr = Logger.getLogger(SimpleTestJDBC.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

}

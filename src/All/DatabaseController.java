package All;

import java.sql.*;

import java.util.List ;
import java.util.ArrayList ;

public class DatabaseController {
    // in real life, use a connection pool....
    private Connection connection ;

    public DatabaseController(String driverClassName, String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
       connection = DriverManager.getConnection(dbURL, user, password);
    }

    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    public ResultSet getAllPersonalInfo(int id) throws SQLException {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from PersonalInfo where idPersonalInfo = "+id);
        ){
            return rs;
        }
    }
    // other methods, eg. addPerson(...) etc
}
package All;

import java.math.BigDecimal;
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
    public Person getAllPersonalInfo(int id) throws SQLException {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from PersonalInfo where idPersonalInfo = "+id);
        ){
            //double check all these names
            String firstName = rs.getString("Forename");
            String lastName = rs.getString("Surname");
            String Username = rs.getString("Username");
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            Date DOB = rs.getDate("DOB");
            int weight = rs.getInt("CurrentWeight");
            int goalID = rs.getInt("CurrentGoal");
            BigDecimal height = rs.getBigDecimal("Height");
            Person user = new Person(id,firstName, lastName,Username, email,password,DOB,height,weight,goalID);
            return user;
        }
    }
    public void addPerson(Person User) throws SQLException {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("Insert Into PersonalInfo ");
        ){}
    }
    // other methods, eg. addPerson(...) etc
}
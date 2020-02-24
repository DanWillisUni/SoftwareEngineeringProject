package All;

import java.math.BigDecimal;
import java.sql.*;

public class DatabaseController {
    private Connection connection ;

    public DatabaseController() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwareengineering", "SoftwareEngineering","SoftwareEngineering");
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
                ResultSet rs = stmnt.executeQuery("Insert Into PersonalInfo Values(" + generateUserID() + ", " + User.getForename()+ ", " + User.getSurname()+ ", " + User.getUsername()+ ", " + User.getEmail()+ ", " + User.getPassword()+ ", " + User.getDOB()+ ", " + User.getGoalID()+ ", " + User.getCurrentWeight()+ ", " + User.getHeight()+ ")");
        ){}
    }
    public int generateUserID(){
        //connect to db
        //get all ids
        //find highest id
        //add one to it
        return 0;
    }
    // other methods, eg. addPerson(...) etc
}
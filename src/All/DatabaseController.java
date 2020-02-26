package All;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseController {
    private Connection connection;

    public DatabaseController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwareengineering", "root","rootroot");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void addUser(Person User){
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            addPerson(this.generateUserID(),User.getForename(),User.getSurname(),User.getUsername(),User.getEmail(),User.getPassword(), df.format(User.getDOB()),Integer.toString(User.getGoalID()), Integer.toString(User.getCurrentWeight()),User.getHeight().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPerson(int ID,String fn,String sn,String un,String e,String p,String DOB,String GID,String cw,String h) throws SQLException {
        final String query = "Insert Into softwareengineering.PersonalInfo Values("+ ID + ", '" + fn + "', '" + sn+ "', '" + un+ "', '" + e+ "', '" + p+ "', " + DOB+ ", " + GID+ ", " + cw+ ", " + h+ ")";
        try (Statement stmnt = connection.createStatement()
        ){int r = stmnt.executeUpdate(query);
        }
    }
    private ArrayList<Integer> getAllIDs(){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select softwareengineering.idPersonalInfo from PersonalInfo");
        ){
            ArrayList<Integer> IDs = new ArrayList<>();
            while (rs.next()) {
                IDs.add(rs.getInt("idPersonalInfo"));
            }
            return IDs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private int generateUserID(){
        ArrayList<Integer> ids = getAllIDs();
        Collections.sort(ids);
        return ids.get(ids.size()-1) +1 ;
    }
    public String getMatchingPassword(String email){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select Password from softwareengineering.personalinfo Where Email= '" + email + "'");
        ){
            if (rs.first()) {
                return rs.getString("Password");
            }
    } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getMatchingID(String email){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select idPersonalInfo from softwareengineering.personalinfo Where Email= '" + email + "'");
        ){
            if (rs.first()) {
                return rs.getString("idPersonalInfo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
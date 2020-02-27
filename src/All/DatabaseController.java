package All;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
    public Person getAllPersonalInfo(int id) {
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.PersonalInfo where idUser = '"+id +"'");
        ){
            if(rs.next()){
                String firstName = rs.getString("forename");
                String lastName = rs.getString("surname");
                String Username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Date DOB = rs.getDate("DOB");
                BigDecimal height = rs.getBigDecimal("height");
                BigDecimal weight = rs.getBigDecimal("currentWeight");
                char gender = rs.getString("gender").charAt(0);
                Person user = new Person(id,firstName, lastName,Username, email,password,DOB,height,weight,gender);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addUser(Person User){
        try {
            final String query = "Insert Into softwareengineering.PersonalInfo Values("+ this.generateUserID() + ", '" + User.getForename() + "', '" + User.getSurname()+ "', '" + User.getEmail()+ "', '" + User.getUsername()+ "', '" + User.getPassword()+ "', ? , " + User.getHeight().toString()+ ", "+ User.getCurrentWeight().toString()+  ", '" + User.getGender() + "' )";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(User.getDOB().getTime()));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<Integer> getAllIDs(){
        ArrayList<Integer> IDs = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select idUser from softwareengineering.PersonalInfo");
        ){
            while (rs.next()) {
                IDs.add(rs.getInt("idUser"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IDs;
    }
    private int generateUserID(){
        ArrayList<Integer> ids = getAllIDs();
        if (ids.size()>0){
            Collections.sort(ids);
            return ids.get(ids.size()-1) +1 ;
        } else {
            return 0;
        }
    }
    public String getMatchingPassword(String email){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select password from softwareengineering.personalinfo Where email= '" + email + "'");
        ){
            if (rs.first()) {
                return rs.getString("password");
            }
    } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getMatchingID(String email){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select idUser from softwareengineering.personalinfo Where email= '" + email + "'");
        ){
            if (rs.first()) {
                return rs.getString("idUser");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
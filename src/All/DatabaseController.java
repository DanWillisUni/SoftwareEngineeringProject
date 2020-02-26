package All;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
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
                String password = rs.getString("Password");
                Date DOB = rs.getDate("DOB");
                BigDecimal height = rs.getBigDecimal("height");
                BigDecimal weight = rs.getBigDecimal("currentWeight");
                Person user = new Person(id,firstName, lastName,Username, email,password,DOB,height,weight);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addUser(Person User){
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
        try {
            addPerson(this.generateUserID(),User.getForename(),User.getSurname(),User.getUsername(),User.getEmail(),User.getPassword(), User.getDOB(),User.getHeight().toString(),User.getCurrentWeight().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addPerson(int ID, String fn, String sn, String un, String e, String p, Date DOB, String h, String cw) throws SQLException {
        final String query = "Insert Into softwareengineering.PersonalInfo Values("+ ID + ", '" + fn + "', '" + sn+ "', '" + un+ "', '" + e+ "', '" + p+ "', ? , " + h+ ", "+ cw+  " )";
        try (
                PreparedStatement pstmt = connection.prepareStatement(query)
        ){
            pstmt.setDate(1, new java.sql.Date(DOB.getTime()));
            pstmt.executeUpdate();
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
                ResultSet rs = stmnt.executeQuery("select Password from softwareengineering.personalinfo Where email= '" + email + "'");
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
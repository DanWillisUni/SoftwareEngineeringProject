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
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int genID(String TableName,String ColumName){
        ArrayList<Integer> ids = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering." + TableName);
        ){
            while (rs.next()) {
                ids.add(rs.getInt(ColumName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ids.size()>0){
            Collections.sort(ids);
            return ids.get(ids.size()-1) +1 ;
        } else {
            return 0;
        }
    }
    public ArrayList<String> getAllLike(String s,String TableName,String ColumnName){
        ArrayList<String> things = new ArrayList<>();
        String sql = "SELECT * FROM softwareengineering."+TableName+" WHERE "+ ColumnName+" LIKE ? ";
        try (
                PreparedStatement pst=connection.prepareStatement(sql);
        ){
            pst.setString(1, "%" + s + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                things.add(rs.getString(ColumnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return things;
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
            final String query = "Insert Into softwareengineering.PersonalInfo Values("+ this.genID("PersonalInfo","idUser") + ", '" + User.getForename() + "', '" + User.getSurname()+ "', '" + User.getEmail()+ "', '" + User.getUsername()+ "', '" + User.getPassword()+ "', ? , " + User.getHeight().toString()+ ", "+ User.getCurrentWeight().toString()+  ", '" + User.getGender() + "' )";
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

    public void addWeight(int id,String weight){
        try {
            final String first ="Delete From softwareengineering.weightTracking Where idUser = " +id +" And date = ?";
            final String second = "Insert Into softwareengineering.weightTracking Values("+ id + ", ? , '" + weight + "' )";
            try (
                    PreparedStatement pstmt1 = connection.prepareStatement(first)
            ){
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                pstmt1.setDate(1, currentDate);
                pstmt1.executeUpdate();
                try (
                        PreparedStatement pstmt2 = connection.prepareStatement(second)
                ){
                    pstmt2.setDate(1, currentDate);
                    pstmt2.executeUpdate();

                    final String query = "Update softwareengineering.personalInfo Set currentWeight = " + weight + " Where idUser = " + id;
                    try (
                            PreparedStatement pstmt3 = connection.prepareStatement(query)
                    ){
                        pstmt3.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExerciseSession(BigDecimal duration,int sportID,int calburned,int userID){
        try {
            final String query = "Insert Into softwareengineering.exercisesession Values("+ genID("exercisesession","idExerciseSession") + ", ? , '" + duration + "' ,"+ sportID+ " ,"+ calburned + " ,"+ userID + ")";
            try (
                    PreparedStatement pstmt1 = connection.prepareStatement(query)
            ){
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                pstmt1.setDate(1, currentDate);
                pstmt1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getIDFromName(String name){
        int r = 0;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.exercise where exerciseName = '"+name +"'");
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString("idExerciseType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public int getCalsBurnedFromID(int id){
        int r = 0;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.exercise where idExerciseType = '"+id+"'");
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString("calsPerMinute"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public int addMeal(String foodName,int quantity,String Type){
        int id;
        int foodID = getFoodIDFromName(foodName);
        int made = selectMeal(foodID,quantity,Type);
        if (made==-1){
            id = genID("meal","idmeal");
            try {
                final String query = "Insert Into softwareengineering.meal Values("+ id +", " + foodID + ", " + quantity+", '" +Type+"')";
                try (
                        PreparedStatement pstmt1 = connection.prepareStatement(query)
                ){
                    pstmt1.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            id = made;
        }
        return id;
    }
    private int selectMeal(int foodID,int quantity,String Type){
        int r = -1;
        try (
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.meal where idFood ="+foodID + " AND quantity = " + quantity+" AND mealCategory = '" +Type+"'");

        ){
            if(rs.next()) {
                r = rs.getInt("idMeal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private int getFoodIDFromName(String name){
        int r = -1;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("Select * From softwareengineering.foods where foodName = '"+name+"'");
        ){
            if(rs.next()) {
                r = rs.getInt("idFood");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    public void addDiet(int mealID,int userID){
        try {
            final String query = "Insert Into softwareengineering.diet Values("+ genID("diet","idDiet") +", " + userID + ", " + mealID+", ?)";
            try (
                    PreparedStatement pstmt1 = connection.prepareStatement(query)
            ){
                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                pstmt1.setDate(1, currentDate);
                pstmt1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGoal(){
        
    }
    public void addGoalLink(){

    }
}
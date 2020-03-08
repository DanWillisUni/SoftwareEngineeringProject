package Model;

import All.Person;//other class imports
//java imports
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class DatabaseController {
    private Connection connection;//connection to the database
    //used everywhere
    /**
     * Creates a connection to the server
     */
    public DatabaseController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwareengineering", "root","rootroot");//my username and password
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * closes the connection to the local server
     */
    public void shutdown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //generic functions
    /**
     * Generates an id that hasnt been used before
     * Selects all the ids in that column
     * Sorts them
     * gets the largest one and adds one to it
     * @param TableName the name of the table to generate the id in
     * @param ColumName the column name of the id
     * @return a unique id
     */
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

    /**
     * Select all ColumnName where they are like %s%
     * @param s string to search
     * @param TableName the name of the table to search in
     * @param ColumnName the name of the column to search in
     * @return an array list of all the elements in ColumnName like s
     */
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

    /**
     * select all from the column
     * if any of them match str return true
     * if none match str return false
     * @param str element to search for
     * @param TableName the name of the table to look in
     * @param ColumnName the column to look in
     * @return if str is in the column
     */
    public boolean isStr(String str,String TableName,String ColumnName){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering." + TableName);
        ){
            while(rs.next()){
                String s = rs.getString(ColumnName);
                if(s.equals(str)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * select the id column from TableName where ColumnName is equal to name
     * @param name the name to match
     * @param TableName the table name
     * @param ColumnName the name column
     * @param ColumnIDName the id column
     * @return
     */
    public int getIDFromName(String name,String TableName,String ColumnName,String ColumnIDName){
        int r = -1;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering."+ TableName +" where " + ColumnName + " = '"+name +"'");
        ){
            if(rs.next()){
                r = Integer.parseInt(rs.getString(ColumnIDName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    //login
    /**
     * Selects all info from personalinfo where the user id is the same as id
     * create new Person object from all the info
     * return new person
     * @param id the id of the user
     * @return User with idUser id
     */
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
                char gender = rs.getString("gender").charAt(0);
                Person user = new Person(id,firstName, lastName,Username, email,password,DOB,height,gender);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * selects the password where the email matches
     * @param email email to match
     * @return password if there is any
     */
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
    //registration
    /**
     * Inserts into personal info the new person
     * @param User Person object to add to database
     */
    public void addUser(Person User){
        try {
            final String query = "Insert Into softwareengineering.PersonalInfo Values("+ this.genID("PersonalInfo","idUser") + ", '" + User.getForename() + "', '" + User.getSurname()+ "', '" + User.getEmail()+ "', '" + User.getUsername()+ "', '" + User.getPassword()+ "', ? , " + User.getHeight().toString()+ ", '" + User.getGender() + "' )";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(User.getDOB().getTime()));//sets the date to the current
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //add weight
    /**
     * Adds a weight
     * Deletes any weight entry by the same user that day
     * Inserts weight
     * @param id
     * @param weight
     */
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkGoalMet(int id,String weight){
        boolean r = false;
        ArrayList<Integer> goalIDs = getGoalIDFromID(id);
        int cw = Integer.parseInt(weight);
        for (int goalID:goalIDs){
            if (getGoalWeight(goalID) >= cw){
                r = true;
                DelGoalLink(id,goalID);
            }
        }
        return r;
    }
    private void DelGoalLink(int id,int goalID){
        final String query = "Delete from softwareengineering.goallink Where idUser= "+ id + " And idGoalWeight = " + goalID;
        try (
                PreparedStatement pstmt = connection.prepareStatement(query)
        ){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<Integer> getGoalIDFromID(int id){
        ArrayList<Integer> r = new ArrayList<>();
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.goallink Where idUser= "+ id);
        ){
            while (rs.next()) {
                r.add(rs.getInt("idGoalWeight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private int getGoalWeight(int id){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.goalweight Where idGoalWeight=" + id);
        ){
            if (rs.first()) {
                return rs.getInt("weightGoal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private int getCurrentWeight(int id){
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.weighttracking Where idUser=" + id + " order by date desc");
        ){
            if (rs.first()) {
                return rs.getInt("weight");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addExerciseLink(int sessionID,int userID){
        int id =genID("exerciselink","idLink");
        try {
            final String query = "Insert Into softwareengineering.exerciselink Values("+ id + ", " + userID  + " ,"+ sessionID + " ,?)";
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
    public int addExerciseSession(BigDecimal duration,int sportID,int calburned){
        int id = getExerciseSessionID(duration,sportID,calburned);
        if (id == -1){
            id =genID("exercisesession","idExerciseSession");
            try {
                final String query = "Insert Into softwareengineering.exercisesession Values("+ id + ", '" + duration + "' ,"+ sportID+ " ,"+ calburned + ")";
                try (
                        PreparedStatement pstmt1 = connection.prepareStatement(query)
                ){
                    pstmt1.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
    private int getExerciseSessionID(BigDecimal duration,int sportID,int calburned){
        int r = -1;
        try (
                Statement stmnt = connection.createStatement();
                ResultSet rs = stmnt.executeQuery("select * from softwareengineering.exercisesession where idExerciseType = " + sportID+" AND durationMinutes = " +duration +" and caloriesBurned = "+calburned);
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
        int foodID = getIDFromName(foodName,"foods","foodName","idFood");
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

    public void addGoal(int id,int targetWeight, Date targetDate){
        int idGoalWeight = selectGoal(targetWeight,new Date(),targetDate);
        if (idGoalWeight == -1){
            idGoalWeight = genID("goalweight","idGoalWeight");
            createGoal(idGoalWeight,targetWeight,new Date(),targetDate);
        }
        addGoalLink(id,idGoalWeight);
    }
    private int selectGoal(int targetWeight,Date setDate, Date targetDate){
        int r = -1;
        try {
            final String query = "SELECT * FROM softwareengineering.goalweight WHERE dateSet = ? AND targetDate = ? AND weightGoal = " + targetWeight;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(setDate.getTime()));
                pstmt.setDate(2, new java.sql.Date(targetDate.getTime()));
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    r = rs.getInt("idGoalWeight");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private void addGoalLink(int id,int idGoalWeight){
        try {
            final String query = "Insert Into softwareengineering.goallink Values(" + id + ", " + idGoalWeight+")";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void createGoal(int id,int targetWeight,Date set, Date targetDate){
        try {
            final String query = "Insert Into softwareengineering.goalweight Values("+ id +", " + targetWeight +", ?,?)";
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(set.getTime()));
                pstmt.setDate(2, new java.sql.Date(targetDate.getTime()));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCalConsumed(int id,Date d){
        int r = 0;
        ArrayList<Integer> idMeals = getMealsFromID(id,d);
        for (Integer idMeal:idMeals){
            int q = getQuantityFromMeal(idMeal);
            int cal = getCalCountFromFoodID(getFoodIDFromMeal(idMeal));
            r+=(q*cal);
        }
        return r;
    }
    private ArrayList<Integer> getMealsFromID(int id,Date d){
        ArrayList<Integer> i = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.diet WHERE date = ? AND idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(d.getTime()));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    i.add(rs.getInt("idMeal"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
    private int getQuantityFromMeal(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idMeal = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private int getFoodIDFromMeal(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.meal WHERE idMeal = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("idFood"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private int getCalCountFromFoodID(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.foods WHERE idFood = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r = (rs.getInt("amountOfCalories"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public int getCalBurned(int id,Date d){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.exerciselink WHERE date = ? AND idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                pstmt.setDate(1, new java.sql.Date(d.getTime()));
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    r+=getCalBurnedFromSessionID(rs.getInt("idExerciseSession"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    private int getCalBurnedFromSessionID(int id){
        int r = 0;
        try {
            final String query = "SELECT * FROM softwareengineering.exercisesession WHERE idExerciseSession = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    r=(rs.getInt("caloriesBurned"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    public ArrayList<Integer> getWeightTrackingWeight(int id){
        ArrayList<Integer> w = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.weighttracking WHERE idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    w.add(rs.getInt("weight"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return w;
    }
    public ArrayList<java.util.Date> getWeightTrackingDate(int id){
        ArrayList<java.util.Date> d = new ArrayList<>();
        try {
            final String query = "SELECT * FROM softwareengineering.weighttracking WHERE idUser = " + id;
            try (
                    PreparedStatement pstmt = connection.prepareStatement(query)
            ){
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    d.add(new java.util.Date(rs.getDate("date").getTime()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
}
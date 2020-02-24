package All;

import java.math.BigDecimal;
import java.sql.Date;

public class Person {
    int ID;
    String forename;
    String surname;
    String username;
    String email;
    String password;
    Date DOB;
    BigDecimal height;
    int currentWeight;
    int goalID;
    public Person(int ID, String forename, String surname,String username, String email, String password, Date DOB, BigDecimal height, int currentWeight, int goalID){
        this.ID = ID;
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.currentWeight = currentWeight;
        this.goalID = goalID;
    }
    public Person(String forename, String surname,String username, String email, String password, Date DOB, BigDecimal height){
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
    }
    public int getID(){
        return ID;
    }
    public String getForename(){
        return forename;
    }
    public String getSurname(){
        return surname;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public Date getDOB(){
        return DOB;
    }
    public BigDecimal getHeight(){
        return height;
    }
    public int getCurrentWeight(){
        return currentWeight;
    }
    public int getGoalID(){
        return goalID;
    }
}

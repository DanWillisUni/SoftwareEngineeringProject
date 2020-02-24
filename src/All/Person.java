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
    public Person(String forename,String surname,String username,String email,String password,Date DOB,BigDecimal height){
        this.ID = generateID();
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
    }
    private int generateID(){
        //connect to db
        //get all ids
        //find highest id
        //add one to it
        return 0;
    }
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
}

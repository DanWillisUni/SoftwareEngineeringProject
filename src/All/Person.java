package All;

import java.sql.Date;

public class Person {
    int ID;
    String forename;
    String surname;
    String email;
    String password;
    Date DOB;
    Double height;
    int currentWeight;
    int goalID;
    public Person(String forename,String surname,String email,String password,Date DOB,Double height){
        this.ID = generateID();
        this.forename = forename;
        this.surname = surname;
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
}

package All;

import java.math.BigDecimal;
import java.util.Date;

public class Person {
    int ID;
    String forename;
    String surname;
    String username;
    String email;
    String password;
    Date DOB;
    BigDecimal height;
    BigDecimal currentWeight;
    char gender;
    public Person(int ID, String forename, String surname, String username, String email, String password, Date DOB, BigDecimal height, BigDecimal currentWeight,char gender){
        this.ID = ID;
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.currentWeight = currentWeight;
        this.gender = gender;
    }
    public Person(String forename, String surname, String username, String email, String password, Date DOB, BigDecimal height,char gender){
        this.forename = forename;
        this.surname = surname;
        this.username = username;
        this.email=email;
        this.password=password;
        this.DOB=DOB;
        this.height=height;
        this.currentWeight= BigDecimal.valueOf(0.0);
        this.gender = gender;
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
    public BigDecimal getCurrentWeight(){
        return currentWeight;
    }
    public char getGender(){return gender;}
}

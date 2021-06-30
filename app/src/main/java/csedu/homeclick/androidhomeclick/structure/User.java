package csedu.homeclick.androidhomeclick.structure;

import java.io.Serializable;

public class User implements Serializable {
    private String Name;
    private String EmailAddress;
    private String PhoneNumber;
    private String UID;

    public User() {

    }

    public User(String name, String emailAddress, String phoneNumber) {
        Name = name;
        EmailAddress = emailAddress;
        PhoneNumber = phoneNumber;
    }

    public User(String name, String emailAddress, String phoneNumber, String UID) {
        Name = name;
        EmailAddress = emailAddress;
        PhoneNumber = phoneNumber;
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

}

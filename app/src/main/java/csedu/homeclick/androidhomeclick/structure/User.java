package csedu.homeclick.androidhomeclick.structure;

import java.util.HashMap;

public class User {
    private String Name;
    private String EmailAddress;
    private String PhoneNumber;

    public User() {

    }

    public User(String name, String emailAddress, String phoneNumber) {
        Name = name;
        EmailAddress = emailAddress;
        PhoneNumber = phoneNumber;
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

    public HashMap<String, Object> getUserHashMap() {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("Name", getName());
        userMap.put("EmailAddress", getEmailAddress());
        userMap.put("PhoneNumber", getPhoneNumber());
        return userMap;
    }

}

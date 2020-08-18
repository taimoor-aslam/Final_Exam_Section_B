package com.example.finalexamsectionb;

public class UserData {
    private String id;
    private String mName;
    private String dateOfJoining;
    private String mEmail;
    private String mPasscode;
    private String userType;
    private String marketingSector;
    private String mPhoto;
    private byte[] bphoto;
    public UserData()
    {

    }
//    public MyListData(String id,String mName, String mAge, String mEmail, String mPasscode, String mHobbies, String mGender, String mPhoto)
//    {
//        this.id=id;
//        this.mName=mName;
//        this.mAge=mAge;
//        this.mEmail=mEmail;
//        this.mPasscode=mPasscode;
//        this.mHobbies=mHobbies;
//        this.mGender=mGender;
//        this.mPhoto=mPhoto;
//
//    }
    public UserData(String id, String mName, String date, String mEmail, String mPasscode, String uType, String sector, String photo)
    {
        this.id=id;
        this.mName=mName;
        this.dateOfJoining=date;
        this.mEmail=mEmail;
        this.mPasscode=mPasscode;
        this.userType=uType;
        this.marketingSector=sector;
        this.mPhoto=photo;

    }
    public String getmName()
    {
        return mName;
    }
    public String getmDateOfJoining()
    {
        return dateOfJoining;
    }
    public String getmEmail()
    {
        return mEmail;
    }
    public String getmPasscode()
    {
        return mPasscode;
    }
    public String getmID()
    {
        return id;
    }
    public String getmUserType()
    {
        return  userType;
    }
    public String getmMarketeSecor()
    {
        return marketingSector;
    }
    public void setmPhoto(String mPhoto)
    {
        this.mPhoto=mPhoto;
    }
    public void setmName(String mName)
    {
        this.mName=mName;
    }

    public void setmEmail(String mEmail)
    {
        this.mEmail=mEmail;
    }
    public String getmPhoto()
    {
        return mPhoto;
    }
    public void setmPasscode(String mPasscode)
    {
        this.mPasscode=mPasscode;
    }


}

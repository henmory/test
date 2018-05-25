package com.digiarty.phoneassistant.bean;

/***
 *
 * Created on：09/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContactReadBean {

    private String name;
    private String phone_number;
    private int phone_type;
    private String mail;

    public ContactReadBean() {
    }

    public ContactReadBean(String name, String phone_number, int phone_type, String mail) {
        this.name = name;
        this.phone_number = phone_number;
        this.phone_type = phone_type;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPhoneType() {
        return phone_type;
    }

    public void setPhoneType(int phone_type) {
        this.phone_type = phone_type;
    }

    @Override
    public String toString() {
        return "ContactReadBean{" +
                "name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", phone_type='" + phone_type + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}

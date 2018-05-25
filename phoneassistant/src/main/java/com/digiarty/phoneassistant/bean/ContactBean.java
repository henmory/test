package com.digiarty.phoneassistant.bean;

import java.util.Arrays;
import java.util.List;

/***
 *
 * Created on：2018/5/24
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContactBean {

    private String key ;//联系人(本地联系人id与key对应)

    //照片
    private byte[] image; //照片

    //姓名
    private String firstName ; //名称
    private String prefix ;//前缀
    private String lastName ; //姓
    private String sufix ;//后缀
    private String middleName ;

    //组织
    private String jobTitle ;//职位
    private String companyName ; //公司名
    private String department ; //部门

    //电话列表
    private List<ContactKeyValueEntity> phoneNumberList;


    //邮箱列表
    private List<ContactKeyValueEntity> emailAddressList;


    //群组[0]

    private List<ContactAddressEntity> addressList; //地址
    private List<ContactKeyValueEntity> urlList ;//网址
    private List<ContactKeyValueEntity> event; //事件日期
    private String notes ;//注释
    private List<ContactKeyValueEntity> imList ;//即时通信账号
    private String nickname ;//昵称
    //关系
    private List<ContactKeyValueEntity> relatedNameList; //关系


    //铃声
    //信息提示音
    //震动方式

//    private String recordEntityName ;
//    private int rowID;
//    private String suffix ;
//    private String title ;
//    private List<ContactDateEntity> dateList;
//    private String displayAsCompany ;

    public class ContactAddressEntity
    {
        private String type ;//类型
        private String street ;//街道
        private String city ; //城市
        private String state ; //州
        private String postalCode ;//邮编
        private String country ; //国家

//        private String contactKey ; //联系人key
//        private String countryCode ;//国家码
//        private String key ;
//        private String label ;
//        private String recordEntityName ;
//        private String uID ;
    }

//    public class ContactDateEntity
//    {
//        private String contactKey ;
//        private String key ;
//        private String label ;
//        private String recordEntityName ;
//        private String type ;
//        private Date value;
//    }

    public class ContactKeyValueEntity {
//        private String contactKey;
//        private String key;
//        private String label;
//        private String recordEntityName;
        private String type;//类型
        private String value; //号码
    }


//    public class ContactIMEntity {
//        private String contactKey ;
//        private String key ;
//        private String label ;
//        private String recordEntityName ;
//        private String service ;
//        private String type ;
//        private String uID ;
//        private String user ;
//    }


    public ContactBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ContactKeyValueEntity> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<ContactKeyValueEntity> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public List<ContactKeyValueEntity> getEmailAddressList() {
        return emailAddressList;
    }

    public void setEmailAddressList(List<ContactKeyValueEntity> emailAddressList) {
        this.emailAddressList = emailAddressList;
    }

    public List<ContactAddressEntity> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<ContactAddressEntity> addressList) {
        this.addressList = addressList;
    }

    public List<ContactKeyValueEntity> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<ContactKeyValueEntity> urlList) {
        this.urlList = urlList;
    }

    public List<ContactKeyValueEntity> getEvent() {
        return event;
    }

    public void setEvent(List<ContactKeyValueEntity> event) {
        this.event = event;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ContactKeyValueEntity> getImList() {
        return imList;
    }

    public void setImList(List<ContactKeyValueEntity> imList) {
        this.imList = imList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<ContactKeyValueEntity> getRelatedNameList() {
        return relatedNameList;
    }

    public void setRelatedNameList(List<ContactKeyValueEntity> relatedNameList) {
        this.relatedNameList = relatedNameList;
    }

    @Override
    public String toString() {
        return "ContactBean{" +
                "key='" + key + '\'' +
                ", image=" + Arrays.toString(image) +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", department='" + department + '\'' +
                ", phoneNumberList=" + phoneNumberList +
                ", emailAddressList=" + emailAddressList +
                ", addressList=" + addressList +
                ", urlList=" + urlList +
                ", event=" + event +
                ", notes='" + notes + '\'' +
                ", imList=" + imList +
                ", nickname='" + nickname + '\'' +
                ", relatedNameList=" + relatedNameList +
                '}';
    }
}

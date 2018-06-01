package com.digiarty.phoneassistant.model.bean.beanfromclient;

import com.digiarty.phoneassistant.bean.ContactBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 *
 * Created on：2018/5/31
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class AddContactDataFromClientBean {
    private String command;
    private List<contactBeans> contactBeans;

    public AddContactDataFromClientBean() {
        contactBeans = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "AddContactDataFromClientBean{" +
                "command='" + command + '\'' +
                ", contactBeans=" + contactBeans +
                '}';
    }

    public List<AddContactDataFromClientBean.contactBeans> getContactBeans() {
        return contactBeans;
    }

    public void setContactBeans(List<AddContactDataFromClientBean.contactBeans> contactBeans) {
        this.contactBeans = contactBeans;
    }

    public class contactBeans{

        private String key;//联系人(本地联系人id与key对应)
        //照片
        private String image; //照片
        //姓名
        private String firstName; //名称
        private String prefix;//前缀
        private String lastName; //姓
        private String sufix;//后缀
        private String middleName;
        //组织
        private String jobTitle;//职位
        private String companyName; //公司名
        private String department; //部门
        //电话列表
        private List<ContactBean.ContactKeyValueEntity> phoneNumberList;
        //邮箱列表
        private List<ContactBean.ContactKeyValueEntity> emailAddressList;
        //群组[0]
        private List<ContactBean.ContactAddressEntity> addressList; //地址
        private List<ContactBean.ContactKeyValueEntity> urlList;//网址
        private List<ContactBean.ContactKeyValueEntity> event; //事件日期
        private String notes;//注释
        private List<ContactBean.ContactKeyValueEntity> imList;//即时通信账号
        private String nickname;//昵称
        //关系
        private List<ContactBean.ContactKeyValueEntity> relatedNameList; //关系

        public contactBeans() {
            phoneNumberList = new ArrayList<>();
            emailAddressList = new ArrayList<>();
            addressList = new ArrayList<>();
            urlList = new ArrayList<>();
            event = new ArrayList<>();
            imList = new ArrayList<>();
            relatedNameList = new ArrayList<>();
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getSufix() {
            return sufix;
        }

        public void setSufix(String sufix) {
            this.sufix = sufix;
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

        public List<ContactBean.ContactKeyValueEntity> getPhoneNumberList() {
            return phoneNumberList;
        }

        public void setPhoneNumberList(List<ContactBean.ContactKeyValueEntity> phoneNumberList) {
            this.phoneNumberList = phoneNumberList;
        }

        public List<ContactBean.ContactKeyValueEntity> getEmailAddressList() {
            return emailAddressList;
        }

        public void setEmailAddressList(List<ContactBean.ContactKeyValueEntity> emailAddressList) {
            this.emailAddressList = emailAddressList;
        }

        public List<ContactBean.ContactAddressEntity> getAddressList() {
            return addressList;
        }

        public void setAddressList(List<ContactBean.ContactAddressEntity> addressList) {
            this.addressList = addressList;
        }

        public List<ContactBean.ContactKeyValueEntity> getUrlList() {
            return urlList;
        }

        public void setUrlList(List<ContactBean.ContactKeyValueEntity> urlList) {
            this.urlList = urlList;
        }

        public List<ContactBean.ContactKeyValueEntity> getEvent() {
            return event;
        }

        public void setEvent(List<ContactBean.ContactKeyValueEntity> event) {
            this.event = event;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public List<ContactBean.ContactKeyValueEntity> getImList() {
            return imList;
        }

        public void setImList(List<ContactBean.ContactKeyValueEntity> imList) {
            this.imList = imList;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public List<ContactBean.ContactKeyValueEntity> getRelatedNameList() {
            return relatedNameList;
        }

        public void setRelatedNameList(List<ContactBean.ContactKeyValueEntity> relatedNameList) {
            this.relatedNameList = relatedNameList;
        }

        @Override
        public String toString() {
            return "AddContactDataFromClientBean{" +
                    "key='" + key + '\'' +
                    ", image=" + image +
                    ", firstName='" + firstName + '\'' +
                    ", prefix='" + prefix + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", sufix='" + sufix + '\'' +
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


    public class ContactAddressEntity {
        private String type;//类型
        private String street;//街道
        private String city; //城市
        private String state; //州
        private String postalCode;//邮编
        private String country; //国家

        public ContactAddressEntity() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public String toString() {
            return "ContactAddressEntity{" +
                    "type='" + type + '\'' +
                    ", street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", postalCode='" + postalCode + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
    public class ContactKeyValueEntity {
        private String type;//类型
        private String value; //号码

        public ContactKeyValueEntity() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ContactKeyValueEntity{" +
                    "type='" + type + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }


}

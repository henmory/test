package com.digiarty.phoneassistant.model.bean.beanfromclient;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/***
 *
 * Created on：2018/6/1
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContactBean {

    @JSONField(name = "Key")
    private String Key;//联系人(本地联系人id与key对应)
    //照片
    @JSONField(name = "Image")
    private String Image; //照片
    //姓名
    @JSONField(name = "FirstName")
    private String FirstName; //名称
    @JSONField(name = "Prefix")
    private String Prefix;//前缀
    @JSONField(name = "LastName")
    private String LastName; //姓
    @JSONField(name = "Sufix")
    private String Sufix;//后缀
    @JSONField(name = "MiddleName")
    private String MiddleName;
    //组织
    @JSONField(name = "JobTitle")
    private String JobTitle;//职位
    @JSONField(name = "CompanyName")
    private String CompanyName; //公司名
    @JSONField(name = "Department")
    private String Department; //部门
    //电话列表
    @JSONField(name = "PhoneNumberList")
    private List<ContactKeyValueEntity> PhoneNumberList;
    //邮箱列表
    @JSONField(name = "EmailAddressList")
    private List<ContactKeyValueEntity> EmailAddressList;
    //群组[0]
    @JSONField(name = "AddressList")
    private List<ContactAddressEntity> AddressList; //地址
    @JSONField(name = "UrlList")
    private List<ContactKeyValueEntity> UrlList;//网址
    @JSONField(name = "Event")
    private List<ContactKeyValueEntity> Event; //事件日期
    @JSONField(name = "Notes")
    private String Notes;//注释
    @JSONField(name = "ImList")
    private List<ContactKeyValueEntity> ImList;//即时通信账号
    @JSONField(name = "Nickname")
    private String Nickname;//昵称
    //关系
    @JSONField(name = "RelatedNameList")
    private List<ContactKeyValueEntity> RelatedNameList; //关系

    public ContactBean() {
        PhoneNumberList = new ArrayList<>();
        EmailAddressList = new ArrayList<>();
        AddressList = new ArrayList<>();
        UrlList = new ArrayList<>();
        Event = new ArrayList<>();
        ImList = new ArrayList<>();
        RelatedNameList = new ArrayList<>();
    }


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSufix() {
        return Sufix;
    }

    public void setSufix(String sufix) {
        Sufix = sufix;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public List<ContactKeyValueEntity> getPhoneNumberList() {
        return PhoneNumberList;
    }

    public void setPhoneNumberList(List<ContactKeyValueEntity> phoneNumberList) {
        PhoneNumberList = phoneNumberList;
    }

    public List<ContactKeyValueEntity> getEmailAddressList() {
        return EmailAddressList;
    }

    public void setEmailAddressList(List<ContactKeyValueEntity> emailAddressList) {
        EmailAddressList = emailAddressList;
    }

    public List<ContactAddressEntity> getAddressList() {
        return AddressList;
    }

    public void setAddressList(List<ContactAddressEntity> addressList) {
        AddressList = addressList;
    }

    public List<ContactKeyValueEntity> getUrlList() {
        return UrlList;
    }

    public void setUrlList(List<ContactKeyValueEntity> urlList) {
        UrlList = urlList;
    }

    public List<ContactKeyValueEntity> getEvent() {
        return Event;
    }

    public void setEvent(List<ContactKeyValueEntity> event) {
        Event = event;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public List<ContactKeyValueEntity> getImList() {
        return ImList;
    }

    public void setImList(List<ContactKeyValueEntity> imList) {
        ImList = imList;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public List<ContactKeyValueEntity> getRelatedNameList() {
        return RelatedNameList;
    }

    public void setRelatedNameList(List<ContactKeyValueEntity> relatedNameList) {
        RelatedNameList = relatedNameList;
    }

    @Override
    public String toString() {
        return "contactBeans{" +
                "Key='" + Key + '\'' +
                /*", Image='" + Image + '\'' +*/
                ", FirstName='" + FirstName + '\'' +
                ", Prefix='" + Prefix + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Sufix='" + Sufix + '\'' +
                ", MiddleName='" + MiddleName + '\'' +
                ", JobTitle='" + JobTitle + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", Department='" + Department + '\'' +
                ", PhoneNumberList=" + PhoneNumberList +
                ", EmailAddressList=" + EmailAddressList +
                ", AddressList=" + AddressList +
                ", UrlList=" + UrlList +
                ", Event=" + Event +
                ", Notes='" + Notes + '\'' +
                ", ImList=" + ImList +
                ", Nickname='" + Nickname + '\'' +
                ", RelatedNameList=" + RelatedNameList +
                '}';
    }


    public static class ContactAddressEntity {
        @JSONField(name = "Type")
        private String Type;//类型
        @JSONField(name = "Label")
        private String Label;//自定义类型
        @JSONField(name = "Street")
        private String Street;//街道
        @JSONField(name = "City")
        private String City; //城市
        @JSONField(name = "State")
        private String State; //州
        @JSONField(name = "PostalCode")
        private String PostalCode;//邮编
        @JSONField(name = "Country")
        private String Country; //国家

        public ContactAddressEntity() {
        }

        public String getLabel() {
            return Label;
        }

        public void setLabel(String label) {
            Label = label;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getStreet() {
            return Street;
        }

        public void setStreet(String street) {
            Street = street;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getPostalCode() {
            return PostalCode;
        }

        public void setPostalCode(String postalCode) {
            PostalCode = postalCode;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        @Override
        public String toString() {
            return "ContactAddressEntity{" +
                    "Type='" + Type + '\'' +
                    ", Label='" + Label + '\'' +
                    ", Street='" + Street + '\'' +
                    ", City='" + City + '\'' +
                    ", State='" + State + '\'' +
                    ", PostalCode='" + PostalCode + '\'' +
                    ", Country='" + Country + '\'' +
                    '}';
        }
    }

    // TODO: 2018/6/5 添加label字段 
    public static class ContactKeyValueEntity {
        @JSONField(name = "Type")
        private String Type;//类型
        @JSONField(name = "Value")
        private String Value; //号码
        @JSONField(name = "Label")
        private String label;

        public ContactKeyValueEntity() {
        }

        public ContactKeyValueEntity(String type, String value) {
            Type = type;
            Value = value;
        }

        public ContactKeyValueEntity(String type, String value, String label) {
            Type = type;
            Value = value;
            this.label = label;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return "ContactKeyValueEntity{" +
                    "Type='" + Type + '\'' +
                    ", Value='" + Value + '\'' +
                    ", label='" + label + '\'' +
                    '}';
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}

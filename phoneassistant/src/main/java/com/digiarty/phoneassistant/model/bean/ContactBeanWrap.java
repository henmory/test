package com.digiarty.phoneassistant.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/***
 *
 * Created on：2018/6/6
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class ContactBeanWrap {
    @JSONField(name = "ContactBean")
    ContactBean contactBean;
    @JSONField(name = "Image")
    byte[] image;

    public ContactBeanWrap() {
    }

    public ContactBean getContactBean() {
        return contactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ContactBeanWrap{" +
                "contactBean=" + contactBean /*+
                    ", image=" + Arrays.toString(image) +
                    '}'*/;
    }
}


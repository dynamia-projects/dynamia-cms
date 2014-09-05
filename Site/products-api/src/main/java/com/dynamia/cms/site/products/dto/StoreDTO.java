/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.products.dto;

import com.dynamia.tools.domain.util.ContactInfo;
import java.io.Serializable;

/**
 *
 * @author mario_2
 */
public class StoreDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 936740099546344064L;
	private String name;
    private Long externalRef;
    private ContactInfo contactInfo = new ContactInfo();
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(Long externalRef) {
        this.externalRef = externalRef;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

}

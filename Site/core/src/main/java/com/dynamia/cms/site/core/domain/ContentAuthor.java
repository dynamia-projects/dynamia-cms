/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.domain;

import com.dynamia.tools.domain.SimpleEntity;
import com.dynamia.tools.domain.contraints.Email;
import com.dynamia.tools.domain.contraints.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author mario
 */
@Entity
@Table(name = "cr_authors")
public class ContentAuthor extends SimpleEntity {

    @NotEmpty(message = "Enter first name")
    private String firstName;
    @NotEmpty(message = "Enter first name")
    private String lastName;
    @NotEmpty(message = "Enter email address")
    @Email(message = "Enter valid email")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, lastName);
    }

}

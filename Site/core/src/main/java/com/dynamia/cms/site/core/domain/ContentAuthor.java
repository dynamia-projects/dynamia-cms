/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import tools.dynamia.domain.contraints.Email;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_authors")
public class ContentAuthor extends SiteSimpleEntity {

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

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
package tools.dynamia.cms.site.users.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.City;
import tools.dynamia.cms.site.core.domain.ContentAuthor;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.users.api.UserDTO;
import tools.dynamia.cms.site.users.api.UserProfile;
import tools.dynamia.commons.DateTimeUtils;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.domain.BaseEntity;
import tools.dynamia.domain.contraints.Email;
import tools.dynamia.domain.contraints.NotEmpty;
import tools.dynamia.domain.util.ContactInfo;

/**
 * User entity
 *
 * @author Ing. Mario Serrano Leones
 */
@Entity
@Table(name = "usr_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"site_id", "username"})})
public class User extends BaseEntity implements UserDetails, SiteAware {

    @OneToOne
    @NotNull
    private Site site;

    @Column(updatable = false)
    @Size(min = 5, message = "El nombre de usuario debe ser minimo de 5 caracteres")
    @Email(message = "Ingrese direccion valida de email")
    private String username;
    @Size(min = 5, message = "El password del usuario debe ser minimo de 5 caracteres")
    @JsonIgnore
    private String password;

    private ContactInfo contactInfo = new ContactInfo();
    private String userphoto;
    private String identification;
    @JsonIgnore
    private boolean accountNonExpired = true;
    @JsonIgnore
    private boolean accountNonLocked = true;
    @JsonIgnore
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    @JsonIgnore
    private boolean passwordExpired;

    @NotNull(message = "Enter user full name")
    @NotEmpty
    private String fullName;

    private String firstName;
    private String lastName;
    private String groupName;

    @Enumerated(EnumType.ORDINAL)
    private UserProfile profile = UserProfile.USER;

    @OneToOne
    private ContentAuthor relatedAuthor;

    private String externalRef;



    @OneToOne
    @JsonIgnore
    private User relatedUser;

    private BigDecimal paymentCredit;
    private BigDecimal currentPaymentCredit;
    private String code;

    private boolean validated;

    private String validationKey;
    @Temporal(TemporalType.DATE)
    private Date validationDate;
    @Temporal(TemporalType.DATE)
    private Date validationDateLimit;

    @Column(length = 1000)
    private String auxField1;
    @Column(length = 1000)
    private String auxField2;
    @Column(length = 1000)
    private String auxField3;
    @Column(length = 1000)
    private String auxField4;
    @Column(length = 1000)
    private String auxField5;
    @Column(length = 1000)
    private String auxField6;
    @Column(length = 1000)
    private String auxField7;
    @Column(length = 1000)
    private String auxField8;
    @Column(length = 1000)
    private String auxField9;
    @Column(length = 1000)
    private String auxField10;
    @Column(length = 1000)
    private String auxField11;
    @Column(length = 1000)
    private String auxField12;
    @Column(length = 1000)
    private String auxField13;
    @Column(length = 1000)
    private String auxField14;
    @Column(length = 1000)
    private String auxField15;

    public void startEmailValidation() {
        validationDate = null;
        validationDateLimit = DateTimeUtils.dayAfterTomorrow();
        validationKey = StringUtils.randomString();
        validated = false;
        enabled = false;

    }

    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }

    public Date getValidationDateLimit() {
        return validationDateLimit;
    }

    public void setValidationDateLimit(Date validationDateLimit) {
        this.validationDateLimit = validationDateLimit;
    }

    public String getCode() {
        if (code == null || code.isEmpty()) {
            code = getExternalRef();
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPaymentCredit() {
        return paymentCredit;
    }

    public void setPaymentCredit(BigDecimal paymentCredit) {
        this.paymentCredit = paymentCredit;
    }

    public BigDecimal getCurrentPaymentCredit() {
        return currentPaymentCredit;
    }

    public void setCurrentPaymentCredit(BigDecimal currentPaymentCredit) {
        this.currentPaymentCredit = currentPaymentCredit;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(User relatedUser) {
        this.relatedUser = relatedUser;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public ContentAuthor getRelatedAuthor() {
        return relatedAuthor;
    }

    public void setRelatedAuthor(ContentAuthor relatedAuthor) {
        this.relatedAuthor = relatedAuthor;
    }

    public UserProfile getProfile() {
        if (profile == null) {
            profile = UserProfile.USER;
        }
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public String getFirstName() {
        if (firstName == null) {
            firstName = "";
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        getFullName();
    }

    public String getLastName() {
        if (lastName == null) {
            lastName = "";
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        getFullName();
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void setSite(Site site) {
        this.site = site;
    }

    public ContactInfo getContactInfo() {
        if (contactInfo == null) {
            contactInfo = new ContactInfo();
        }
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getFullName() {

        fullName = getFirstName() + " " + getLastName();
        if (fullName != null) {
            fullName = StringUtils.toUpperCase(fullName);
        }

        if (fullName.equals("NULL NULL")) {
            fullName = "";
        }

        return fullName;
    }

    public void setFullName(String fullName) {

        this.fullName = fullName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }

    @Override
    public String toString() {
        if (username == null) {
            return super.toString();
        } else {
            return username;
        }
    }

    /*--------------------------------------------------*/
    @Override
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        auths.add(new SimpleGrantedAuthority("ROLE_" + UserProfile.USER.name()));
        if (getProfile() != UserProfile.USER) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + getProfile().name()));
        }

        return auths;
    }

    public static User createMock() {
        User user = new User();
        user.setUsername("Anonymous");
        user.setFullName("Anonymous");
        return user;
    }

    public void sync(UserDTO dto) {
        if (dto.getProfile() != null) {
            profile = dto.getProfile();
        }

        username = dto.getEmail();
        externalRef = dto.getExternalRef();
        enabled = dto.isEnabled();
        firstName = dto.getFirstName();
        lastName = dto.getLastName();
        fullName = dto.getFullName();

        if (firstName == null) {
            firstName = fullName;
            lastName = "";
        }
        identification = dto.getIdentification();
        groupName = dto.getGroupName();
        if (contactInfo == null) {
            contactInfo = new ContactInfo();
        }
        contactInfo.setAddress(dto.getAddress());
        contactInfo.setEmail(dto.getEmail());
        contactInfo.setPhoneNumber(dto.getPhoneNumber());
        contactInfo.setMobileNumber(dto.getMobileNumber());
        contactInfo.setCity(dto.getCity());
        contactInfo.setCountry(dto.getCountry());
    }

    public String getAuxField1() {
        return auxField1;
    }

    public void setAuxField1(String auxField1) {
        this.auxField1 = auxField1;
    }

    public String getAuxField2() {
        return auxField2;
    }

    public void setAuxField2(String auxField2) {
        this.auxField2 = auxField2;
    }

    public String getAuxField3() {
        return auxField3;
    }

    public void setAuxField3(String auxField3) {
        this.auxField3 = auxField3;
    }

    public String getAuxField4() {
        return auxField4;
    }

    public void setAuxField4(String auxField4) {
        this.auxField4 = auxField4;
    }

    public String getAuxField5() {
        return auxField5;
    }

    public void setAuxField5(String auxField5) {
        this.auxField5 = auxField5;
    }

    public String getAuxField6() {
        return auxField6;
    }

    public void setAuxField6(String auxField6) {
        this.auxField6 = auxField6;
    }

    public String getAuxField7() {
        return auxField7;
    }

    public void setAuxField7(String auxField7) {
        this.auxField7 = auxField7;
    }

    public String getAuxField8() {
        return auxField8;
    }

    public void setAuxField8(String auxField8) {
        this.auxField8 = auxField8;
    }

    public String getAuxField9() {
        return auxField9;
    }

    public void setAuxField9(String auxField9) {
        this.auxField9 = auxField9;
    }

    public String getAuxField10() {
        return auxField10;
    }

    public void setAuxField10(String auxField10) {
        this.auxField10 = auxField10;
    }

    public String getAuxField11() {
        return auxField11;
    }

    public void setAuxField11(String auxField11) {
        this.auxField11 = auxField11;
    }

    public String getAuxField12() {
        return auxField12;
    }

    public void setAuxField12(String auxField12) {
        this.auxField12 = auxField12;
    }

    public String getAuxField13() {
        return auxField13;
    }

    public void setAuxField13(String auxField13) {
        this.auxField13 = auxField13;
    }

    public String getAuxField14() {
        return auxField14;
    }

    public void setAuxField14(String auxField14) {
        this.auxField14 = auxField14;
    }

    public String getAuxField15() {
        return auxField15;
    }

    public void setAuxField15(String auxField15) {
        this.auxField15 = auxField15;
    }

}

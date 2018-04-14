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
package tools.dynamia.cms.site.users.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.ContentAuthor
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.users.api.UserDTO
import tools.dynamia.cms.site.users.api.UserProfile
import tools.dynamia.commons.DateTimeUtils
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.BaseEntity
import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * User entity
 *
 * @author Ing. Mario Serrano Leones
 */
@Entity
@Table(name = "usr_users", uniqueConstraints = [@UniqueConstraint(columnNames = ["site_id", "username"])])
class User extends BaseEntity implements UserDetails, SiteAware {

    @OneToOne
    @NotNull
    private Site site

    @Column(updatable = false)
    @Size(min = 5, message = "El nombre de usuario debe ser minimo de 5 caracteres")
    @Email(message = "Ingrese direccion valida de email")
    private String username
    @Size(min = 5, message = "El password del usuario debe ser minimo de 5 caracteres")
    @JsonIgnore
    private String password

    private ContactInfo contactInfo = new ContactInfo()
    private String userphoto
    private String identification
    @JsonIgnore
    private boolean accountNonExpired = true
    @JsonIgnore
    private boolean accountNonLocked = true
    @JsonIgnore
    private boolean credentialsNonExpired = true
    private boolean enabled = true
    @JsonIgnore
    private boolean passwordExpired

    @NotNull(message = "Enter user full name")
    @NotEmpty
    private String fullName

    private String firstName
    private String lastName
    private String groupName

    @Enumerated(EnumType.ORDINAL)
    private UserProfile profile = UserProfile.USER

    @OneToOne
    private ContentAuthor relatedAuthor

    private String externalRef


    @OneToOne
    @JsonIgnore
    private User relatedUser

    private BigDecimal paymentCredit
    private BigDecimal currentPaymentCredit
    private String code

    private boolean validated

    private String validationKey
    @Temporal(TemporalType.DATE)
    private Date validationDate
    @Temporal(TemporalType.DATE)
    private Date validationDateLimit

    @Column(length = 1000)
    private String auxField1
    @Column(length = 1000)
    private String auxField2
    @Column(length = 1000)
    private String auxField3
    @Column(length = 1000)
    private String auxField4
    @Column(length = 1000)
    private String auxField5
    @Column(length = 1000)
    private String auxField6
    @Column(length = 1000)
    private String auxField7
    @Column(length = 1000)
    private String auxField8
    @Column(length = 1000)
    private String auxField9
    @Column(length = 1000)
    private String auxField10
    @Column(length = 1000)
    private String auxField11
    @Column(length = 1000)
    private String auxField12
    @Column(length = 1000)
    private String auxField13
    @Column(length = 1000)
    private String auxField14
    @Column(length = 1000)
    private String auxField15

    void startEmailValidation() {
        validationDate = null
        validationDateLimit = DateTimeUtils.dayAfterTomorrow()
        validationKey = StringUtils.randomString()
        validated = false
        enabled = false

    }

    Date getValidationDate() {
        return validationDate
    }

    void setValidationDate(Date validationDate) {
        this.validationDate = validationDate
    }

    boolean isValidated() {
        return validated
    }

    void setValidated(boolean validated) {
        this.validated = validated
    }

    String getValidationKey() {
        return validationKey
    }

    void setValidationKey(String validationKey) {
        this.validationKey = validationKey
    }

    Date getValidationDateLimit() {
        return validationDateLimit
    }

    void setValidationDateLimit(Date validationDateLimit) {
        this.validationDateLimit = validationDateLimit
    }

    String getCode() {
        if (code == null || code.empty) {
            code = externalRef
        }
        return code
    }

    void setCode(String code) {
        this.code = code
    }

    BigDecimal getPaymentCredit() {
        return paymentCredit
    }

    void setPaymentCredit(BigDecimal paymentCredit) {
        this.paymentCredit = paymentCredit
    }

    BigDecimal getCurrentPaymentCredit() {
        return currentPaymentCredit
    }

    void setCurrentPaymentCredit(BigDecimal currentPaymentCredit) {
        this.currentPaymentCredit = currentPaymentCredit
    }

    String getGroupName() {
        return groupName
    }

    void setGroupName(String groupName) {
        this.groupName = groupName
    }

    User getRelatedUser() {
        return relatedUser
    }

    void setRelatedUser(User relatedUser) {
        this.relatedUser = relatedUser
    }

    String getExternalRef() {
        return externalRef
    }

    void setExternalRef(String externalRef) {
        this.externalRef = externalRef
    }

    ContentAuthor getRelatedAuthor() {
        return relatedAuthor
    }

    void setRelatedAuthor(ContentAuthor relatedAuthor) {
        this.relatedAuthor = relatedAuthor
    }

    UserProfile getProfile() {
        if (profile == null) {
            profile = UserProfile.USER
        }
        return profile
    }

    void setProfile(UserProfile profile) {
        this.profile = profile
    }

    String getFirstName() {
        if (firstName == null) {
            firstName = ""
        }
        return firstName
    }

    void setFirstName(String firstName) {
        this.firstName = firstName
        fullName
    }

    String getLastName() {
        if (lastName == null) {
            lastName = ""
        }
        return lastName
    }

    void setLastName(String lastName) {
        this.lastName = lastName
        fullName
    }

    String getUserphoto() {
        return userphoto
    }

    void setUserphoto(String userphoto) {
        this.userphoto = userphoto
    }

    String getIdentification() {
        return identification
    }

    void setIdentification(String identification) {
        this.identification = identification
    }

    @Override
    Site getSite() {
        return site
    }

    @Override
    void setSite(Site site) {
        this.site = site
    }

    ContactInfo getContactInfo() {
        if (contactInfo == null) {
            contactInfo = new ContactInfo()
        }
        return contactInfo
    }

    void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo
    }

    String getFullName() {

        fullName = firstName + " " + lastName
        if (fullName != null) {
            fullName = StringUtils.toUpperCase(fullName)
        }

        if (fullName.equals("NULL NULL")) {
            fullName = ""
        }

        return fullName
    }

    void setFullName(String fullName) {

        this.fullName = fullName
    }

    @Override
    String getPassword() {
        return password
    }

    void setPassword(String password) {
        this.password = password
    }

    @Override
    String getUsername() {
        return username
    }

    void setUsername(String username) {
        this.username = username
    }

    @Override
    boolean isAccountNonExpired() {
        return accountNonExpired
    }

    void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired
    }

    @Override
    boolean isAccountNonLocked() {
        return accountNonLocked
    }

    void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked
    }

    @Override
    boolean isCredentialsNonExpired() {
        return credentialsNonExpired
    }

    void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired
    }

    @Override
    boolean isEnabled() {
        return enabled
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    boolean isPasswordExpired() {
        return passwordExpired
    }

    void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired
    }

    @Override
    String toString() {
        if (username == null) {
            return super.toString()
        } else {
            return username
        }
    }

    /*--------------------------------------------------*/

    @Override
    @JsonIgnore
    List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>()

        auths.add(new SimpleGrantedAuthority("ROLE_" + UserProfile.USER.name()))
        if (profile != UserProfile.USER) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + profile.name()))
        }

        return auths
    }

    static User createMock() {
        User user = new User()
        user.username = "Anonymous"
        user.fullName = "Anonymous"
        return user
    }

    void sync(UserDTO dto) {
        if (dto.profile != null) {
            profile = dto.profile
        }

        username = dto.email
        externalRef = dto.externalRef
        enabled = dto.enabled
        firstName = dto.firstName
        lastName = dto.lastName
        fullName = dto.fullName

        if (firstName == null) {
            firstName = fullName
            lastName = ""
        }
        identification = dto.identification
        groupName = dto.groupName
        if (contactInfo == null) {
            contactInfo = new ContactInfo()
        }
        contactInfo.address = dto.address
        contactInfo.email = dto.email
        contactInfo.phoneNumber = dto.phoneNumber
        contactInfo.mobileNumber = dto.mobileNumber
        contactInfo.city = dto.city
        contactInfo.country = dto.country
    }

    String getAuxField1() {
        return auxField1
    }

    void setAuxField1(String auxField1) {
        this.auxField1 = auxField1
    }

    String getAuxField2() {
        return auxField2
    }

    void setAuxField2(String auxField2) {
        this.auxField2 = auxField2
    }

    String getAuxField3() {
        return auxField3
    }

    void setAuxField3(String auxField3) {
        this.auxField3 = auxField3
    }

    String getAuxField4() {
        return auxField4
    }

    void setAuxField4(String auxField4) {
        this.auxField4 = auxField4
    }

    String getAuxField5() {
        return auxField5
    }

    void setAuxField5(String auxField5) {
        this.auxField5 = auxField5
    }

    String getAuxField6() {
        return auxField6
    }

    void setAuxField6(String auxField6) {
        this.auxField6 = auxField6
    }

    String getAuxField7() {
        return auxField7
    }

    void setAuxField7(String auxField7) {
        this.auxField7 = auxField7
    }

    String getAuxField8() {
        return auxField8
    }

    void setAuxField8(String auxField8) {
        this.auxField8 = auxField8
    }

    String getAuxField9() {
        return auxField9
    }

    void setAuxField9(String auxField9) {
        this.auxField9 = auxField9
    }

    String getAuxField10() {
        return auxField10
    }

    void setAuxField10(String auxField10) {
        this.auxField10 = auxField10
    }

    String getAuxField11() {
        return auxField11
    }

    void setAuxField11(String auxField11) {
        this.auxField11 = auxField11
    }

    String getAuxField12() {
        return auxField12
    }

    void setAuxField12(String auxField12) {
        this.auxField12 = auxField12
    }

    String getAuxField13() {
        return auxField13
    }

    void setAuxField13(String auxField13) {
        this.auxField13 = auxField13
    }

    String getAuxField14() {
        return auxField14
    }

    void setAuxField14(String auxField14) {
        this.auxField14 = auxField14
    }

    String getAuxField15() {
        return auxField15
    }

    void setAuxField15(String auxField15) {
        this.auxField15 = auxField15
    }

}

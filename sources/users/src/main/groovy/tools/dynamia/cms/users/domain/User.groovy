/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.users.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.ContentAuthor
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.api.UserProfile
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
    Site site

    @Column(updatable = false)
    @Size(min = 5, message = "El nombre de usuario debe ser minimo de 5 caracteres")
    @Email(message = "Ingrese direccion valida de email")
    String username
    @Size(min = 5, message = "El password del usuario debe ser minimo de 5 caracteres")
    @JsonIgnore
    String password

    ContactInfo contactInfo = new ContactInfo()
    String userphoto
    String identification
    @JsonIgnore
    boolean accountNonExpired = true
    @JsonIgnore
    boolean accountNonLocked = true
    @JsonIgnore
    boolean credentialsNonExpired = true
    boolean enabled = true
    @JsonIgnore
    boolean passwordExpired

    @NotNull(message = "Enter user full name")
    @NotEmpty
    String fullName

    String firstName
    String lastName
    String groupName

    @Enumerated(EnumType.ORDINAL)
    UserProfile profile = UserProfile.USER

    @OneToOne
    ContentAuthor relatedAuthor

    String externalRef
    String website


    @OneToOne
    @JsonIgnore
    User relatedUser

    BigDecimal paymentCredit
    BigDecimal currentPaymentCredit
    String code

    boolean validated

    String validationKey
    @Temporal(TemporalType.DATE)
    Date validationDate
    @Temporal(TemporalType.DATE)
    Date validationDateLimit

    @Column(length = 1000)
    String auxField1
    @Column(length = 1000)
    String auxField2
    @Column(length = 1000)
    String auxField3
    @Column(length = 1000)
    String auxField4
    @Column(length = 1000)
    String auxField5
    @Column(length = 1000)
    String auxField6
    @Column(length = 1000)
    String auxField7
    @Column(length = 1000)
    String auxField8
    @Column(length = 1000)
    String auxField9
    @Column(length = 1000)
    String auxField10
    @Column(length = 1000)
    String auxField11
    @Column(length = 1000)
    String auxField12
    @Column(length = 1000)
    String auxField13
    @Column(length = 1000)
    String auxField14
    @Column(length = 1000)
    String auxField15

    void startEmailValidation() {
        validationDate = null
        validationDateLimit = DateTimeUtils.dayAfterTomorrow()
        validationKey = StringUtils.randomString()
        validated = false
        enabled = false

    }


    String getCode() {
        if (code == null || code.empty) {
            code = externalRef
        }
        return code
    }


    UserProfile getProfile() {
        if (profile == null) {
            profile = UserProfile.USER
        }
        return profile
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


    ContactInfo getContactInfo() {
        if (contactInfo == null) {
            contactInfo = new ContactInfo()
        }
        return contactInfo
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

}

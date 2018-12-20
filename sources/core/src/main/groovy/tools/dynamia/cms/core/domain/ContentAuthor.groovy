/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
package tools.dynamia.cms.core.domain

import tools.dynamia.domain.contraints.Email
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "cr_authors")
class ContentAuthor extends SiteSimpleEntity {

    @NotEmpty(message = "Enter first name")
    private String firstName
    @NotEmpty(message = "Enter first name")
    private String lastName
    @NotEmpty(message = "Enter email address")
    @Email(message = "Enter valid email")
    private String email
    private String twitter
    private String googlePlus
    private String facebook
    private String instagram
    private String linkedin
    private String tumblr
    private String snapchat
    private String pinterest
    private String vk
    private String flickr
    private String vine
    private String meetup
    private String website

    @Column(length = 1000)
    private String bio

    String getFirstName() {
        return firstName
    }

    void setFirstName(String firstName) {
        this.firstName = firstName
    }

    String getLastName() {
        return lastName
    }

    void setLastName(String lastName) {
        this.lastName = lastName
    }

    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }

    String getTwitter() {
        return twitter
    }

    void setTwitter(String twitter) {
        this.twitter = twitter
    }

    String getGooglePlus() {
        return googlePlus
    }

    void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus
    }

    String getFacebook() {
        return facebook
    }

    void setFacebook(String facebook) {
        this.facebook = facebook
    }

    String getInstagram() {
        return instagram
    }

    void setInstagram(String instagram) {
        this.instagram = instagram
    }

    String getLinkedin() {
        return linkedin
    }

    void setLinkedin(String linkedin) {
        this.linkedin = linkedin
    }

    String getTumblr() {
        return tumblr
    }

    void setTumblr(String tumblr) {
        this.tumblr = tumblr
    }

    String getSnapchat() {
        return snapchat
    }

    void setSnapchat(String snapchat) {
        this.snapchat = snapchat
    }

    String getPinterest() {
        return pinterest
    }

    void setPinterest(String pinterest) {
        this.pinterest = pinterest
    }

    String getVk() {
        return vk
    }

    void setVk(String vk) {
        this.vk = vk
    }

    String getFlickr() {
        return flickr
    }

    void setFlickr(String flickr) {
        this.flickr = flickr
    }

    String getVine() {
        return vine
    }

    void setVine(String vine) {
        this.vine = vine
    }

    String getMeetup() {
        return meetup
    }

    void setMeetup(String meetup) {
        this.meetup = meetup
    }

    String getWebsite() {
        return website
    }

    void setWebsite(String website) {
        this.website = website
    }

    String getBio() {
        return bio
    }

    void setBio(String bio) {
        this.bio = bio
    }

    @Override
    String toString() {
        return String.format("%s %s", firstName, lastName)
    }

}

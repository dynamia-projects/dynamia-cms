/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.users.services;

import java.util.List;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.users.UserForm;
import com.dynamia.cms.site.users.domain.User;
import com.dynamia.cms.site.users.domain.UserContactInfo;

/**
 *
 * @author mario_2
 */
public interface UserService {

    public void saveUser(UserForm user);

    public User getUser(Site site, String name);

    public void changePassword(UserForm form);

    void checkAdminUser(Site site);

	public List<UserContactInfo> getContactInfos(User current);

}

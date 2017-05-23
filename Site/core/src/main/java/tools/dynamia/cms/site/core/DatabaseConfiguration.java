/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tools.dynamia.app.ApplicationInfo;
import tools.dynamia.app.JPAConfigurationAdapter;

/**
 *
 * @author mario
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration extends JPAConfigurationAdapter {

    @Autowired
    private ApplicationInfo appInfo;

    @Override
    public String jndiName() {
        return appInfo.getJndiName();
    }

    @Override
    protected void configureJpaVendorAdapter(HibernateJpaVendorAdapter va) {
        super.configureJpaVendorAdapter(va);

        va.setShowSql(false);
    }

}

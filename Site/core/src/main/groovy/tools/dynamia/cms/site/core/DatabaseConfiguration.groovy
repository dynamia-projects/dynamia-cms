/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import tools.dynamia.app.ApplicationInfo
import tools.dynamia.app.JPAConfigurationAdapter

/**
 *
 * @author mario
 */
@Configuration
@EnableTransactionManagement
class DatabaseConfiguration extends JPAConfigurationAdapter {

    @Autowired
    private ApplicationInfo appInfo

    @Override
    String jndiName() {
        return appInfo.jndiName
    }

    @Override
    protected void configureJpaVendorAdapter(HibernateJpaVendorAdapter va) {
        super.configureJpaVendorAdapter(va)
        va.jpaPropertyMap.put("hibernate.id.new_generator_mappings", false)
        va.showSql = false
    }

}

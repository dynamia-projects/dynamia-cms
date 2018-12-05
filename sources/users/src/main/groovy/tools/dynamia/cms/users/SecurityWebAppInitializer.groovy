/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.users

import org.springframework.web.WebApplicationInitializer
import org.springframework.web.filter.DelegatingFilterProxy

import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletException

class SecurityWebAppInitializer implements WebApplicationInitializer {

    @Override
    void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration filter = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
        filter.addMappingForUrlPatterns(null, false, "/*")
    }

}

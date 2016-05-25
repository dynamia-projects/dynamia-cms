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
package tools.dynamia.cms.site.pages;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import tools.dynamia.cms.site.core.DynamiaCMSException;

/**
 *
 * @author Mario Serrano Leones
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "page not found in site")
public class PageNotFoundException extends DynamiaCMSException {

    private String pageAlias;
    private String siteKey;

    public PageNotFoundException() {
    }

    public PageNotFoundException(String pageAlias, String siteKey) {
        this.pageAlias = pageAlias;
        this.siteKey = siteKey;
    }

    public PageNotFoundException(String pageAlias, String siteKey, String message) {
        super(message);
        this.pageAlias = pageAlias;
        this.siteKey = siteKey;
    }

    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotFoundException(Throwable cause) {
        super(cause);
    }

    public PageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getPageAlias() {
        return pageAlias;
    }

    public String getSiteKey() {
        return siteKey;
    }

}

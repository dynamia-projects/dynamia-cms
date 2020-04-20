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
package tools.dynamia.cms.pages

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import tools.dynamia.cms.core.CMSException

/**
 *
 * @author Mario Serrano Leones
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "page not found in site")
class PageNotFoundException extends CMSException {

    private String pageAlias
    private String siteKey

    PageNotFoundException() {
    }

    PageNotFoundException(String pageAlias, String siteKey) {
        this.pageAlias = pageAlias
        this.siteKey = siteKey
    }

    PageNotFoundException(String pageAlias, String siteKey, String message) {
        super(message)
        this.pageAlias = pageAlias
        this.siteKey = siteKey
    }

    PageNotFoundException(String message) {
        super(message)
    }

    PageNotFoundException(String message, Throwable cause) {
        super(message, cause)
    }

    PageNotFoundException(Throwable cause) {
        super(cause)
    }

    PageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
    }

    String getPageAlias() {
        return pageAlias
    }

    String getSiteKey() {
        return siteKey
    }

    @Override
    HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND
    }

}

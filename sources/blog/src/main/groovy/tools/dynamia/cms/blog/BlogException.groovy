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

package tools.dynamia.cms.blog

import org.springframework.http.HttpStatus
import tools.dynamia.cms.core.CMSException

/**
 * Exception for blog module
 */
class BlogException extends CMSException {

    BlogException() {
    }

    BlogException(String message) {
        super(message)
    }

    BlogException(String message, Throwable cause) {
        super(message, cause)
    }

    BlogException(Throwable cause) {
        super(cause)
    }

    BlogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
    }

    BlogException(HttpStatus status) {
        super(status)
    }

    BlogException(HttpStatus status, String message) {
        super(status, message)
    }

    BlogException(HttpStatus status, String message, Throwable cause) {
        super(status, message, cause)
    }

    BlogException(HttpStatus status, Throwable cause) {
        super(status, cause)
    }
}
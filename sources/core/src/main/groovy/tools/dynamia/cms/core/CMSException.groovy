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
package tools.dynamia.cms.core

import org.springframework.http.HttpStatus

/**
 *
 * @author Mario Serrano Leones
 */
class CMSException extends RuntimeException {

    private HttpStatus status

    CMSException() {
    }

    CMSException(String message) {
        super(message)
    }

    CMSException(String message, Throwable cause) {
        super(message, cause)
    }

    CMSException(Throwable cause) {
        super(cause)
    }

    CMSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace)
    }

    CMSException(HttpStatus status) {
        this.status = status
    }

    CMSException(HttpStatus status, String message) {
        super(message)
        this.status = status
    }

    CMSException(HttpStatus status, String message, Throwable cause) {
        super(message, cause)
        this.status = status
    }

    CMSException(HttpStatus status, Throwable cause) {
        super(cause)
        this.status = status
    }

    HttpStatus getStatus() {
        return status
    }

}

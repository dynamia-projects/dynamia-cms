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
package tools.dynamia.cms.site.core;

import org.springframework.http.HttpStatus;

/**
 *
 * @author Mario Serrano Leones
 */
public class CMSException extends RuntimeException {

    private HttpStatus status;

    public CMSException() {
    }

    public CMSException(String message) {
        super(message);
    }

    public CMSException(String message, Throwable cause) {
        super(message, cause);
    }

    public CMSException(Throwable cause) {
        super(cause);
    }

    public CMSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CMSException(HttpStatus status) {
        this.status = status;
    }

    public CMSException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CMSException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public CMSException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}

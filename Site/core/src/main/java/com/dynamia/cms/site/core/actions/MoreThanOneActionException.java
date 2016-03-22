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
package com.dynamia.cms.site.core.actions;

import com.dynamia.cms.site.core.DynamiaCMSException;

/**
 *
 * @author Mario Serrano Leones
 */
class MoreThanOneActionException extends DynamiaCMSException {

    private String actionName;

    public MoreThanOneActionException(String actionName) {
        this.actionName = actionName;

    }

    public MoreThanOneActionException(String actionName, String message) {
        super(message);
        this.actionName = actionName;
    }

    public MoreThanOneActionException(String actionName, String message, Throwable cause) {
        super(message, cause);
        this.actionName = actionName;
    }

    public MoreThanOneActionException(String actionName, Throwable cause) {
        super(cause);
        this.actionName = actionName;
    }

    public MoreThanOneActionException(String actionName, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

}

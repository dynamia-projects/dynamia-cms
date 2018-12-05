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

package tools.dynamia.cms.mail.services.impl

import org.springframework.mail.MailSender

/**
 * Created by mario on 13/07/2017.
 */
class MailSenderHolder {

    private long timestamp
    private MailSender sender

    MailSenderHolder(long timestamp, MailSender sender) {
        this.timestamp = timestamp
        this.sender = sender
    }

    MailSender getSender() {
        return sender
    }

    long getTimestamp() {
        return timestamp
    }

    boolean isOld(long newtimestamp) {
        return newtimestamp > timestamp
    }

}

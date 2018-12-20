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
package tools.dynamia.cms.mail.services

import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.domain.MailingContact

/**
 *
 * @author ronald
 */
interface MailService {

	void send(MailMessage message)

    void send(String to, String subject, String content)

    void setPreferredEmailAccount(MailAccount account)

    MailAccount getPreferredEmailAccount(Site site)

    boolean existsMailingContact(MailingContact contact)

    void sendAsync(MailMessage mailMessage)

    void sendAsync(String to, String subject, String content)

}

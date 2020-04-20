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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.mail.admin.actions

import org.zkoss.zul.Messagebox
import tools.dynamia.actions.AbstractAction
import tools.dynamia.actions.ActionEvent
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.mail.MailMessage
import tools.dynamia.cms.mail.MailServiceListener
import tools.dynamia.cms.mail.domain.MailAccount
import tools.dynamia.cms.mail.services.MailService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.integration.Containers
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages
import tools.dynamia.viewers.ViewDescriptor
import tools.dynamia.zk.actions.ActionToolbar
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer
import tools.dynamia.zk.util.ZKUtil
import tools.dynamia.zk.viewers.ui.Viewer

import static tools.dynamia.viewers.ViewDescriptorBuilder.field
import static tools.dynamia.viewers.ViewDescriptorBuilder.viewDescriptor

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
class TestEmailAccountAction extends AbstractCrudAction implements MailServiceListener {

    TestEmailAccountAction() {
        name = "Test Account"
        description = "Send a test message using this email account"
        image = "mail"
        menuSupported = true

    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        MailAccount cuenta = (MailAccount) evt.data
        if (cuenta != null) {
            Viewer viewer = createView(cuenta)
            ZKUtil.showDialog("Test Message: " + cuenta.name, viewer, "60%", "")
        } else {
            UIMessages.showMessage("Select account to test", MessageType.WARNING)
        }
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MailAccount.class)
    }

    private Viewer createView(MailAccount account) {
        ViewDescriptor descriptor = viewDescriptor("form", MailMessage.class, false)
                .fields(field("to"), field("subject"), field("content").params("multiline", true, "height", "200px")).layout("columns", 1)
                .build()

        final Viewer viewer = new Viewer(descriptor)
        MailMessage msg = new MailMessage()
        msg.mailAccount = account
        viewer.value = msg

        ActionToolbar toolbar = new ActionToolbar({ Object src, Map<String, Object> parms1 -> new ActionEvent(viewer.value, src) })
        toolbar.addAction(new SendTestEmailAction())
        viewer.toolbar = toolbar

        return viewer

    }

    @Override
    void onMailSending(MailMessage message) {
        UIMessages.showMessage("Sending " + message, MessageType.WARNING)
    }

    @Override
    void onMailSended(MailMessage message) {
        UIMessages.showMessage("Test OK: Mail sended successfull " + message)
    }

    @Override
    void onMailSendFail(MailMessage message, Throwable cause) {
        Messagebox.show("Error performing test: " + cause.message, "Error", Messagebox.OK, Messagebox.ERROR)
    }

    private static class SendTestEmailAction extends AbstractAction {

        SendTestEmailAction() {
            name = "Send Test"
            image = "mail"
        }

        @Override
        void actionPerformed(ActionEvent evt) {
            MailService service = Containers.get().findObject(MailService.class)
            MailMessage msg = (MailMessage) evt.data
            if (msg != null) {
                if (msg.to != null && !msg.to.empty) {
                    try {
                        service.send(msg)
                        UIMessages.showMessage("The email test started...")
                    } catch (Exception e) {
                        Messagebox.show("Error sending test: " + e.message)
                    }
                } else {
                    UIMessages.showMessage("Enter [to] address", MessageType.ERROR)
                }
            }
        }

        @Override
        ActionRenderer getRenderer() {
            return new ToolbarbuttonActionRenderer(true)
        }

    }

}

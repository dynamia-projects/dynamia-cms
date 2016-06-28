/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.mail.actions;

import java.util.Map;

import org.zkoss.zul.Messagebox;

import tools.dynamia.cms.site.mail.MailMessage;
import tools.dynamia.cms.site.mail.domain.MailAccount;
import tools.dynamia.cms.site.mail.services.MailService;

import tools.dynamia.actions.AbstractAction;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.cms.site.mail.MailServiceListener;
import tools.dynamia.commons.ApplicableClass;
import tools.dynamia.crud.AbstractCrudAction;
import tools.dynamia.crud.CrudActionEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.integration.Containers;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.viewers.ViewDescriptor;
import static tools.dynamia.viewers.ViewDescriptorBuilder.*;
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ui.Viewer;

/**
 *
 * @author Mario Serrano Leones
 */
@InstallAction
public class TestEmailAccountAction extends AbstractCrudAction implements MailServiceListener {

    public TestEmailAccountAction() {
        setName("Test Account");
        setDescription("Send a test message using this email account");
        setImage("mail");
        setMenuSupported(true);

    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {
        MailAccount cuenta = (MailAccount) evt.getData();
        if (cuenta != null) {
            Viewer viewer = createView(cuenta);
            ZKUtil.showDialog("Test Message: " + cuenta.getName(), viewer, "60%", "");
        } else {
            UIMessages.showMessage("Select account to test", MessageType.WARNING);
        }
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(MailAccount.class);
    }

    private Viewer createView(MailAccount account) {
        ViewDescriptor descriptor = viewDescriptor("form", MailMessage.class, false)
                .fields(field("to"), field("subject"), field("content").params("multiline", true, "height", "200px")).layout("columns", 1)
                .build();

        final Viewer viewer = new Viewer(descriptor);
        MailMessage msg = new MailMessage();
        msg.setMailAccount(account);
        viewer.setValue(msg);

        ActionToolbar toolbar = new ActionToolbar((Object src, Map<String, Object> parms1) -> new ActionEvent(viewer.getValue(), src));
        toolbar.addAction(new SendTestEmailAction());
        viewer.setToolbar(toolbar);

        return viewer;

    }

    @Override
    public void onMailSending(MailMessage message) {
        UIMessages.showMessage("Sending " + message, MessageType.WARNING);
    }

    @Override
    public void onMailSended(MailMessage message) {
        UIMessages.showMessage("Test OK: Mail sended successfull " + message);
    }

    @Override
    public void onMailSendFail(MailMessage message, Throwable cause) {
        Messagebox.show("Error performing test: " + cause.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
    }

    private static class SendTestEmailAction extends AbstractAction {

        public SendTestEmailAction() {
            setName("Send Test");
            setImage("mail");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            MailService service = Containers.get().findObject(MailService.class);
            MailMessage msg = (MailMessage) evt.getData();
            if (msg != null) {
                if (msg.getTo() != null && !msg.getTo().isEmpty()) {
                    try {
                        service.send(msg);
                        UIMessages.showMessage("The email test started...");
                    } catch (Exception e) {
                        Messagebox.show("Error sending test: " + e.getMessage());
                    }
                } else {
                    UIMessages.showMessage("Enter [to] address", MessageType.ERROR);
                }
            }
        }

        @Override
        public ActionRenderer getRenderer() {
            return new ToolbarbuttonActionRenderer(true);
        }

    }

}

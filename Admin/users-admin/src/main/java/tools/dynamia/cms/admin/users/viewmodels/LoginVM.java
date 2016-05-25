/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.users.viewmodels;

import org.springframework.security.web.WebAttributes;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Form;
import org.zkoss.zhtml.Input;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 *
 * @author Mario Serrano Leones
 */
public class LoginVM {

    private LoggingService logger = new SLF4JLoggingService(LoginVM.class);

    @WireVariable
    private Session session;

    @WireVariable
    private Page page;

    private String username;
    private String password;
    private String message;

    @Init
    public void init() {
        Exception ex = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        if (ex != null) {
            message = ex.getLocalizedMessage();
        }
    }

    @Command
    public void login() {
        try {
            Form form = new Form();
            form.setDynamicProperty("action", "login");
            form.setDynamicProperty("method", "post");
            form.setPage(page);

            //Username
            {
                Input input = new Input();
                input.setParent(form);
                input.setDynamicProperty("type", "hidden");
                input.setDynamicProperty("name", "username");
                input.setValue(username);
            }

            //Password
            {
                Input input = new Input();
                input.setParent(form);
                input.setDynamicProperty("type", "hidden");
                input.setDynamicProperty("name", "password");
                input.setValue(password);
            }

            Clients.submitForm(form);
            //Executions.getCurrent().sendRedirect("j_spring_security_check?j_username=" + username + "&j_password=" + password);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Command
    public void logout() {
        try {
            Executions.getCurrent().sendRedirect("logout");
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

}

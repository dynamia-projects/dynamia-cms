package com.dynamia.cms.admin.core.zk;

import org.zkoss.zk.ui.util.Clients;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.zk.ui.MessageDialog;

public class JQueryGrowlMessageDisplayer extends MessageDialog {

	@Override
	public void showMessage(String message, String title, MessageType type) {

		String method = "notice";
		if (title == null) {
			title = "Message";
			if (type != MessageType.NORMAL) {
				title = StringUtils.capitalize(type.name());
			}
		}

		switch (type) {
		case ERROR:
			method = "error";
			break;
		case WARNING:
			method = "warning";
			break;

		default:
			break;
		}

		String script = "$.growl." + method + "({ title: '" + title + "', message: '" + message + "', size: 'large', location: 'br'  });";
		Clients.evalJavaScript(script);

	}
}

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
package tools.dynamia.cms.admin.core.zk;

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

package tools.dynamia.cms.site.users.ext;

import tools.dynamia.cms.site.users.domain.User;

public interface CustomerChangeListener {

	public void onCustomerChange(User customer);
}

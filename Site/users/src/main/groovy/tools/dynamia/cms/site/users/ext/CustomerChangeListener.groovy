package tools.dynamia.cms.site.users.ext

import tools.dynamia.cms.site.users.domain.User

interface CustomerChangeListener {

	void onCustomerChange(User customer)
}

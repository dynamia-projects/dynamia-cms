package tools.dynamia.cms.users.ext

import tools.dynamia.cms.users.domain.User

interface CustomerChangeListener {

	void onCustomerChange(User customer)
}

package tools.dynamia.cms.users.services

import tools.dynamia.cms.users.domain.UserSiteConfig

interface UserSyncService {

	void syncUsers(UserSiteConfig siteCfg, Map<String, String> params)

}
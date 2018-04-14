package tools.dynamia.cms.site.users.services;

import java.util.Map;

import tools.dynamia.cms.site.users.domain.UserSiteConfig;

public interface UserSyncService {

	void syncUsers(UserSiteConfig siteCfg, Map<String, String> params);

}
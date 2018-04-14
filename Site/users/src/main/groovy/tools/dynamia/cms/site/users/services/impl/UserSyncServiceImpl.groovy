package tools.dynamia.cms.site.users.services.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import tools.dynamia.cms.site.users.api.UserDTO
import tools.dynamia.cms.site.users.api.UsersDatasource
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.cms.site.users.domain.UserSiteConfig
import tools.dynamia.cms.site.users.services.UserService
import tools.dynamia.cms.site.users.services.UserSyncService
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.services.CrudService
import tools.dynamia.web.util.HttpRemotingServiceClient

@Service
class UserSyncServiceImpl implements UserSyncService {

	@Autowired
	private CrudService crudService

    @Autowired
	private UserService service

    /*
     * (non-Javadoc)
     *
     * @see
     * tools.dynamia.cms.site.users.services.impl.UserSyncService#syncUsers(
     * tools.dynamia.cms.site.core.domain.Site, java.util.Map)
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    void syncUsers(UserSiteConfig cfg, Map<String, String> params) {

		if (cfg.isSynchronizationEnabled() && cfg.getDatasourceURL() != null) {

			UsersDatasource datasource = HttpRemotingServiceClient.build(UsersDatasource.class)
					.setServiceURL(cfg.getDatasourceURL())
					.setUsername(cfg.getDatasourceUsername())
					.setPassword(cfg.getDatasourceURL())
					.getProxy()

            List<UserDTO> remoteUsers = datasource.getUsers(params)
            if (remoteUsers != null && !remoteUsers.isEmpty()) {
				sync(cfg, remoteUsers, params)
            }
		}
	}

	private void sync(UserSiteConfig cfg, List<UserDTO> remoteUsers, Map<String, String> params) {
		for (UserDTO remoteUser : remoteUsers) {
			try {
				User localUser = getLocalUser(remoteUser)
                if (localUser == null) {
					localUser = new User()
                    localUser.setSite(cfg.getSite())
                }
				localUser.sync(remoteUser)

                if (localUser.getPassword() == null || localUser.getPassword().isEmpty()) {
					String password = remoteUser.getExternalRef()
                    if (password == null || password.isEmpty()) {
						password = StringUtils.randomString()
                    }
					service.setupPassword(localUser, password)
                }

				if (remoteUser.getRelatedUser() != null && !remoteUser.getRelatedUser().isEmpty()) {
					User relatedUser = crudService.findSingle(User.class, "externalRef", remoteUser.getRelatedUser())
                    localUser.setRelatedUser(relatedUser)
                }

				crudService.save(localUser)
            } catch (Exception e) {
				System.out.println("Error Sync: " + remoteUser.getEmail() + "  - " + e.getMessage())
            }
		}
	}

	private User getLocalUser(UserDTO dto) {

		return crudService.findSingle(User.class, "username", dto.getEmail())

    }
}

package tools.dynamia.cms.site.users.api;

import java.util.List;
import java.util.Map;

public interface UsersDatasource {

	public List<UserDTO> getUsers(Map<String, String> params);
}

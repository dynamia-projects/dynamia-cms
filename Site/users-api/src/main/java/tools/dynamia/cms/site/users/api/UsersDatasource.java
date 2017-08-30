package tools.dynamia.cms.site.users.api;

import java.util.List;
import java.util.Map;

public interface UsersDatasource {

    List<UserDTO> getUsers(Map<String, String> params);

    UserDTO getUser(String identification);
}

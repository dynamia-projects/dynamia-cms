package tools.dynamia.cms.site.users.api

public interface UsersDatasource {

    List<UserDTO> getUsers(Map<String, String> params);

    UserDTO getUser(String identification);
}

package tools.dynamia.cms.users.api

interface UsersDatasource {

    List<UserDTO> getUsers(Map<String, String> params)

    UserDTO getUser(String identification)
}

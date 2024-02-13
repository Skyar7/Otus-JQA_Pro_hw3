package user.create;

import dto.UserDTO;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import services.ServiceAPI;

public class CreateNewUser_Test {
  ServiceAPI userApi = new ServiceAPI();
  @Test
  public void createUser() {
    UserDTO userDTO = UserDTO.builder()
            .id(4069l)
            .userStatus(505l)
            .username("Ivan")
            .email("mail.google.com")
            .build();

    userApi.createUser(userDTO)
            .statusCode(HttpStatus.SC_OK);
  }
}
package api;

import dto.RequestedUserDTO;
import dto.UserDTO;
import dto.UserCreateAndUpdateResponseDTO;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.lessThan;

public class User_Test extends BaseTestsUtils {

  @Test
  public void createAndDeleteUser() {

    /*
     СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ
     Ответ запроса на создание должен удовлетворять условиям:
     - Статус код HTTP-ответа должен быть 2xx.
     - Ответ должен содержать обязательных 3 ключа - code, type и message.
     - Ответ не должен содержать других ключей.
     - Время получения ответа - не более 4 секунд.
     - Ключ code должен содержать значение "200".
     - Ключ type должен содержать значение "unknown".
     Проверка методом GET должна удовлетворять условиям:
     - Статус код HTTP-ответа должен быть 200.
     - Время получения ответа - не более 4 секунд.
     - Тело ответа должно содержать ключи и значения, переданные при создании.

     УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ
     Ответ запроса на создание должен удовлетворять условиям:
     - Статус код HTTP-ответа должен быть .
     - Время получения ответа - не более 4 секунд.
     Проверка методом GET должна удовлетворять условиям:
     - Статус код HTTP-ответа должен быть .
     - Время получения ответа - не более 4 секунд.
    */

    // СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ
    UserDTO userDTO = UserDTO.builder()
            .id(7l)
            .username("Agent")
            .firstName("James")
            .lastName("Bond")
            .email("pierce_brosnan@gmail.com")
            .phone("5553817")
            .build();

    ValidatableResponse response = userApi.createUser(userDTO);
    response
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/UserCreateResponseSchema.json"))
            .time(lessThan(4000l));

    UserCreateAndUpdateResponseDTO createResponse = response.extract().body().as(UserCreateAndUpdateResponseDTO.class);

    Assertions.assertAll("Проверка ответа создания пользователя",
            () -> Assertions.assertEquals(200, createResponse.getCode(), "Неверный код!"),
            () -> Assertions.assertEquals("unknown", createResponse.getType(), "Неверный тип!")
    );

    //Проверка
    response = userApi.readUser(userDTO.getUsername());
    response
            .statusCode(HttpStatus.SC_OK)
            .time(lessThan(4000l));

    RequestedUserDTO createdUser = response.extract().body().as(RequestedUserDTO.class);

    Assertions.assertAll("Проверка ответа по запросу созданного пользователя",
            () -> Assertions.assertEquals(userDTO.getId(), createdUser.getId(), "ID созданного пользователя не совпадает!"),
            () -> Assertions.assertEquals(userDTO.getUsername(), createdUser.getUsername(), "Юзернейм созданного пользователя не совпадает!"),
            () -> Assertions.assertEquals(userDTO.getFirstName(), createdUser.getFirstName(), "Имя созданного пользователя не совпадает!"),
            () -> Assertions.assertEquals(userDTO.getLastName(), createdUser.getLastName(), "Фамилия созданного пользователя не совпадает!"),
            () -> Assertions.assertEquals(userDTO.getEmail(), createdUser.getEmail(), "E-mail созданного пользователя не совпадает!"),
            () -> Assertions.assertEquals(userDTO.getPhone(), createdUser.getPhone(), "Телефон созданного пользователя не совпадает!")
    );


    // УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ


  }
}
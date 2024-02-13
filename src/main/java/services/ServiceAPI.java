package services;

import dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ServiceAPI {
  private static final String BASE_URL = "https://petstore.swagger.io/v2";
  private static final String BASE_PATH = "/user";
  private RequestSpecification spec;

  public ServiceAPI() {
    spec = given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all();
  }
  public ValidatableResponse createUser(UserDTO user) {
    return given(spec)
            .basePath(BASE_PATH)
            .body(user)
            .when()
            .post()
            .then()
            .log().all();
  }
}

package services;

import static io.restassured.RestAssured.given;

import dto.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class ServiceAPI {
  private static final String BASE_URL = System.getProperty("base.url");
  private static final String USER_PATH = "/user";
  private RequestSpecification spec;

  public ServiceAPI() {
    spec = given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .log().all();
  }

  public ValidatableResponse createUser(UserDTO user) {
    return given(spec)
            .basePath(USER_PATH)
            .body(user)
            .when()
            .post()
            .then()
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/UserCreateResponseSchema.json"))
            .log().all();
  }

  public ValidatableResponse readUser(String username) {
    return given(spec)
            .basePath(USER_PATH + "/" + username)
            .when()
            .get()
            .then()
            .log().all();
  }

  public ValidatableResponse deleteUser(String username) {
    return given(spec)
            .basePath(USER_PATH + "/" + username)
            .when()
            .delete()
            .then()
            .log().all();
  }
}
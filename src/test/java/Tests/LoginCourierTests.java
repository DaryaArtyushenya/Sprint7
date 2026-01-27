package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import modelPojo.courierPojo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import steps.CourierSteps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginCourierTests {
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    CourierSteps courierSteps = new CourierSteps();
    @Test
    @DisplayName("Успешная авторизация")
    void loginCourierSuccessTest() {
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "1234");
        // loginSuccess -это только боди, те часть респонса
        LoginSuccess loginSuccess = response
                .then()
                .statusCode(200)
                .extract()
                .as(LoginSuccess.class);
        assertTrue(loginSuccess.getId() > 0);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }

    @Test
    @DisplayName("Попытка авторизации без ввода логина")
    void loginWithoutLoginFieldTest() {
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.loginCourierStep("", "1234");
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
        Response response1 =courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }

    @Test
    @DisplayName("Попытка авторизации без ввода пароля")
    void loginWithoutPasswordTest() {
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "");
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }

    @Test
    @DisplayName("Попытка авторизации, когда пара логин/пароль не совпадают")
    void loginWithIncorrectPasswordTest() {
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.loginCourierStep("dartyushenya1", "12345");
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
        Response response1 =courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }

    @Test
    @DisplayName("Попытка авторизации, когда пара логин/пароль не совпадают")
    void loginWithIncorrectLoginTest() {
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.loginCourierStep("dartyufgbfggrbrshenya1", "1234");
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }
}
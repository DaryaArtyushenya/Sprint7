package Tests;

import dataFactory.CourierFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.courierPojo.ErrorResponse;
import model.modelPojo.courierPojo.LoginSuccess;
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
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        Response response = courierSteps.loginCourierStep(courier);
        // loginSuccess -это только боди, те часть респонса
        LoginSuccess loginSuccess = response
                .then()
                .statusCode(200)
                .extract()
                .as(LoginSuccess.class);
        assertTrue(loginSuccess.getId() > 0);
        courierSteps.deleteCourierStep(courier);
    }

    @Test
    @DisplayName("Попытка авторизации без ввода логина")
    void loginWithoutLoginFieldTest() {
        Courier courier = CourierFactory.courierWithoutLogin();
        Response response = courierSteps.loginCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Попытка авторизации без ввода пароля")
    void loginWithoutPasswordTest() {
        Courier courier = CourierFactory.courierWithoutPassword();
        Response response = courierSteps.loginCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для входа", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Попытка авторизации, когда пара логин/пароль не совпадают")
    void loginWithIncorrectPasswordTest() {
        Courier courier = CourierFactory.validCourier();
        Courier courier1 = CourierFactory.courierWithIncorrectLogin();
        courierSteps.createCourierStep(courier);
        Response response = courierSteps.loginCourierStep(courier1);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
        courierSteps.loginCourierStep(courier);
        courierSteps.deleteCourierStep(courier);
    }

    @Test
    @DisplayName("Попытка авторизации, когда пара логин/пароль не совпадают")
    void loginWithIncorrectLoginTest() {
        Courier courier = CourierFactory.validCourier();
        Courier courier1 = CourierFactory.courierWithIncorrectPassword();
        courierSteps.createCourierStep(courier);
        Response response = courierSteps.loginCourierStep(courier1);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Учетная запись не найдена", errorResponse.getMessage());
        Response response1 = courierSteps.loginCourierStep(courier);
        courierSteps.deleteCourierStep(courier);
    }
}
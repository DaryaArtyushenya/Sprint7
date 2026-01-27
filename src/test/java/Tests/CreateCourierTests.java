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

public class CreateCourierTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    CourierSteps courierSteps = new CourierSteps();
    @Test
    @DisplayName("Проверка успешного создания курьера")
    void createCourierSuccessTest(){
        Integer courierID;
        Response response = courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        CreateDeleteSuccess object =response
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(CreateDeleteSuccess.class);
        assertTrue(object.getOk());

        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierID = loginSuccess.getId();

        courierSteps.deleteCourierStep(courierID);
    }
    @Test
    @DisplayName("Попытка создать курьера с уже существующим логином")
    void createCourierWithExistingLoginTest(){
        Integer courierID;
        courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        Response response = courierSteps.createCourierStep("dartyushenya1", "1234", "darya");
        ErrorResponse object = response
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Этот логин уже используется. Попробуйте другой.", object.getMessage());
        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");

        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierID = loginSuccess.getId();
        courierSteps.deleteCourierStep(courierID);

    }
    @Test
    @DisplayName("Попытка создать курьера без ввода логина")
    void createCourierWithoutLoginTest(){
        Response response = courierSteps.createCourierStep("", "1234", "darya");
        ErrorResponse object = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", object.getMessage());

    }
    @Test
    @DisplayName("Попытка создать курьера без ввода пароля")
    void createCourierWithoutPasswordTest(){
        Response response = courierSteps.createCourierStep("dartyushenya2", "", "darya");
        ErrorResponse object = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", object.getMessage());
    }
    @Test
    @DisplayName("Успешное создание курьера с вводом только обязательных полей")
    void createCourierWithRequiredFieldsOnlyTest(){
        Integer courierID;
        Response response = courierSteps.createCourierStep("dartyushenya1", "1234", "");
        CreateDeleteSuccess object  = response
                .then()
                .statusCode(201)
                .extract()
                .as(CreateDeleteSuccess.class);
        assertTrue(object.getOk());
        Response response1 =courierSteps.loginCourierStep("dartyushenya1", "1234");

        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierID = loginSuccess.getId();
        courierSteps.deleteCourierStep(courierID);
    }
}
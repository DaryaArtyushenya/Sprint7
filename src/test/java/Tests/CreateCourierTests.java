package Tests;

import dataFactory.CourierFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.courierPojo.CreateDeleteSuccess;
import model.modelPojo.courierPojo.ErrorResponse;
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
        Courier courier = CourierFactory.validCourier();
        Response response = courierSteps.createCourierStep(courier);
        CreateDeleteSuccess createDeleteSuccess = response
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(CreateDeleteSuccess.class);
        assertTrue(createDeleteSuccess.getOk());
        courierSteps.loginCourierStep(courier);
        courierSteps.deleteCourierStep(courier);
    }
    @Test
    @DisplayName("Попытка создать курьера с уже существующим логином")
    void createCourierWithExistingLoginTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        Response response = courierSteps.createCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(409)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Этот логин уже используется. Попробуйте другой.", errorResponse.getMessage());
        courierSteps.loginCourierStep(courier);
        courierSteps.deleteCourierStep(courier);
    }
    @Test
    @DisplayName("Попытка создать курьера без ввода логина")
    void createCourierWithoutLoginTest(){
        Courier courier = CourierFactory.courierWithoutLogin();
        Response response = courierSteps.createCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());

    }
    @Test
    @DisplayName("Попытка создать курьера без пароля")
    void createCourierWithoutPasswordTest(){
        Courier courier = CourierFactory.courierWithoutPassword();
        Response response = courierSteps.createCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Недостаточно данных для создания учетной записи", errorResponse.getMessage());
    }
    @Test
    @DisplayName("Успешное создание курьера с вводом только обязательных полей")
    void createCourierWithRequiredFieldsOnlyTest(){
        Courier courier = CourierFactory.courierWithRequiredFieldsOnly();
        Response response = courierSteps.createCourierStep(courier);
        CreateDeleteSuccess createDeleteSuccess  = response
                .then()
                .statusCode(201)
                .extract()
                .as(CreateDeleteSuccess.class);
        assertTrue(createDeleteSuccess.getOk());
        courierSteps.loginCourierStep(courier);
        courierSteps.deleteCourierStep(courier);
    }
}
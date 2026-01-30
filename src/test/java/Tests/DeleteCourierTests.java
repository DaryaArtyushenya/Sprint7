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

public class DeleteCourierTests{
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    Integer courierID;
    CourierSteps courierSteps = new CourierSteps();
    @Test
    @DisplayName("Успешное удаление курьера")
    void deleteCourierSuccessTest(){
        Courier courier = CourierFactory.validCourier();
        courierSteps.createCourierStep(courier);
        courierSteps.loginCourierStep(courier);
        Response response = courierSteps.deleteCourierStep(courier);
        CreateDeleteSuccess createDeleteSuccess = response
                .then()
                .statusCode(200)
                .extract()
                .as(CreateDeleteSuccess.class);
        assertTrue(createDeleteSuccess.getOk());

    }

    @Test
    @DisplayName("Попытка удалить курьера с несуществующим id")
    void deleteCourierWithNotExistingIDTest(){
        Courier courier = CourierFactory.courierWithNonExistingCourierId();
        Response response = courierSteps.deleteCourierStep(courier);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Курьера с таким id нет.", errorResponse.getMessage());
    }

}
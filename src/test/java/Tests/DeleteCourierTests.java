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
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierID = loginSuccess.getId();
        Response response = courierSteps.deleteCourierStep(courierID);
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
        courierSteps.createCourierStep("dartyushenya1", "1234", "имя");
        Response response = courierSteps.deleteCourierStep(92);
        ErrorResponse errorResponse = response
                .then()
                .statusCode(404)
                .extract()
                .as(ErrorResponse.class);
        assertEquals("Курьера с таким id нет.", errorResponse.getMessage());
        Response response1 = courierSteps.loginCourierStep("dartyushenya1", "1234");
        LoginSuccess loginSuccess = response1.as(LoginSuccess.class);
        courierSteps.deleteCourierStep(loginSuccess.getId());
    }

}
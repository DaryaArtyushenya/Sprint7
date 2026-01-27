package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import modelPojo.orderPojo.CreateOrderSuccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import steps.OrderSteps;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertNotNull;



public class CreateOrderTests {
    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }
    OrderSteps orderSteps = new OrderSteps();
    @ParameterizedTest
    @MethodSource("dataSource")
    @DisplayName("Попытка создание заказа")
    void createOrderSuccess(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
        Response response = orderSteps.createOrderStep(firstName,lastName,address, metroStation, phone,rentTime,deliveryDate,comment, color);
        CreateOrderSuccess createOrderSuccess = response
                .then()
                .statusCode(201)
                .extract()
                .as(CreateOrderSuccess.class);
        assertNotNull(createOrderSuccess.getTrack());
    }
    static Stream<Arguments> dataSource() {
        return Stream.of(
                Arguments.of("имя",
                        "фамилия",
                        "адрес",
                        "Пушкинская",
                        "777777777777",
                        23,
                        "2026.12.01",
                        "коммент",
                        new String[]{"BLACK", "GREY"}),
                Arguments.of("имя",
                        "фамилия",
                        "адрес",
                        "Пушкинская",
                        "777777777777",
                        23,
                        "2026.12.01",
                        "коммент",
                        new String[]{"GREY"}),
                Arguments.of("имя",
                        "фамилия",
                        "адрес",
                        "Пушкинская",
                        "777777777777",
                        23,
                        "2026.12.01",
                        "коммент",
                        new String[]{"BLACK"}),
                Arguments.of("имя",
                        "фамилия",
                        "адрес",
                        "Пушкинская",
                        "777777777777",
                        23,
                        "2026.12.01",
                        "коммент",
                        new String[]{""}),
                Arguments.of("имя",
                        "фамилия",
                        "адрес",
                        "Пушкинская",
                        "777777777777",
                        23,
                        "2026.12.01",
                        "коммент",
                        null)
        );
    }
}
package steps;

import clients.CourierClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import modelPojo.courierPojo.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierSteps extends CourierClient {
    // создали объект courierClient с типом CourierClient, чтобы мочь вызывать методы этого класса
    CourierClient courierClient = new CourierClient();
    //аннотация для Allure, чтобы в отчете отобразилось действие
    @Step("Создать курьера")
    public Response createCourierStep(String login, String password, String firstName){
        //создать реквест для создания курьера
        CreateRequest createRequest = new CreateRequest(login, password, firstName);
        //вернуть респонс для метода создания курьера, который принимает созданный в степе реквест
        return createCourier(createRequest);
    }
    @Step("Авторизация курьера")
    public Response loginCourierStep(String login, String password){
        LoginRequest loginRequest= new LoginRequest(login, password);
        return loginCourier(loginRequest);
    }
    @Step("Удалить курьера")
    public Response deleteCourierStep(Integer courierId){
        DeleteRequest deleteRequest = new DeleteRequest(courierId);
        return deleteCourier(deleteRequest);
    }
}
package steps;

import clients.CourierClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.modelData.Courier;
import model.modelPojo.courierPojo.CreateRequest;
import model.modelPojo.courierPojo.DeleteRequest;
import model.modelPojo.courierPojo.LoginRequest;


public class CourierSteps extends CourierClient {
    // создали объект courierClient с типом CourierClient, чтобы мочь вызывать методы этого класса
    //аннотация для Allure, чтобы в отчете отобразилось действие
    @Step("Создать курьера")
    public Response createCourierStep(Courier courier){
        //создать реквест для создания курьера
        CreateRequest createRequest = new CreateRequest(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        //вернуть респонс для метода создания курьера, который принимает созданный в степе реквест
        return createCourier(createRequest);
    }
    @Step("Авторизация курьера")
    public Response loginCourierStep(Courier courier){
        LoginRequest loginRequest= new LoginRequest(courier.getLogin(), courier.getPassword());
        Response response = loginCourier(loginRequest);
        Integer id = response.path("id");
        courier.setCourierId(id);
        return response;
    }

    @Step("Удалить курьера")
    public Response deleteCourierStep(Courier courier){
        DeleteRequest deleteRequest = new DeleteRequest(courier.getCourierId());
        return deleteCourier(deleteRequest);
    }


}
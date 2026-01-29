package dataFactory;

import model.modelData.Courier;

import java.util.concurrent.ThreadLocalRandom;

public class CourierFactory {

    public static Courier validCourier(){
        return new Courier(
                "dartyushenya1",
                "qwerty123",
                "darya");
    }
    public static Courier courierWithRequiredFieldsOnly(){
        return new Courier(
                "dartyushenya1",
                "qwerty123",
                "");
    }
    public static Courier courierWithoutLogin(){
        return new Courier(
                "",
                "qwerty123",
                "darya");
    }
    public static Courier courierWithoutPassword(){
        return new Courier(
                "dartyushenya1",
                "",
                "darya");
    }
    public static Courier courierWithIncorrectLogin(){
        return new Courier(
                "dartyushenya1121",
                "qwerty123",
                "darya");
    }
    public static Courier courierWithIncorrectPassword(){
        return new Courier(
                "dartyushenya1",
                "jfdnvkdjfn",
                "darya");
    }
    public static Courier courierWithNonExistingCourierId(){
        Courier courier =  new Courier(
                "dartyushenya1",
                "jfdnvkdjfn",
                "darya");
        courier.setCourierId(ThreadLocalRandom.current().nextInt(1_000_000, 9_999_999));
        return courier;
    }
}

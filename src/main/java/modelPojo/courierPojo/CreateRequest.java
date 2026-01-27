package modelPojo.courierPojo;

public class CreateRequest extends LoginRequest{
    //pojo -класс с приватными полями, который хранит данные
    private String firstName;

    //конструктор со всеми параметрами
    public CreateRequest(String login, String password, String firstName) {
        super(login, password);
        this.firstName = firstName;
    }

    //конструктор без параметров для библиотеки gson
    public CreateRequest() {
    }

    //геттеры и сеттеры

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
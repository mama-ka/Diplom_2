import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderWithoutTokenTest {
    private final String ingredients;
    private int statusCode;

    public CreateOrderWithoutTokenTest(String ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }
    @Parameterized.Parameters
    public static Object[] getIngredients() {
        return new Object[][]{
                {"{\"ingredients\": [\"ppppppppppppppppp\"] }", 500},
                {"{\"ingredients\": [] }", 400},
                {"{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6f\"] }", 200},
        };
    }

    //создать заказ без токена можно, оформить на сайте - нельзя
    @Test
    @DisplayName("Создание заказа без токена")
    public void orderCreateWithoutToken(){
        OrderCreate orderCreate = new OrderCreate();
        orderCreate.createOrderWithoutToken(ingredients)
                .assertThat()
                .statusCode(statusCode);
    }
}

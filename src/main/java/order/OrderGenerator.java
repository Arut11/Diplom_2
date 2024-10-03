package order;

import com.github.javafaker.Faker;
import java.util.ArrayList;

public class OrderGenerator {

    public static Faker faker = new Faker();

    public static Order getOrder(ArrayList<String> ingredientsId) {
        int ingredientsCount = faker.number().numberBetween(1, ingredientsId.size());
        ArrayList<String> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < ingredientsCount; i++) {
            int randomIndex = faker.number().numberBetween(0, ingredientsId.size());
            selectedIngredients.add(ingredientsId.get(randomIndex));
        }
        return new Order(selectedIngredients);
    }

}

package Ingredients;

import java.util.ArrayList;

public class IngredientGenerator {
    public static Ingredient getDefault() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa70");
        ingredients.add("61c0c5a71d1f82001bdaaa6c");
        System.out.println(ingredients);
        return new Ingredient(ingredients);
    }

    public static Ingredient getEmpty() {
        ArrayList<String> ingredients = new ArrayList<>();
        System.out.println(ingredients);
        return new Ingredient(ingredients);
    }

    public static Ingredient getErrorHash() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a11d1f82001bdaaa71");
        System.out.println(ingredients);
        return new Ingredient(ingredients);
    }
}

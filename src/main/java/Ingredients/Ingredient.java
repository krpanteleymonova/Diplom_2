package Ingredients;

import java.util.ArrayList;

public class Ingredient {
    ArrayList<String> ingredients = new ArrayList<>();

    public Ingredient(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Ingredient {


    ArrayList<String> ingredients = new ArrayList<>();

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredient (ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }



}

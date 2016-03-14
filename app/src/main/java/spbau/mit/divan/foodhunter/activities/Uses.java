package spbau.mit.divan.foodhunter.activities;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

public class Uses {
    public static final String SEARCH_TEXT = "searchText";
    public static final String PLACE = "place";
    public static final String DISH = "dish";
    public static final String FOOD_OR_PLACE = "FoodOrPlace";
    public static final String CORRECT_COORDINATES = "-?\\d+(\\.\\d+)?";
    public static final String CORRECT_PRICE = "^-?\\d+$";

    public enum SearchChoice {SEARCH_FOOD, SEARCH_PLACE}

    public static void clearEditText(EditText editText) {
        if (editText.getText().toString().equals("Enter the name of place or dish…")) {
            editText.setText("");
        }
    }

    public static void showToast(Context context, String text) {
        Toast toast = makeText(context, text, LENGTH_LONG);
        toast.show();
    }
}

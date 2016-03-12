package spbau.mit.divan.foodhunter.activities;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import spbau.mit.divan.foodhunter.R;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class FoodHunterUtil {
    public static void clearEditText(EditText editText) {
        if (editText.getText().toString().equals(editText.getResources().getString(R.string.search_line))) {
            editText.setText(editText.getResources().getString(R.string.empty));
        }
    }

    public static void showToast(Context context, String text) {
        Toast toast = makeText(context, text, LENGTH_LONG);
        toast.show();
    }
}

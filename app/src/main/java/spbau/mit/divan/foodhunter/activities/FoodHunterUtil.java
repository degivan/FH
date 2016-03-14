package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.function.Supplier;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.MapObject;
import spbau.mit.divan.foodhunter.net.Client;

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

    public static void displayMapObjectRate(MapObject mapObject, Activity context, String text) {
        ((RatingBar) context.findViewById(R.id.ratingBar)).setRating((float) mapObject.getRate());
        ((TextView) context.findViewById(R.id.rateIndexText)).setText(new StringBuilder().append(Integer.toString(mapObject.getRateIndex()))
                .append(text).toString());
    }

    public static void openNetActivity(Activity context, Intent intent) {
        onNetConnectedAction(context, () -> {
            context.startActivity(intent);
            return null;
        });
    }

    public static void openNetActivityAndFinish(Activity context, Intent intent) {
        onNetConnectedAction(context, () -> {
            context.startActivity(intent);
            context.finish();
            return null;
        });
    }

    public static void onNetConnectedAction(Activity context, Supplier supplier) {
        if (Client.isOnline(context)) {
            supplier.get();
        } else {
            showToast(context.getApplicationContext(), context.getResources().getString(R.string.no_connection));
        }
    }
}

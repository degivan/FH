package spbau.mit.divan.foodhunter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.onNetConnectedAction;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;

public class AddPlace extends AppCompatActivity {
    private static final String CORRECT_COORDINATES = "-?\\d+(\\.\\d+)?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
    }

    public void onAddPlaceClick(View view) {
       onNetConnectedAction(this, () -> {
           String name = ((TextView) findViewById(R.id.newPlaceName)).getText().toString();
           String address = ((TextView) findViewById(R.id.newPlaceAddress)).getText().toString();
           String openHours = ((TextView) findViewById(R.id.newPlaceOpenHrs)).getText().toString();
           String latitude = ((TextView) findViewById(R.id.newPlaceLatitude)).getText().toString();
           String longitude = ((TextView) findViewById(R.id.newPlaceLongitude)).getText().toString();
           if (latitude.matches(CORRECT_COORDINATES) && longitude.matches(CORRECT_COORDINATES)) {
               Client.pushPlace(name, address, openHours, Double.parseDouble(latitude), Double.parseDouble(longitude));
               finish();
           } else {
               showToast(getApplicationContext(), getResources().getString(R.string.m_incorrect_coordinates));
           }
           return null;
       });
    }
}

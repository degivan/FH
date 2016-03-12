package spbau.mit.divan.foodhunter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.Uses.CORRECT_COORDINATES;
import static spbau.mit.divan.foodhunter.activities.Uses.showToast;

public class AddPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
    }

    public void onAddPlaceClick(View view) {
        String name = ((TextView) findViewById(R.id.newPlaceName)).getText().toString();
        String address = ((TextView) findViewById(R.id.newPlaceAddress)).getText().toString();
        String openHours = ((TextView) findViewById(R.id.newPlaceOpenHrs)).getText().toString();
        String latitude = ((TextView) findViewById(R.id.newPlaceLatitude)).getText().toString();
        String longitude = ((TextView) findViewById(R.id.newPlaceLongitude)).getText().toString();
        if (latitude.matches(CORRECT_COORDINATES) && longitude.matches(CORRECT_COORDINATES)) {
            Client.pushPlace(name, address, openHours, Double.parseDouble(latitude), Double.parseDouble(longitude));
            finish();
        } else {
            showToast(getApplicationContext(), "Incorrect coordinates");
        }
    }
}

package spbau.mit.divan.foodhunter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

app.Ap CompatActivity;

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
        if (latitude.matches("-?\\d+(\\.\\d+)?") && longitude.matches("-?\\d+(\\.\\d+)?")) {
            Client.pushPlace(name, address, openHours, Double.parseDouble(latitude), Double.parseDouble(longitude));
            finish();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect coordinates", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}

package spbau.mit.divan.foodhunter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

public class NewReview extends AppCompatActivity {

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);
        place = (Place)getIntent().getExtras().get("place");
    }

    public void onSendReviewClick(View view) {
        Client.sendReview(place, ((EditText)findViewById(R.id.reviewEditText)).getText().toString());
        finish();
    }
}

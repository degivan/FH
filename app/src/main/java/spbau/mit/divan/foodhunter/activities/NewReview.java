package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.onNetConnectedAction;

public class NewReview extends AppCompatActivity {
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);
        place = (Place) getIntent().getExtras().get(PLACE_EXTRA_NAME);
    }

    public void onSendReviewClick(View view) {
        onNetConnectedAction(this, () -> {
            Client.sendReview(place, ((EditText) findViewById(R.id.reviewEditText)).getText().toString(), this);
            this.onBackPressed();
            return null;
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewReview.this, PlacePage.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
        finish();
    }
}

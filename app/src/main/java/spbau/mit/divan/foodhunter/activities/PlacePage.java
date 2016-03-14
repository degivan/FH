package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;
import spbau.mit.divan.foodhunter.net.Client;
import spbau.mit.divan.foodhunter.net.UserInfo;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.displayMapObjectRate;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.onNetConnectedAction;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivity;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivityAndFinish;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.showToast;

public class PlacePage extends AppCompatActivity {
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_page);
        place = (Place) getIntent().getExtras().get(PLACE_EXTRA_NAME);
        ((TextView) findViewById(R.id.placeNameText)).setText(place.getName());
        ((TextView) findViewById(R.id.placeAddressText)).setText(place.getAddress());
        ((TextView) findViewById(R.id.placeOpenHoursText)).setText(place.getOpenHours());
        displayRate();
        if (UserInfo.isPlaceRateChangedBefore(place, this)) {
            ((RatingBar) findViewById(R.id.ratingBar)).setIsIndicator(true);
        }
    }

    public void onViewReviewsClick(View view) {
        Intent intent = new Intent(PlacePage.this, ReviewsList.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        startActivity(intent);
    }

    public void onWriteReviewClick(View view) {
        if (!UserInfo.isPlaceReviewedBefore(place, this)) {
            Intent intent = new Intent(PlacePage.this, NewReview.class);
            intent.putExtra(PLACE_EXTRA_NAME, place);
            startActivity(intent);
            finish();
        } else {
            showToast(this, getResources().getString(R.string.already_reviewed));
        }
    }

    public void onViewMenuClick(View view) {
        if (!UserInfo.isPlaceReviewedBefore(place, this)) {
            Intent intent = new Intent(PlacePage.this, PlaceMenu.class);
            intent.putExtra(PLACE_EXTRA_NAME, place);
            openNetActivity(this, intent);
        } else {
            showToast(this, getResources().getString(R.string.already_reviewed));
        }
    }

    public void onRatePlaceClick(View view) {
        onNetConnectedAction(this, () -> {
            if (!UserInfo.isPlaceReviewedBefore(place, this)) {
                if (!UserInfo.isPlaceRateChangedBefore(place, this)) {
                    Client.changePlaceRate(place, ((RatingBar) findViewById(R.id.ratingBar)).getRating(), this);
                    displayRate();
                    ((RatingBar) findViewById(R.id.ratingBar)).setIsIndicator(true);
                } else {
                    showToast(this, getResources().getString(R.string.already_rated));
                }
            } else {
                showToast(this, getResources().getString(R.string.already_reviewed));
            }
            return null;
        });
    }

    public void onShowOnMapClick(View view) {
        Intent intent = new Intent(PlacePage.this, ShowOnMap.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        openNetActivity(this, intent);
    }

    public void onAddNewDishClick(View view) {
        Intent intent = new Intent(PlacePage.this, AddDish.class);
        intent.putExtra(PLACE_EXTRA_NAME, place);
        openNetActivityAndFinish(this, intent);
    }

    private void displayRate() {
        displayMapObjectRate(place, this, getResources().getString(R.string.t_place_rate_index));
    }
}

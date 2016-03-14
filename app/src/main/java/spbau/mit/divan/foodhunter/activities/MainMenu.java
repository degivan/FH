package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.QueryHistory;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_CHOICE_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_TEXT_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.clearEditText;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivity;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivityAndFinish;

public class MainMenu extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main_menu);
        searchLine = (EditText) findViewById(R.id.searchLine);
        String searchText = getIntent().getStringExtra(SEARCH_TEXT_EXTRA_NAME);
        if (searchText != null) {
            searchLine.setText(searchText);
        }
    }

    public void onFindPlaceForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, PlacesListPage.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        QueryHistory.addPlaceQuery(this, searchLine.getText().toString());
        openNetActivityAndFinish(this, intent);
    }

    public void onFindFoodForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, DishesListPage.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        QueryHistory.addDishQuery(this, searchLine.getText().toString());
        openNetActivityAndFinish(this, intent);
    }

    public void onFindPlaceForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        intent.putExtra(SEARCH_CHOICE_EXTRA_NAME, ShowMap.SearchChoice.SEARCH_PLACE);
        QueryHistory.addPlaceQuery(this, searchLine.getText().toString());
        openNetActivityAndFinish(this, intent);
    }

    public void onFindFoodForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        intent.putExtra(SEARCH_CHOICE_EXTRA_NAME, ShowMap.SearchChoice.SEARCH_FOOD);
        QueryHistory.addDishQuery(this, searchLine.getText().toString());
        openNetActivityAndFinish(this, intent);
    }

    public void onAddNewPlaceClick(View view) {
        Intent intent = new Intent(MainMenu.this, AddPlace.class);
        openNetActivity(this, intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onShowAllPlacesClick(View view) {
        Intent intent = new Intent(MainMenu.this, PlacesListPage.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, getResources().getString(R.string.empty));
        openNetActivityAndFinish(this, intent);
    }

    public void onSearchLineClick(View view) {
        clearEditText(searchLine);
    }

    public void onViewHistoryClick(View view) {
        Intent intent = new Intent(MainMenu.this, ViewHistoryPage.class);
        startActivity(intent);
    }
}

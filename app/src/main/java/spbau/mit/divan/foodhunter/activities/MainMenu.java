package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import spbau.mit.divan.foodhunter.R;

public class MainMenu extends ActivityWithSearchLine {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main_menu);
        searchLine = (EditText) findViewById(R.id.search_line);
        String searchText = getIntent().getStringExtra("searchText");
        if (searchText != null)
            searchLine.setText(searchText);
    }

    public void onFindPlaceForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, PlacesListPage.class);
        intent.putExtra("name", searchLine.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onFindFoodForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, DishesListPage.class);
        intent.putExtra("name", searchLine.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onFindPlaceForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra("name", searchLine.getText().toString());
        intent.putExtra("foodnotplace", false);
        startActivity(intent);
        finish();
    }

    public void onFindFoodForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra("name", searchLine.getText().toString());
        intent.putExtra("foodnotplace", true);
        startActivity(intent);
        finish();
    }

    public void onAddNewPlaceClick(View view) {
        Intent intent = new Intent(MainMenu.this, AddPlace.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

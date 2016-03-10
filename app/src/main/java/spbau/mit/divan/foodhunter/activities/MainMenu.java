package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import spbau.mit.divan.foodhunter.R;

public class MainMenu extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main_menu);
        searchLine = (EditText) findViewById(R.id.search_line);
    }

    public void onFindPlaceForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, PlacesListPage.class);
        intent.putExtra("name", searchLine.getText().toString());
        startActivity(intent);
    }

    public void onFindFoodForNameClick(View view) {
        Intent intent = new Intent(MainMenu.this, DishesListPage.class);
        intent.putExtra("name", searchLine.getText().toString());
        startActivity(intent);
    }


    public void onFindPlaceForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra("name", searchLine.getText().toString());
        intent.putExtra("foodnotplace", false);
        startActivity(intent);
    }

    public void onFindFoodForNameNearbyClick(View view) {
        Intent intent = new Intent(MainMenu.this, ShowMap.class);
        intent.putExtra("name", searchLine.getText().toString());
        intent.putExtra("foodnotplace", true);
        startActivity(intent);
    }

    public void onSearchLineClick(View view) {
        if (searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}

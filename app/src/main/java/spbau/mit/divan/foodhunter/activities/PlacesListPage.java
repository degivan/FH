package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.Uses.SEARCH_TEXT;
import static spbau.mit.divan.foodhunter.activities.Uses.clearEditText;

public class PlacesListPage extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list_page);
        String placeName = getIntent().getStringExtra(SEARCH_TEXT);
        searchLine = (EditText) findViewById(R.id.placesSearchLine);
        searchLine.setText(placeName);
        Client.request(ItemListValueEventListeners.placesListListener(placeName, this));
    }

    public void onFindPlacesClick(View view) {
        Intent intent = new Intent(PlacesListPage.this, PlacesListPage.class);
        intent.putExtra(SEARCH_TEXT, ((EditText) findViewById(R.id.placesSearchLine)).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlacesListPage.this, MainMenu.class);
        intent.putExtra(SEARCH_TEXT, searchLine.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onSearchLineClick(View view) {
        clearEditText(searchLine);
    }
}

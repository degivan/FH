package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

public class PlacesListPage extends ActivityWithSearchLine {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list_page);
        String placeName = getIntent().getStringExtra("name");
        searchLine = (EditText) findViewById(R.id.placesSearchLine);
        searchLine.setText(placeName);
        Client.request(ItemListValueEventListeners.placesListListener(placeName, this));
    }

    public void onFindPlacesClick(View view) {
        Intent intent = new Intent(PlacesListPage.this, PlacesListPage.class);
        intent.putExtra("name", ((EditText) findViewById(R.id.placesSearchLine)).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlacesListPage.this, MainMenu.class);
        intent.putExtra("searchText", searchLine.getText().toString());
        startActivity(intent);
        finish();
    }
}

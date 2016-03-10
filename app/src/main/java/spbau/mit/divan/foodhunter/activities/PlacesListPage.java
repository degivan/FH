package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

public class PlacesListPage extends AppCompatActivity {

    private EditText searchLine;

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

    public void onSearchLineClick(View view) {
        if (searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}

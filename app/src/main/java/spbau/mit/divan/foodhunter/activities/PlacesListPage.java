package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;
import spbau.mit.divan.foodhunter.net.QueryHistory;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_TEXT_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.clearEditText;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivity;

public class PlacesListPage extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list_page);
        String placeName = getIntent().getStringExtra(SEARCH_TEXT_EXTRA_NAME);
        searchLine = (EditText) findViewById(R.id.placesSearchLine);
        searchLine.setText(placeName);
        Client.request(ItemListValueEventListeners.placesListListener(placeName, this));
    }

    public void onFindPlacesClick(View view) {
            Intent intent = new Intent(PlacesListPage.this, PlacesListPage.class);
            intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
            QueryHistory.addPlaceQuery(this, searchLine.getText().toString());
            openNetActivity(this, intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PlacesListPage.this, MainMenu.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onSearchLineClick(View view) {
        clearEditText(searchLine);
    }
}

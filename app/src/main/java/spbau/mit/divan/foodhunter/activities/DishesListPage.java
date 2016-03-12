package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_TEXT_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.clearEditText;

public class DishesListPage extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_list_page);
        String dishName = getIntent().getStringExtra(SEARCH_TEXT_EXTRA_NAME);
        searchLine = (EditText) findViewById(R.id.dishesSearchLine);
        searchLine.setText(dishName);
        Client.request(ItemListValueEventListeners.dishesListListener(dishName, this));
    }

    public void onFindDishesClick(View view) {
        Intent intent = new Intent(DishesListPage.this, DishesListPage.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, ((EditText) findViewById(R.id.dishesSearchLine)).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DishesListPage.this, MainMenu.class);
        intent.putExtra(SEARCH_TEXT_EXTRA_NAME, searchLine.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onSearchLineClick(View view) {
        clearEditText(searchLine);
    }
}

package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Client;

public class DishesListPage extends AppCompatActivity {
    private EditText searchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_list_page);
        String dishName = getIntent().getStringExtra("name");
        searchLine = (EditText) findViewById(R.id.dishesSearchLine);
        searchLine.setText(dishName);
        Client.request(ItemListValueEventListeners.dishesListListener(dishName, this));
    }

    public void onFindDishesClick(View view) {
        Intent intent = new Intent(DishesListPage.this, DishesListPage.class);
        intent.putExtra("name", ((EditText) findViewById(R.id.dishesSearchLine)).getText().toString());
        startActivity(intent);
    }

    public void onSearchLineClick(View view) {
        if (searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}

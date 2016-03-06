package spbau.mit.divan.foodhunter.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;

public class ReviewsList extends AppCompatActivity {
    private List<String> reviewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);
        ListView listView = (ListView) findViewById(R.id.reviews_list);
        reviewsList = ((Place) getIntent().getSerializableExtra("place")).getReviews();
        if (reviewsList.size() == 0) {
            findViewById(R.id.no_result).setVisibility(View.VISIBLE);
        } else{
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, reviewsList);
            listView.setAdapter(adapter);
        }
    }
}

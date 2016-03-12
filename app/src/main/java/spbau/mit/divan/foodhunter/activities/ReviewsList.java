package spbau.mit.divan.foodhunter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Place;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.PLACE_EXTRA_NAME;

public class ReviewsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);
        ListView listView = (ListView) findViewById(R.id.reviews_list);
        Collection<String> reviewsCollection = ((Place) getIntent().getSerializableExtra(PLACE_EXTRA_NAME)).getReviews();
        List<String> reviewsList = new ArrayList<>();
        reviewsList.addAll(reviewsCollection);
        if (reviewsList.isEmpty()) {
            findViewById(R.id.no_result).setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, reviewsList);
            listView.setAdapter(adapter);
        }
    }
}

package spbau.mit.divan.foodhunter.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import spbau.mit.divan.foodhunter.R;

public class ActivityWithSearchLine extends AppCompatActivity {
    public EditText searchLine;

    public void onSearchLineClick(View view) {
        if (searchLine.getText().toString().equals(getResources().getString(R.string.search_line))) {
            searchLine.setText("");
        }
    }
}

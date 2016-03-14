package spbau.mit.divan.foodhunter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.net.Query;
import spbau.mit.divan.foodhunter.net.QueryHistory;

import static spbau.mit.divan.foodhunter.activities.ExtraNames.SEARCH_TEXT_EXTRA_NAME;
import static spbau.mit.divan.foodhunter.activities.FoodHunterUtil.openNetActivityAndFinish;

public class ViewHistoryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_histoty_page);
        ListView listView = (ListView) findViewById(R.id.queries_list);
        List<Query> queryList = QueryHistory.getQueryHistory(this);
        if(queryList.isEmpty()) {
            findViewById(R.id.no_result).setVisibility(View.VISIBLE);
        } else {
            List<String> items = Stream.of(queryList)
                    .map(Query::getQuery)
                    .collect(Collectors.toList());
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent;
                    if (queryList.get(position).isPlace()) {
                        intent = new Intent(ViewHistoryPage.this, PlacesListPage.class);
                    } else {
                        intent = new Intent(ViewHistoryPage.this, DishesListPage.class);
                    }
                    intent.putExtra(SEARCH_TEXT_EXTRA_NAME, queryList.get(position).getQuery());
                    openNetActivityAndFinish(this, intent);
            });
        }
    }

    public void onClearHistory(View view) {
        QueryHistory.clearHistory(this);
        finish();
    }
}

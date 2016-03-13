package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class HistoryKeeper {
    private static SharedPreferences sharedPreferences;
    private static Editor editor;

    public static List<String> getHistory(Activity context) {
        sharedPreferences = context.getPreferences(MODE_PRIVATE);
        List<String> queries = new ArrayList<>();
        queries.addAll(sharedPreferences.getStringSet("history", new HashSet<>()));
        return Stream.of(queries)
                .sorted((s1, s2) -> getOrder(s1, sharedPreferences) - getOrder(s2, sharedPreferences))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static void addQuery(String query, Activity context) {
        sharedPreferences = context.getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Set<String> historySet = sharedPreferences.getStringSet("history", new HashSet<>());
        historySet.add(query);
        editor.putStringSet("history", historySet);
        editor.putInt(query, historySet.size());
        editor.apply();
    }

    private static int getOrder(String query, SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(query, 0);
    }
}

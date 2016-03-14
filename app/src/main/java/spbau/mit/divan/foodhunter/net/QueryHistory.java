package spbau.mit.divan.foodhunter.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class QueryHistory {
    private static final int MAX_QUERY_COUNT = 100;
    private static final String QUERY_COUNT_KEY = "$queryCount";
    private static final String QUERY_HISTORY = "QueryHistory";

    public static void addPlaceQuery(Activity context, String query) {
        addQuery(context, query, true);
    }

    public static void addDishQuery(Activity context, String query) {
        addQuery(context, query, false);
    }

    public static List<Query> getQueryHistory(Activity context) {
        int queryCount = getQueryCount(context);
        List<Query> history = new ArrayList<>();
        for (int i = 0; i < queryCount; i++) {
            history.add(getQuery(context, i));
        }
        return history;
    }

    private static Query getQuery(Activity context, int index) {
        SharedPreferences sharedPreferences = getQueryHistoryPreferences(context);
        String query = sharedPreferences.getString(queryKeyFromInt(index), "");
        Boolean isPlace = sharedPreferences.getBoolean(queryTypeKeyFromInt(index), true);
        return new Query(query, isPlace);
    }

    private static void addQuery(Activity context, String query, Boolean queryType) {
        if (isQueryNew(query, context)) {
            SharedPreferences sharedPreferences = getQueryHistoryPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int queryCount = getQueryCount(context);
            if (queryCount < MAX_QUERY_COUNT) {
                editor.putInt(QUERY_COUNT_KEY, queryCount + 1);
                pushQuery(context, queryCount, query, queryType);
            } else {
                for (int i = 0; i < MAX_QUERY_COUNT; i++) {
                    String nextQuery = sharedPreferences.getString(queryKeyFromInt(i + 1), "");
                    pushQuery(context, i, nextQuery, queryType);
                }
                pushQuery(context, MAX_QUERY_COUNT, query, queryType);
            }
            editor.apply();
        }
    }

    private static int getQueryCount(Activity context) {
        SharedPreferences sharedPreferences = getQueryHistoryPreferences(context);
        return sharedPreferences.getInt(QUERY_COUNT_KEY, 0);
    }

    private static String queryKeyFromInt(int number) {
        return ("$" + Integer.toString(number));
    }

    private static String queryTypeKeyFromInt(int number) {
        return ("%" + Integer.toString(number));
    }

    private static void pushQuery(Activity context, int index, String query, Boolean queryType) {
        SharedPreferences.Editor editor = getQueryHistoryPreferences(context).edit();
        editor.putString(queryKeyFromInt(index), query);
        editor.putBoolean(queryTypeKeyFromInt(index), queryType);
        editor.apply();
    }

    public static void clearHistory(Activity context) {
        SharedPreferences.Editor editor = getQueryHistoryPreferences(context).edit();
        editor.putInt(QUERY_COUNT_KEY, 0);
        editor.apply();
    }

    private static boolean isQueryNew(String query, Activity context) {
        if (query.length() == 0)
            return false;
        List<Query> queries = getQueryHistory(context);
        for (Query q : queries) {
            if (q.getQuery().equals(query))
                return false;
        }
        return true;
    }
    private static SharedPreferences getQueryHistoryPreferences(Activity context) {
        return context.getSharedPreferences(QUERY_HISTORY, Context.MODE_PRIVATE);
    }
}

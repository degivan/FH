package spbau.mit.divan.foodhunter.net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;

public class UserInfo {
    public static final String EMPTY_LOGIN = "";
    private static final String LOGIN_KEY = "#login";
    private static final String USER_INFO = "UserInfo";

    public static String getLogin(Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        return sharedPreferences.getString(LOGIN_KEY, "");
    }

    public static void setLogin (Activity context, String login) {
        SharedPreferences.Editor editor = getUserInfoPreferences(context).edit();
        editor.putString(LOGIN_KEY, login);
        editor.apply();
    }

    public static boolean isPlaceRateChangedBefore(Place place, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        return sharedPreferences.getBoolean(Integer.toString(place.getId()), false);
    }

    public static void addPlaceWithChangedRate(Place place, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Integer.toString(place.getId()), true);
        editor.apply();
    }

    public static boolean isDishRateChangedBefore(Dish dish, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        return sharedPreferences.getBoolean(Integer.toString(dish.getDishId()) + "#" + Integer.toString(dish.getPlaceId()), false);
    }

    public static void addDishWithChangedRate(Dish dish, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Integer.toString(dish.getDishId()) + "#" + Integer.toString(dish.getPlaceId()), true);
        editor.apply();
    }

    public static boolean isPlaceReviewedBefore(Place place, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        return sharedPreferences.getBoolean(Integer.toString(place.getId()) + "#", false);
    }

    public static void addPlaceWithNewReview(Place place, Activity context) {
        SharedPreferences sharedPreferences = getUserInfoPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Integer.toString(place.getId()) + "#", true);
        editor.apply();
    }

    private static SharedPreferences getUserInfoPreferences(Activity context) {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }
}

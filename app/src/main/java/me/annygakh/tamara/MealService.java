package me.annygakh.tamara;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.annygakh.tamara.model.Meal;

/**
 * Created by annygakhokidze on 10/18/15.
 */
public class MealService {
    public final static String LOG_TAG = "MealService";
    private static MealService instance = new MealService();
    private static List<Meal> meals;
    private static Bitmap bitmap;
    private static Context c;

    private MealService() {
        meals = new ArrayList<>();
    }

    public static MealService getInstance() {
        if (instance == null) {
            instance = new MealService();
        }
        return instance;
    }

    public static Bitmap getPhoto(Context context) {
        Resources resources = context.getResources();
        BitmapDrawable bd = (BitmapDrawable) ResourcesCompat.getDrawable(resources,
                R.drawable.cat, null);
        Bitmap bitmap = bd.getBitmap();

        return bitmap;
    }

    public static void seedPhotos(Context context) {
        Resources resources = context.getResources();
        BitmapDrawable bd = (BitmapDrawable) ResourcesCompat.getDrawable(resources,
                R.drawable.cat, null);
        Bitmap fbitmap = bd.getBitmap();
        c = context;

        bitmap = fbitmap;

        Meal meal = new Meal(bitmap);
        meal.setLatLon(49.1916740, -122.8299600);
        Uri uri = Uri.parse("android.resource://me.annygakh.tamara/" + R.drawable.cat);
        String path = uri.toString();
        meal.setPictureFilePath(path);
        meal.setmCreatedDate(new Date(2012 - 1970));
        meals.add(meal);

        Meal meal2 = new Meal(bitmap);
        meal2.setLatLon(49.1916740, -122.8299600);
        Uri uri2 = Uri.parse("android.resource://me.annygakh.tamara/" + R.drawable.cat);
        String path2 = uri2.toString();
        meal2.setPictureFilePath(path2);
        meal2.setmCreatedDate(new Date(2015 - 1970, 9, 24));
        meals.add(meal2);

        Meal meal3 = new Meal(bitmap);
        meal3.setLatLon(49.1916740, -122.8299600);
        Uri uri3 = Uri.parse("android.resource://me.annygakh.tamara/" + R.drawable.cat);
        String path3 = uri3.toString();
        meal3.setPictureFilePath(path3);
        meal3.setmCreatedDate(new Date(2015 - 1970, 9, 23));
        meals.add(meal3);

    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        MealService.bitmap = bitmap;
    }

    public void addMeal(Meal meal) {
        meals.add(0, meal);
    }

    public void removeMeal(Meal meal) {
        meals.remove(meal);
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public Meal getMeal(String anotherSimpleDate) {
        for (Meal meal : meals) {
            if (meal.getSimpleDate().equals(anotherSimpleDate)) {
                return meal;
            }
        }
        return null;
    }

    public String getRelativeDate(Date date) {
        Date now = new Date();
        if (date.compareTo(now) > 0) {
            return "Future";
        }
        if (date.getYear() < date.getYear() || date.getMonth() < date.getMonth()) {
            return "Long time ago";
        }
        int difference = date.getDate() - now.getDate();
        switch (difference) {
            case 0:
                return "Today";
            case -1:
                return "Yesterday";
            case -2:
                return "2 Days ago";
        }
        return "Long time ago";
    }


}

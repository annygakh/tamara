package me.annygakh.tamara;

import android.app.Application;

/**
 * Created by annygakhokidze on 10/25/15.
 */
public class MyApplication extends Application {

    public MyApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        MealService.getInstance();
        MealService.seedPhotos(this);
    }

}

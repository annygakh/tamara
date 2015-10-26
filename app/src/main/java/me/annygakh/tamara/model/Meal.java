package me.annygakh.tamara.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by annygakhokidze on 10/18/15.
 */
public class Meal {
    private final static String LOG_TAG = Meal.class.getSimpleName();

    private Bitmap mPhoto;
    private double mLat, mLon;
    //    private List<Ingredient> mIngredients;
    private List<String> mIngredients;

    public void setmCreatedDate(Date mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    private Date mCreatedDate;
    private String mPictureFilePath;
    private String mLocationString;
    
    public Meal(Bitmap photo) {
        this.mPhoto = photo;
//        mIngredients = new ArrayList<Ingredient>();
        mIngredients = new ArrayList<>();
        // TODO fix for imported pictures
        mCreatedDate = new Date();
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<String> mIngredients) {
        this.mIngredients = mIngredients;
    }

//    public List<Ingredient> addIngredient(Ingredient ingr) {
//        mIngredients.add(ingr);
//        return mIngredients;
//    }
//
//    public List<Ingredient> removeIngredient(Ingredient ingr) {
//        mIngredients.remove(ingr);
//        return mIngredients;
//    }

    public String getIngredientsString() {
        if (mIngredients.isEmpty()) {
            return "";
        }
        String result = Arrays.toString(mIngredients.toArray());
        return result.replace("[", "").replace("]", "");
    }

    public double getLat() {
        return mLat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLatLon(double lat, double lon) {
        mLat = lat;
        mLon = lon;
    }
    public Bitmap getPhoto() {
        return mPhoto;
    }

    public String getPictureFilePath() {
        return mPictureFilePath;
    }

    public void setPictureFilePath(String mPictureFilePath) {
        this.mPictureFilePath = mPictureFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meal meal = (Meal) o;

        return !(mPictureFilePath != null ? !mPictureFilePath.equals(meal.mPictureFilePath) : meal.mPictureFilePath != null);

    }

    @Override
    public int hashCode() {
        return mPictureFilePath != null ? mPictureFilePath.hashCode() : 0;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public String getSimpleDate() {
        return mCreatedDate.toString();
    }

    public String getLocation() {
        return mLocationString;
    }

    public void setLocation(String location) {
        this.mLocationString = location;
    }

}

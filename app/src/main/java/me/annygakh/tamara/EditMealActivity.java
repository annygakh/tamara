package me.annygakh.tamara;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import me.annygakh.tamara.model.Meal;

public class EditMealActivity extends AppCompatActivity {
    public final static String LOG_TAG = "EditMealActivity";
    public final static String PHOTO_PATH_EXTRA = "me.annygakh.tamara.photo_path";
    public final static String LAT_EXTRA = "me.annygakh.tamara.lat";
    public final static String LON_EXTRA = "me.annygakh.tamara.lon";
    public final static String IS_NEW_MEAL_EXTRA = "me.annygakh.tamara.is_new_meal";
    public final static String SIMPLE_DATE_EXTRA = "me.annygakh.tamara.simple_date";
    public final static int GPS_REQUEST_CODE = 2;

    private ImageView mMealImageView;
    private Button mSaveButton, mDiscardButton;
    private TextView mRelativeDateTextView, mAbsoluteDateTextView;
    private EditText mLocationEditText, mIngredientsEditText;
    private Meal currentMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_edit_layout);

        mMealImageView = (ImageView) findViewById(R.id.item_edit_pic);
        mRelativeDateTextView = (TextView) findViewById(R.id.relativeDateTextView);
        mAbsoluteDateTextView = (TextView) findViewById(R.id.absoluteDateTextView);
        mLocationEditText = (EditText) findViewById(R.id.locationEditText);
        mLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newLocation = s.toString();
                currentMeal.setLocation(newLocation);
                // save meal

            }
        });
        mIngredientsEditText = (EditText) findViewById(R.id.ingredientsEditText);
        mIngredientsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String newIngredients = s.toString();
                String[] ingredients = newIngredients.split(",");
                currentMeal.setIngredients(new ArrayList<>(Arrays.asList(ingredients)));
            }
        });
        mSaveButton = (Button) findViewById(R.id.saveButton);
        mDiscardButton = (Button) findViewById(R.id.discardButton);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDiscardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealService.getInstance().removeMeal(currentMeal);
                // TODO add a confirmation dialog
                finish();
            }
        });


        // get intent data
        Intent intent = getIntent();
        Uri data = intent.getData();
        String intentType = intent.getType();

        if (intentType != null) {
            currentMeal = createNewMeal(intent, true);
        } else {
            boolean isNewMeal = intent.getBooleanExtra(IS_NEW_MEAL_EXTRA, true);

            if (isNewMeal) {
                currentMeal = createNewMeal(intent, false);
                Log.v(LOG_TAG, "Creating a new meal.");
            } else {
                currentMeal = loadOldMeal(intent);
                Log.v(LOG_TAG, "Updating a new meal.");
            }
        }

        // TODO setup text watchers
        mMealImageView.setImageBitmap(currentMeal.getPhoto());
        mLocationEditText.setText(currentMeal.getLocation());
        mIngredientsEditText.setText(currentMeal.getIngredientsString());
        mAbsoluteDateTextView.setText(new SimpleDateFormat("MMMMMMMM dd, hh:mm aaa").format(currentMeal.getCreatedDate()));
        mRelativeDateTextView.setText(MealService.getInstance().getRelativeDate(currentMeal.getCreatedDate()));

    }

    private Meal loadOldMeal(Intent intent) {
        String simpleDate = intent.getStringExtra(SIMPLE_DATE_EXTRA);
        Meal meal = MealService.getInstance().getMeal(simpleDate);
        return meal;
    }

    private Meal createNewMeal(Intent intent, boolean startedByAnotherApp) {
        String photoPath;
//        double lat, lon;
        if (startedByAnotherApp) {
            photoPath = intent.getClipData().getItemAt(0).getUri().toString();

        } else {
            photoPath = intent.getStringExtra(PHOTO_PATH_EXTRA);

        }
        Bitmap bitmap = loadImage(photoPath);

        Meal meal = new Meal(bitmap);
        meal.setPictureFilePath(photoPath);
        Location location = attemptToGetLocation();
        double lat, lon;
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } else {
            lat = 0.0;
            lon = 0.0;
        }

        meal.setLatLon(lat, lon);

        MealService.getInstance().addMeal(meal);
        return meal;
    }

    public Bitmap loadImage(String photoPath) {
        Bitmap bitmap = null;
        Uri uri = Uri.parse(photoPath);
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Photo was not found.");
            e.printStackTrace();
        }
        return bitmap;

    }

    public Location attemptToGetLocation() {
        String[] perms = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(perms[0]) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(perms[1]) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                requestPermissions(perms, GPS_REQUEST_CODE);
                return null;
            }
        }
        if (lm != null) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // TODO
    }

    public void openMap(View v) {
        double lat = currentMeal.getLat();
        double lon = currentMeal.getLon();
        String geoLink = String.format("geo:<lat>%.8f,%.8f?z=17&q=%.8f,%.8f", lat, lon, lat, lon);
        Uri geoLocation = Uri.parse(geoLink);
        Intent openMapIntent = new Intent(Intent.ACTION_VIEW);
        openMapIntent.setData(geoLocation);
        openMapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (openMapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openMapIntent);
        }
    }
}

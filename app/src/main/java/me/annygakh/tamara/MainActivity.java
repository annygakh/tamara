package me.annygakh.tamara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import me.annygakh.tamara.model.Meal;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    public final static int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View.OnClickListener photoLaunchingCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoLaunchingCallback = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch photo taking activity
                Intent takePhotoIntent = new Intent(MainActivity.this, PhotoTakingActivity.class);
                startActivity(takePhotoIntent);
            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", photoLaunchingCallback).show();
//            }
//        });
        fab.setOnClickListener(photoLaunchingCallback);
        setupAdapter();
    }

    private void setupAdapter() {
        mRecyclerView = (RecyclerView) findViewById(R.id.meals_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Meal> meals = MealService.getInstance().getMeals();
        mAdapter = new CardAdapter(meals);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        CardAdapter adapter = (CardAdapter) mRecyclerView.getAdapter();
        List<Meal> meals = MealService.getInstance().getMeals();
        adapter.setMeals(meals);
        adapter.notifyDataSetChanged();

    }

}

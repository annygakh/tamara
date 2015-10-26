package me.annygakh.tamara;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.annygakh.tamara.model.Meal;

/**
 * Created by annygakhokidze on 10/19/15.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Meal> mMealList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private ImageView mealImageView;
        private ImageView locationImageView;
        private ImageView editImageView;
        private TextView containsTextView;
        private TextView absoluteDateTextView;
        private TextView relativeTextView;

            public ViewHolder(View v) {
                super(v);
                mView = v;
            mealImageView = (ImageView) mView.findViewById(R.id.item_pic);
            locationImageView = (ImageView) mView.findViewById(R.id.location_pic);
            editImageView = (ImageView) mView.findViewById(R.id.edit_pic);
            containsTextView = (TextView) mView.findViewById(R.id.containsTextView);
            absoluteDateTextView = (TextView) mView.findViewById(R.id.absoluteDateTextView);
            relativeTextView = (TextView) mView.findViewById(R.id.relativeDateTextView);
            // TODO

        }

        public void bind(final Meal meal) {
            mealImageView.setImageBitmap(meal.getPhoto());

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context c = v.getContext();
                    Intent intent = new Intent(c, EditMealActivity.class);
//                    intent.putExtra(EditMealActivity.PHOTO_PATH_EXTRA, meal.getPictureFilePath());
                    intent.putExtra(EditMealActivity.SIMPLE_DATE_EXTRA, meal.getSimpleDate());
                    intent.putExtra(EditMealActivity.IS_NEW_MEAL_EXTRA, false);
                    c.startActivity(intent);
                }
            });

            locationImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context c = v.getContext();
                    double lat = meal.getLat();
                    double lon = meal.getLon();
                    String geoLink = String.format("geo:<lat>%.8f,%.8f?z=17&q=%.8f,%.8f", lat, lon, lat, lon);
                    Uri geoLocation = Uri.parse(geoLink);
                    Intent openMapIntent = new Intent(Intent.ACTION_VIEW);
                    openMapIntent.setData(geoLocation);
                    openMapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (openMapIntent.resolveActivity(c.getPackageManager()) != null) {
                        c.startActivity(openMapIntent);
                    }
                }
            });
            containsTextView.setText("Contains: " + meal.getIngredientsString());
            absoluteDateTextView.setText(new SimpleDateFormat("MMMMMMMM dd, hh:mm aaa").format(meal.getCreatedDate()));
            relativeTextView.setText(MealService.getInstance().getRelativeDate(meal.getCreatedDate()));
        }
    }

    public CardAdapter(List<Meal> mealList) {
        mMealList = mealList;
        notifyDataSetChanged();
    }

    public void setMeals(List<Meal> meals) {
        mMealList = meals;
        notifyDataSetChanged();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView)LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meal_item_layout, parent, false);
        // set view size margins padding and layout params
        v.setCardElevation(10);

        ViewHolder vh = new ViewHolder((View) v);
        return vh;
    }

    // replace contents of a view (invoked by the layout manager(
    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        Meal meal = mMealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mMealList.size();
    }
}

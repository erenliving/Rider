package com.erenlivingstone.rider.appscreens.card;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.utils.Utils;

import java.util.List;
import java.util.Random;

/**
 * Created by eren on 2016-12-14.
 */

public class SwipeDeckAdapter extends BaseAdapter {

    private List<Station> data;
    private Context context;

    public SwipeDeckAdapter(List<Station> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Normally use a ViewHolder
            view = inflater.inflate(R.layout.card_bikes, parent, false);
        }

        TextView mStationNameTextView = (TextView) view.findViewById(R.id.station_name_text_view);
        ImageView thumbnailImageView = (ImageView) view.findViewById(R.id.thumbnail_image_view);
        TextView mAvailableBikesTextView = (TextView) view.findViewById(R.id.available_bikes_text_view);
        TextView mDistanceTextView = (TextView) view.findViewById(R.id.distance_text_view);
        TextView mLastUpdatedTextView = (TextView) view.findViewById(R.id.last_updated_text_view);

        Station station = data.get(position);

        mStationNameTextView.setText(station.getStationName());
        thumbnailImageView.setImageDrawable(getRandomBikeDrawable(context));
        mAvailableBikesTextView.setText(String.format(context.getString(R.string.available_bikes)
                , station.getAvailableBikes()));
        mDistanceTextView.setText(String.valueOf(Utils.getFormattedDistanceForDisplay(context,
                station.getDistance())));
        mLastUpdatedTextView.setText(String.format(context.getString(R.string.last_updated), station
                .getLastUpdated()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
            }
        });
        return view;
    }

    private Drawable getRandomBikeDrawable(Context context) {
        // Formula for random num between 1 and 6 inclusive: ((max - min + 1) + min)
        switch (new Random().nextInt(6 - 1 + 1) + 1) {
            case 1:
                return ContextCompat.getDrawable(context, R.drawable.bike1);
            case 2:
                return ContextCompat.getDrawable(context, R.drawable.bike2);
            case 3:
                return ContextCompat.getDrawable(context, R.drawable.bike3);
            case 4:
                return ContextCompat.getDrawable(context, R.drawable.bike4);
            case 5:
                return ContextCompat.getDrawable(context, R.drawable.bike5);
            case 6:
                return ContextCompat.getDrawable(context, R.drawable.bike6);
            default:
                return ContextCompat.getDrawable(context, R.drawable.bike1);
        }
    }

}

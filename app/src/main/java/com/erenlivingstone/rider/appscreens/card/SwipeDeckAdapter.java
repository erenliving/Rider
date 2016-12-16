package com.erenlivingstone.rider.appscreens.card;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.utils.Utils;

import java.util.List;

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
        TextView mAvailableBikesTextView = (TextView) view.findViewById(R.id.available_bikes_text_view);
        TextView mDistanceTextView = (TextView) view.findViewById(R.id.distance_text_view);
        TextView mLastUpdatedTextView = (TextView) view.findViewById(R.id.last_updated_text_view);

        Station station = data.get(position);

        mStationNameTextView.setText(station.getStationName());
        mAvailableBikesTextView.setText(station.getAvailableBikes() + " available bikes");
        mDistanceTextView.setText(String.valueOf(Utils.getFormattedDistanceForDisplay(station
                .getDistance())));
        mLastUpdatedTextView.setText("Last updated: " + station.getLastUpdated());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
            }
        });
        return view;
    }

}

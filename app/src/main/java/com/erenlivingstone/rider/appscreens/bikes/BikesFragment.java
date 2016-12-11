package com.erenlivingstone.rider.appscreens.bikes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.data.model.Station;

/**
 * A {@link Fragment} subclass containing a CardView with
 * information on the nearest bikes to the search location.
 *
 * Activities that contain this fragment must implement the
 * {@link BikesFragment.OnBikesFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link BikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BikesFragment extends Fragment implements BikesContract.View {

    public static final String TAG = BikesFragment.class.getSimpleName();

    private BikesContract.Presenter mPresenter;

    public interface OnBikesFragmentInteractionListener {
        void onBikesFragmentInteraction(boolean selectBike);
    }

    private OnBikesFragmentInteractionListener mListener;

    private CardView mCardView;
    private TextView mLocationTextView, mNumOfBikesTextView, mLastUpdatedTextView;

    public BikesFragment() {
        // Required empty public constructor
    }

    public static BikesFragment newInstance() {
        return new BikesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bikes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mLocationTextView = (TextView) view.findViewById(R.id.location_text_view);
        mNumOfBikesTextView = (TextView) view.findViewById(R.id.number_of_bikes_text_view);
        mLastUpdatedTextView = (TextView) view.findViewById(R.id.last_updated_text_view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBikesFragmentInteractionListener) {
            mListener = (OnBikesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBikesFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onCardSwipe(boolean toRight) {
        if (mListener != null) {
            mListener.onBikesFragmentInteraction(false);
        }
    }

    //region BikesContract.View methods

    @Override
    public void setPresenter(BikesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStationCard(Station station) {
        mLocationTextView.setText(station.getLocation().toString());
        mNumOfBikesTextView.setText(String.valueOf(station.getAvailableBikes()));
        mLastUpdatedTextView.setText(station.getLastCommunicationTime());
    }

    //endregion
}

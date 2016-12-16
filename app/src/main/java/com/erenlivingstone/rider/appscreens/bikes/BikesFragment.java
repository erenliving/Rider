package com.erenlivingstone.rider.appscreens.bikes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.erenlivingstone.rider.R;

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

    private SwipeDeck mSwipeDeck;
    private ProgressBar mIndeterminateProgressBar;

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
        mSwipeDeck = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        // Let the Presenter handle configuring SwipeDeck views
        mSwipeDeck.setAdapter(mPresenter.getSwipeDeckAdapter());
        // Forward events to Presenter
        mSwipeDeck.setCallback((BikesPresenter)mPresenter);
        mIndeterminateProgressBar = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
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

    //region BikesContract.View methods

    @Override
    public void setPresenter(BikesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Method to display or hide the indeterminate progress bar indicating loading first Station
     *
     * @param active true to display indicator, false to hide indicator
     */
    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mSwipeDeck.setVisibility(View.GONE);
            mIndeterminateProgressBar.setVisibility(View.VISIBLE);
        } else {
            mSwipeDeck.setVisibility(View.VISIBLE);
            mIndeterminateProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    //endregion

}

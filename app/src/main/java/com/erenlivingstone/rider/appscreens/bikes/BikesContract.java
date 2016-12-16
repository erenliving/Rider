package com.erenlivingstone.rider.appscreens.bikes;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.appscreens.card.SwipeDeckAdapter;

/**
 * Created by Eren on 10/12/2016.
 */

public interface BikesContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showToast(String text);

    }

    interface Presenter extends BasePresenter {

        SwipeDeckAdapter getSwipeDeckAdapter();

    }

}

package com.erenlivingstone.rider.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by eren on 2016-12-18.
 */

public class ResizeAnimation extends Animation {

    private int startWidth;
    private int deltaWidth; // distance between start and end height
    private int startHeight;
    private int deltaHeight;

    private int originalWidth;
    private int originalHeight;
    private View view;

    private boolean fillEnabled = false;

    public ResizeAnimation(View v, int startW, int endW, int startH, int endH) {
        view = v;
        originalHeight = v.getHeight();
        originalWidth = v.getWidth();
        startWidth = startW;
        deltaWidth = endW - startW;
        startHeight = startH;
        deltaHeight = endH - startH;
    }

    @Override
    public void setFillEnabled(boolean enabled) {
        fillEnabled = enabled;
        super.setFillEnabled(enabled);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (interpolatedTime == 1.0 && !fillEnabled) {
            view.getLayoutParams().height = originalHeight;
            view.getLayoutParams().width = originalWidth;
        } else {
            if (deltaHeight != 0) {
                view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
            }
            if (deltaWidth != 0) {
                view.getLayoutParams().width = (int) (startWidth + deltaWidth * interpolatedTime);
            }
        }

        view.requestLayout();
    }

}

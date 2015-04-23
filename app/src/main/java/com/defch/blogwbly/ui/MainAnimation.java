package com.defch.blogwbly.ui;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by DiegoFranco on 4/23/15.
 */
public class MainAnimation extends Animation {

    private View v;
    private int h;

    public MainAnimation(View v, int targetHeight) {
        this.v = v;
        this.h = targetHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        v.getLayoutParams().height = (int) (h * interpolatedTime);
        v.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }
}

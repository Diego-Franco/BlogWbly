package com.defch.blogwbly.listener;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.DragShadowBuilder;

/**
 * Created by DiegoFranco on 4/14/15.
 */
public class CustomTouchListener implements OnTouchListener {
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}


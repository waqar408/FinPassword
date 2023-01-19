package com.zb.finpassword.utils;

import android.view.MotionEvent;
import android.view.View;

public abstract class HorizontalSwipeListener implements View.OnTouchListener {

    private float firstX;
    private int minDistance;

    HorizontalSwipeListener(int minDistance) {
        this.minDistance = minDistance;
    }

    abstract void onSwipeRight();

    abstract void onSwipeLeft();

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = event.getX();
                return true;
            case MotionEvent.ACTION_UP:
                float secondX = event.getX();
                if (Math.abs(secondX - firstX) > minDistance) {
                    if (secondX > firstX) {
                        onSwipeLeft();
                    } else {
                        onSwipeRight();
                    }
                }
                return true;
        }
        return view.performClick();
    }

}
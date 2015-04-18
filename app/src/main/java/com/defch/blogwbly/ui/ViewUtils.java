package com.defch.blogwbly.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;


/**
 * Created by kcampagna on 7/27/14.
 */
public class ViewUtils {

    /**
     * Returns the height of the actionbar and status bar (4.4+) needed for the translucent style
     *
     * @param context
     * @return
     */
    public static int getHeightForTranslucentStyle(Context context) {
        return getActionBarHeight(context) + getStatusBarHeight(context);
    }

    /**
     * Returns the height of the actionbar
     *
     * @param context
     * @return
     */
    public static int getActionBarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.support.v7.appcompat.R.attr.actionBarSize});

        int abHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return abHeight;
    }


    /**
     * Returns the height of the navigation bar
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }

        return 0;
    }

    /**
     * Returns the height of the status bar
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int height = 0;

        // On 4.4 + devices, we need to account for the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = context.getResources().getDimensionPixelSize(resourceId);
            }
        }

        return height;
    }



}

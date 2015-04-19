package com.defch.blogwbly.ui;

import com.defch.blogwbly.R;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public enum ContainerLayout {
    CONTAINER1(0, R.layout.container_1),
    CONTAINER2(1, R.layout.container_2),
    CONTAINER3(2, R.layout.container_3),
    CONTAINER4(3, R.layout.container_4),
    CONTAINER5(4, R.layout.container_5);

    public final int key;
    public final int layoutId;

    ContainerLayout(int key, int layoutId) {
        this.key = key;
        this.layoutId = layoutId;
    }

    public static ContainerLayout getLatyout(int key) {
        ContainerLayout containerL = null;
        switch (key) {
            case 0:
                containerL = CONTAINER1;
                break;
            case 1:
                containerL = CONTAINER2;
                break;
            case 2:
                containerL = CONTAINER3;
                break;
            case 3:
                containerL = CONTAINER4;
                break;
            case 4:
                containerL = CONTAINER5;
                break;
        }
        return containerL;
    }
}

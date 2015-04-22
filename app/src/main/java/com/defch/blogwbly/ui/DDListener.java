package com.defch.blogwbly.ui;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.defch.blogwbly.ifaces.PostInterfaces;

/**
 * Created by DiegoFranco on 4/21/15.
 */
public class DDListener implements View.OnTouchListener{

    private int modx = 50;
    private int mody = 50;

    private long currentTime;
    private long finishTime = 2200l;

    private PostInterfaces postInterfaces;

    private FrameLayout frameLayout;
    private View vi;

    public DDListener(FrameLayout fm, View view) {
        this.frameLayout = fm;
        this.vi = view;
        vi.setOnTouchListener(this);
    }

    public DDListener(FrameLayout fm, View v, PostInterfaces postInterfaces) {
        this.frameLayout = fm;
        this.vi = v;
        this.postInterfaces = postInterfaces;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PointF downPt = new PointF();
        PointF startPt;
        FrameLayout.LayoutParams par = (FrameLayout.LayoutParams) v.getLayoutParams();

                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        startPt = new PointF(vi.getX(), vi.getY());
                        PointF mv = new PointF(event.getX() - downPt.x, event.getY() - downPt.y);

                        vi.setX((startPt.x + mv.x) - modx);
                        vi.setY((startPt.y + mody) - mody);
                        par.topMargin = (int)event.getRawY() - (v.getHeight());
                        par.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
                        v.setLayoutParams(par);
                        currentTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        //onlong click implementation
                        long upLong = System.currentTimeMillis() - currentTime;
                        if(upLong >= finishTime) {
                            postInterfaces.clickTextToEdit((EditText) v);
                            postInterfaces.longClickedOnEditText(true, (EditText) v);
                        }
                        downPt.x = event.getX();
                        downPt.y = event.getY();
                        par.topMargin = (int)event.getRawY() - (v.getHeight());
                        par.leftMargin = (int)event.getRawX() - (v.getWidth()/2);
                        v.setLayoutParams(par);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        v.setLayoutParams(par);
                        currentTime = System.currentTimeMillis();
                        break;

                }
        return true;
    }
}

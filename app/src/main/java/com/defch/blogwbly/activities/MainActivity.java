package com.defch.blogwbly.activities;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.defch.blogwbly.R;
import com.defch.blogwbly.adapters.AdapterBlogList;
import com.defch.blogwbly.adapters.LayoutsViewAdapter;

import org.lucasr.twowayview.widget.GridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.Optional;

public class MainActivity extends BaseActivity {

    @Optional
    @InjectView(R.id.main_img_empty)
    ImageView emptyImg;

    @InjectView(R.id.my_recycler_view)
    TwoWayView recyclerView;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    private ArrayList<Bitmap> bitmaps;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        setupToolbar();
        recyclerView.setOnScrollListener(scrollListener);
        if(ismIsTablet()) {
            ((GridLayoutManager)recyclerView.getLayoutManager()).setNumRows((mAdapter.getItemCount() / 2) + 1);
        }
        new LoadLayoutsViewTask().execute();
        loadIfExistPosts();
    }

    private void loadIfExistPosts() {
        if(app.getPosts().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.GONE);
            mAdapter = new AdapterBlogList(MainActivity.this, app.getPosts());
            recyclerView.setAdapter(mAdapter);
        } else {
            emptyImg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupToolbar() {
        setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        setSupportActionBar(mToolBar);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            float y = recyclerView.getY();
            if (y < dy ) {
                mToolBar.animate()
                        .translationY(-mToolBar.getBottom())
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            } else {
                mToolBar.animate()
                        .translationY(0)
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                createDialogViews();
                break;
            case R.id.action_settings:
                newIntent(SettingsActivity.class);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialogViews() {
        MaterialDialog.Builder dialogB = new MaterialDialog.Builder(this);
        dialogB.theme(Theme.DARK);
        dialogB.title(R.string.select_view);
        dialogB.adapter(new LayoutsViewAdapter(getApplicationContext(), bitmaps), new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                newIntent(PostActivity.class, PostActivity.PostValue.CREATE, which);
                dialog.dismiss();
            }
        });
        MaterialDialog dialog = dialogB.build();
        dialog.setActionButton(DialogAction.NEGATIVE, R.string.close);

        dialog.show();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreateView();
    }

    private void recreateView() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    finish();
                    startActivity(MainActivity.this.getIntent());
                } else {
                    recreate();
                }
            }
        }, 1);
    }

    private class LoadLayoutsViewTask extends  AsyncTask<Void,Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(Void... params) {
            bitmaps = new ArrayList<>();

            TypedArray ar = getResources().obtainTypedArray(R.array.view_arrays);
            int len = ar.length();
            int[] resIds = new int[len];
            for (int i = 0; i < len; i++) {
                resIds[i] = ar.getResourceId(i, 0);
            }


            for(int i = 0; i < resIds.length; i++) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), resIds[i]);
                if(bmp != null) {
                    bitmaps.add(bmp);
                }
            }
            ar.recycle();
            return bitmaps;
        }
    }

}

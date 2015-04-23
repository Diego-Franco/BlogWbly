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
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.defch.blogwbly.R;
import com.defch.blogwbly.adapters.AdapterBlogList;
import com.defch.blogwbly.adapters.LayoutsViewAdapter;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.HidingScrollListener;
import com.defch.blogwbly.util.FileUtil;
import com.defch.blogwbly.util.LogUtil;

import org.lucasr.twowayview.widget.GridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.Optional;

public class MainActivity extends BaseActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String MESSAGE = "msg";

    @Optional
    @InjectView(R.id.main_img_empty)
    ImageView emptyImg;

    @InjectView(R.id.layout)
    LinearLayout layout;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    private TwoWayView recyclerView;
    private RecyclerView supporRecyclerView;

    private ArrayList<Bitmap> bitmaps;

    private RecyclerView.Adapter mAdapter;
    private ArrayList<BlogPost> blogPostArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        setupToolbar();
        new LoadLayoutsViewTask().execute();
        getPostFromDB();
        checkAndroidVersionAndPutLayout();
        if(getIntent().getAction() != null) {
            if(getIntent().getAction().equalsIgnoreCase(MESSAGE)) {
                LogUtil.v(TAG, MESSAGE);
                Toast.makeText(this, R.string.msg_voice, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAndroidVersionAndPutLayout() {
        if(ismIsTablet()) {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                supporRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
                supporRecyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new android.support.v7.widget.GridLayoutManager(this, 2);
                supporRecyclerView.setLayoutManager(layoutManager);
                supporRecyclerView.setOnScrollListener(scrollListener);
            } else {
                recyclerView = (TwoWayView)findViewById(R.id.my_recycler_view);
                recyclerView.setHasFixedSize(true);
                ((GridLayoutManager) recyclerView.getLayoutManager()).setNumRows((mAdapter.getItemCount() / 2) + 1);
                recyclerView.setOnScrollListener(scrollListener);
            }
        } else {
            recyclerView = (TwoWayView)findViewById(R.id.my_recycler_view);
            recyclerView.setOnScrollListener(scrollListener);
        }
    }

    private void getPostFromDB() {
        for(int i = 0; i < app.getPosts().size(); i++) {
            BlogPost p = app.getPosts().get(i);
            new LoadThumbnailsFromPost(p, i).execute();
        }
    }

    private void setupToolbar() {
        setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        setSupportActionBar(mToolBar);
    }

    HidingScrollListener scrollListener = new HidingScrollListener() {

        @Override
        public void onHide() {
            mToolBar.animate().translationY(-mToolBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            layout.animate().translationY(-mToolBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        }

        @Override
        public void onShow() {
            mToolBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
            layout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
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
                MainActivity.this.finish();
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

    private class LoadThumbnailsFromPost extends AsyncTask<Void, Void, Void> {

        BlogPost blogPost;
        int pos;

        public LoadThumbnailsFromPost(BlogPost blogPost, int position) {
            this.blogPost = blogPost;
            this.pos = position;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<Bitmap> pics = FileUtil.getImagesFromFolder(blogPost.getId());
            blogPost.setThumbnails(pics);
            blogPostArrayList.add(blogPost);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(blogPostArrayList.size() > 0) {
                emptyImg.setVisibility(View.GONE);
                if(mAdapter == null) {
                    mAdapter = new AdapterBlogList(MainActivity.this, blogPostArrayList);
                    if(supporRecyclerView != null) {
                        supporRecyclerView.setVisibility(View.VISIBLE);
                        supporRecyclerView.setAdapter(mAdapter);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                emptyImg.setVisibility(View.VISIBLE);
                if(supporRecyclerView != null) {
                    supporRecyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }

}

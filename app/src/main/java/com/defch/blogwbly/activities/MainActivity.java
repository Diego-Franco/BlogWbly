package com.defch.blogwbly.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.adapters.AdapterBlogList;
import com.defch.blogwbly.model.BlogPost;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.Optional;


public class MainActivity extends BaseActivity {

    @Optional
    @InjectView(R.id.main_img_empty)
    ImageView emptyImg;

    @InjectView(R.id.my_recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);
        setupToolbar();

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        new LoadPostTask().execute();

    }

    private void setupToolbar() {
        setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        setSupportActionBar(mToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_add:
                newIntent(PostActivity.class, PostActivity.PostValue.CREATE);
                break;
            case R.id.action_settings:
                newIntent(SettingsActivity.class);
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    private class LoadPostTask extends AsyncTask<Void, Void, ArrayList<BlogPost>> {
        @Override
        protected ArrayList<BlogPost> doInBackground(Void... params) {
            ArrayList<BlogPost> postArrayList = null;
            postArrayList = app.getSql().getPosts();
            return postArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<BlogPost> blogPostArrayList) {
            super.onPostExecute(blogPostArrayList);
            if(blogPostArrayList != null && blogPostArrayList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyImg.setVisibility(View.GONE);
                mAdapter = new AdapterBlogList(blogPostArrayList);
                recyclerView.setAdapter(mAdapter);
            } else {
                emptyImg.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }
}

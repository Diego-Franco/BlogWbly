package com.defch.blogwbly.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.defch.blogwbly.R;
import com.defch.blogwbly.fragments.SettingsFragment;

import butterknife.InjectView;


/**
 * Created by DiegoFranco on 4/16/15.
 */
public class SettingsActivity extends BaseActivity {

    public static final String THEME_KEY = "app_theme";
    public static final String KEY_DARK_THEME = "darkTheme";

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_activity);
        setupToolbar();
        getFragmentManager().beginTransaction().add(R.id.container, SettingsFragment.createInstance()).commit();
        overridePendingTransition(R.anim.appear, R.anim.disappear);
    }

    private void setupToolbar() {
        setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void recreate() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment != null) getFragmentManager().beginTransaction().remove(fragment).commit();
        super.recreate();
    }

}

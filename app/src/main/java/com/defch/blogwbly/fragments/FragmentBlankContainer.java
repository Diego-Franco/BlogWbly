package com.defch.blogwbly.fragments;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.defch.blogwbly.R;

/**
 * Created by DiegoFranco on 4/21/15.
 */
public class FragmentBlankContainer extends FragmentContainerBase {


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_blank_container, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_title:
                break;
            case R.id.action_add_content:
                break;
            case R.id.action_add_images:
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

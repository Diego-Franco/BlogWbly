package com.defch.blogwbly.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.PostActivity;
import com.defch.blogwbly.ui.ContainerLayout;
import com.defch.blogwbly.util.LogUtil;

import org.lucasr.twowayview.widget.TwoWayView;

import butterknife.InjectView;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class FragmentContainer extends FragmentContainerBase {

    private static final String TAG = FragmentContainer.class.getSimpleName();

    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;
    @InjectView(R.id.post_textview_title)
    TextView title;
    @InjectView(R.id.post_textview_text)
    TextView content;
    @InjectView(R.id.list)
    TwoWayView pictureList;

    private MenuItem menuItemEdit, menuItemSave;

    private int keyLayout;
    private ContainerLayout containerLayout;
    private PostActivity.PostValue postValue;


    public static FragmentContainer createInstance(int keyLayout, PostActivity.PostValue postValue) {
        FragmentContainer fragmentContainer = new FragmentContainer();

        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, keyLayout);
        args.putSerializable(POST_VALUE, postValue);
        fragmentContainer.setArguments(args);

        return fragmentContainer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.keyLayout = getArguments().getInt(KEY_LAYOUT);
        this.containerLayout = ContainerLayout.getLatyout(keyLayout);
        this.postValue = (PostActivity.PostValue) getArguments().getSerializable(POST_VALUE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        LogUtil.v(TAG, Integer.toString(containerLayout.key));
        return inflater.inflate(containerLayout.layoutId, container, false);
    }

    private void setupToolbar() {
        ((PostActivity)getActivity()).setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        ((PostActivity)getActivity()).setSupportActionBar(mToolBar);
        ((PostActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((PostActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_post, menu);
        menuItemEdit = menu.findItem(R.id.action_edit);
        menuItemSave = menu.findItem(R.id.action_save);
        if(postValue == PostActivity.PostValue.VIEW) {
            menuItemEdit.setVisible(true);
            menuItemSave.setVisible(false);
        } else if(postValue == PostActivity.PostValue.CREATE || postValue == PostActivity.PostValue.EDIT) {
            menuItemEdit.setVisible(false);
            menuItemSave.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_save:
                //save the post
                menuItemEdit.setVisible(true);
                break;
            case R.id.action_edit:

                menuItemSave.setVisible(true);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

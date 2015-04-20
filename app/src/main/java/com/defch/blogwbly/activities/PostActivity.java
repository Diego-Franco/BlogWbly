package com.defch.blogwbly.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.defch.blogwbly.R;
import com.defch.blogwbly.fragments.FragmentContainer;
import com.defch.blogwbly.ifaces.IfaceSnapMap;
import com.defch.blogwbly.ifaces.PostInterfaces;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.FloatingButton;
import com.defch.blogwbly.ui.ViewUtils;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class PostActivity extends BaseActivity implements View.OnClickListener, IfaceSnapMap, PostInterfaces {

    private static final String FRAGMENT_TAG = "fragment_container";
    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;

    @InjectView(R.id.mtoolbar_bottom)
    Toolbar toolbarBottom;
    @InjectView(R.id.floatMenu)
    View mFloatMenu;
    @InjectView(R.id.float_video_btn)
    FloatingButton videoButton;
    @InjectView(R.id.float_camera_btn)
    FloatingButton cameraButton;
    @InjectView(R.id.float_gallery_btn)
    FloatingButton galleryButton;
    @InjectView(R.id.float_map_btn)
    FloatingButton mapButton;
    @InjectView(R.id.float_add_btn)
    FloatingButton addButton;

    //float button values
    private float mUploadButtonHeight;
    private float mUploadMenuButtonHeight;
    private int mNavBarHeight = -1;
    private boolean floatMenuOpen = false;
    private boolean floatMenuShowing = false;
    private boolean bottomToolbarShowing = false;

    private int viewIndex;
    private PostValue pValue;
    private BlogPost post;

    private FragmentContainer fragmentContainer;

    public enum PostValue {
        VIEW, EDIT, CREATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_activity);
        Resources res = getResources();
        int accentColor = res.getColor(theme.accentColor);
        addButton.setColor(accentColor);
        videoButton.setColor(accentColor);
        cameraButton.setColor(accentColor);
        galleryButton.setColor(accentColor);
        mapButton.setColor(accentColor);
        mUploadButtonHeight = getResources().getDimension(R.dimen.f_button_radius);
        mUploadMenuButtonHeight = getResources().getDimension(R.dimen.f_button_radius_smaller);

        pValue = (PostValue) getIntent().getSerializableExtra(POST_VALUE);
        viewIndex = getIntent().getIntExtra(KEY_LAYOUT, Integer.MIN_VALUE);
        post = getIntent().getParcelableExtra(POST_OBJECT);
        if(post != null) {
            fragmentContainer = FragmentContainer.createInstance(viewIndex, pValue, post);
            getFragmentManager().beginTransaction().replace(R.id.container_views, fragmentContainer, FRAGMENT_TAG).commit();
        } else {
            fragmentContainer = FragmentContainer.createInstance(viewIndex, pValue);
            getFragmentManager().beginTransaction().replace(R.id.container_views, fragmentContainer, FRAGMENT_TAG).commit();
        }
        fragmentContainer.setPostInterfaces(this);
    }

    @OnClick({R.id.float_add_btn, R.id.float_video_btn, R.id.float_camera_btn, R.id.float_gallery_btn, R.id.float_map_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_add_btn:
                animateFloatingMenu();
                break;
            case R.id.float_video_btn:
                    startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE), VIDEO_REQUEST);
                animateFloatingMenu();
                break;
            case R.id.float_camera_btn:
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
                animateFloatingMenu();
                break;
            case R.id.float_gallery_btn:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/* video/*");
                startActivityForResult(i, GALLERY_REQUEST);
                animateFloatingMenu();
                break;
            case R.id.float_map_btn:
                app.setIfaceSnapMap(this);
                newIntent(MapsActivity.class);
                animateFloatingMenu();
                break;
        }
    }

    private void animateFloatingMenu() {
        AnimatorSet set = new AnimatorSet().setDuration(500L);
        String translation = isLandscape() ? "translationX" : "translationY";

        if (!floatMenuOpen) {
            floatMenuOpen = true;

            set.playTogether(
                    ObjectAnimator.ofFloat(mapButton, translation, 0, (mUploadButtonHeight + 25) * -1),
                    ObjectAnimator.ofFloat(videoButton, translation, 0, (mUploadMenuButtonHeight + mUploadButtonHeight + 50) * -1),
                    ObjectAnimator.ofFloat(galleryButton, translation, 0, ((2 * mUploadMenuButtonHeight) + mUploadButtonHeight + 75)  * -1),
                    ObjectAnimator.ofFloat(cameraButton, translation, 0, ((3 * mUploadMenuButtonHeight) + mUploadButtonHeight + 100) * -1),

                    ObjectAnimator.ofFloat(videoButton, "alpha", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(galleryButton, "alpha", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(cameraButton, "alpha", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(mapButton, "alpha", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(addButton, "rotation", 0.0f, 135.0f)
            );

            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    videoButton.setVisibility(View.VISIBLE);
                    galleryButton.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);
                    mapButton.setVisibility(View.VISIBLE);
                    animation.removeAllListeners();
                }
            });

            set.setInterpolator(new OvershootInterpolator());
            set.start();
        } else {
            floatMenuOpen = false;

            set.playTogether(
                    ObjectAnimator.ofFloat(videoButton, translation, 0),
                    ObjectAnimator.ofFloat(cameraButton, translation, 0),
                    ObjectAnimator.ofFloat(galleryButton, translation, 0),
                    ObjectAnimator.ofFloat(mapButton, translation, 0),
                    ObjectAnimator.ofFloat(videoButton, "alpha", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(galleryButton, "alpha", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(cameraButton, "alpha", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(mapButton, "alpha", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(addButton, "rotation", 135.0f, 0.0f)
            );

            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    videoButton.setVisibility(View.GONE);
                    galleryButton.setVisibility(View.GONE);
                    cameraButton.setVisibility(View.GONE);
                    mapButton.setVisibility(View.GONE);
                    animation.removeAllListeners();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    videoButton.setVisibility(View.GONE);
                    galleryButton.setVisibility(View.GONE);
                    cameraButton.setVisibility(View.GONE);
                    mapButton.setVisibility(View.GONE);
                    animation.removeAllListeners();
                }
            });

            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.start();
        }
    }

    //you can hide or show float button
    private void animateFloatingMenuButton(boolean shouldShow) {
        if (!shouldShow && floatMenuShowing) {
            float hideDistance;
            floatMenuShowing = false;
            hideDistance = mUploadButtonHeight + (mUploadButtonHeight / 2);
            // Add extra distance to the hiding of the button if on KitKat due to the translucent nav bar
            if (app.sdkVersion >= Build.VERSION_CODES.KITKAT) {
                if (mNavBarHeight == -1)
                    mNavBarHeight = ViewUtils.getNavigationBarHeight(getApplicationContext());
                hideDistance += mNavBarHeight;
            }

            mFloatMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(hideDistance).setDuration(350).start();
            // Close the menu if it is open
            if (floatMenuOpen) animateFloatingMenu();
        } else if (shouldShow && !floatMenuShowing) {
            floatMenuShowing = true;
            mFloatMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(350).start();
        }
    }

    private void animateBottomToolbar() {
        AnimatorSet set = new AnimatorSet().setDuration(500L);
        if(bottomToolbarShowing) {
            //hide toolbar
            set.playTogether(
                    ObjectAnimator.ofFloat(toolbarBottom, "scaleY", 1.0f, 0.0f),
                    ObjectAnimator.ofFloat(addButton, "scaleY", 0.0f, 1.0f)
            );

            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationStart(animation);
                    addButton.setVisibility(View.VISIBLE);
                    toolbarBottom.setVisibility(View.GONE);
                    animation.removeAllListeners();
                }
            });

            set.setInterpolator(new OvershootInterpolator());
            set.start();
            bottomToolbarShowing = false;
        } else {
            //show toolbar
            if (floatMenuOpen) animateFloatingMenu();
            set.playTogether(
                    ObjectAnimator.ofFloat(toolbarBottom, "scaleY", 0.0f, 1.0f),
                    ObjectAnimator.ofFloat(addButton, "scaleY", 1.0f, 0.0f)
            );

            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    addButton.setVisibility(View.GONE);
                    toolbarBottom.setVisibility(View.VISIBLE);
                    animation.removeAllListeners();
                    inflateMenuOnBottomToolbar();
                }
            });
            set.setInterpolator(new OvershootInterpolator());
            set.start();
            bottomToolbarShowing = true;
        }

    }

    private void inflateMenuOnBottomToolbar() {
        toolbarBottom = (Toolbar) findViewById(R.id.mtoolbar_bottom);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // TODO implement actions for bottom toolbar
                    case R.id.action_text:
                        break;
                    case R.id.action_ok:
                        //TODO apply changes method
                        animateBottomToolbar();
                        break;
                }
                return true;
            }
        });
        toolbarBottom.getMenu().clear();
        toolbarBottom.inflateMenu(R.menu.menu_bottom_toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void takeSnapMap(Bitmap bitmap) {
        Fragment fragment = getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        ((FragmentContainer)fragment).setSnapMap(bitmap);
    }

    @Override
    public void clickTextToEdit(int id) {
        switch (id) {
            case R.id.post_textview_title:
            case R.id.post_textview_text:
                animateBottomToolbar();
                break;
        }
    }

}

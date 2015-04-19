package com.defch.blogwbly.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.defch.blogwbly.R;
import com.defch.blogwbly.adapters.AdapterPostPictures;
import com.defch.blogwbly.fragments.FragmentContainer;
import com.defch.blogwbly.ifaces.IfaceSnapMap;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.BlogPictureView;
import com.defch.blogwbly.ui.FloatingButton;
import com.defch.blogwbly.ui.ViewUtils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by DiegoFranco on 4/17/15.
 */
public class PostActivity extends BaseActivity implements View.OnClickListener, IfaceSnapMap{

    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;

    @InjectView(R.id.floatMenu)
    View mUploadMenu;
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
    private boolean uploadMenuOpen = false;
    private boolean uploadMenuShowing = false;

    private AdapterPostPictures adapterPictures;
    private ArrayList<BlogPictureView> pictures;

    private int viewIndex;
    private PostValue pValue;
    //TODO save the viewLayoutId on db
    private BlogPost post;

    public enum PostValue {
        VIEW, EDIT, CREATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_activity);
        pictures = new ArrayList<>();
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
        getFragmentManager().beginTransaction().replace(R.id.container_views, FragmentContainer.createInstance(viewIndex,pValue)).commit();

    }

    @OnClick({R.id.float_add_btn, R.id.float_video_btn, R.id.float_camera_btn, R.id.float_gallery_btn, R.id.float_map_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_add_btn:
                animateUploadMenu();
                break;
            case R.id.float_video_btn:
                    startActivityForResult(new Intent(MediaStore.ACTION_VIDEO_CAPTURE), VIDEO_REQUEST);
                animateUploadMenu();
                break;
            case R.id.float_camera_btn:
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
                animateUploadMenu();
                break;
            case R.id.float_gallery_btn:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/* video/*");
                startActivityForResult(i, GALLERY_REQUEST);
                animateUploadMenu();
                break;
            case R.id.float_map_btn:
                app.setIfaceSnapMap(this);
                newIntent(MapsActivity.class);
                animateUploadMenu();
                break;
        }
    }

    /**
     * Animates the opening/closing of the Upload button
     */
    private void animateUploadMenu() {
        AnimatorSet set = new AnimatorSet().setDuration(500L);
        String translation = isLandscape() ? "translationX" : "translationY";

        if (!uploadMenuOpen) {
            uploadMenuOpen = true;

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
            uploadMenuOpen = false;

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
    private void animateUploadMenuButton(boolean shouldShow) {
        if (!shouldShow && uploadMenuShowing) {
            float hideDistance;
            uploadMenuShowing = false;
            hideDistance = mUploadButtonHeight + (mUploadButtonHeight / 2);
            // Add extra distance to the hiding of the button if on KitKat due to the translucent nav bar
            if (app.sdkVersion >= Build.VERSION_CODES.KITKAT) {
                if (mNavBarHeight == -1)
                    mNavBarHeight = ViewUtils.getNavigationBarHeight(getApplicationContext());
                hideDistance += mNavBarHeight;
            }

            mUploadMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(hideDistance).setDuration(350).start();
            // Close the menu if it is open
            if (uploadMenuOpen) animateUploadMenu();
        } else if (shouldShow && !uploadMenuShowing) {
            uploadMenuShowing = true;
            mUploadMenu.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(350).start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BlogPictureView pictureView = new BlogPictureView(getApplicationContext());
        Bitmap bmp;
        if(data != null) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        bmp = (Bitmap) extras.get("data");
                        pictureView.setPicture(bmp);
                        pictures.add(pictureView);
                        setAdapter();
                    }
                    break;
                case VIDEO_REQUEST:
                    if (resultCode == RESULT_OK) {
                        Uri videoUri = data.getData();
                        pictureView.setVideo(videoUri);
                        pictures.add(pictureView);
                        setAdapter();
                    }
                    break;
                case GALLERY_REQUEST:
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri.toString().contains("images")) {
                        bmp = loadImage(selectedMediaUri);
                        pictureView.setPicture(bmp);
                    } else if (selectedMediaUri.toString().contains("video")) {
                        pictureView.setVideo(selectedMediaUri);
                    }
                    pictures.add(pictureView);
                    setAdapter();
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAdapter() {
        if(pictures != null && pictures.size() > 0) {
            if(adapterPictures == null) {
                adapterPictures = new AdapterPostPictures(getApplicationContext(), pictures, pValue);
                //mListView.setAdapter(adapterPictures);
            } else {
                adapterPictures.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void takeSnapMap(Bitmap bitmap) {
        BlogPictureView pictureView = new BlogPictureView(getApplicationContext());
        pictureView.setPicture(bitmap);
        pictures.add(pictureView);
        setAdapter();
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private Bitmap loadImage(Uri selectedImageUri) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT < 19) {
            String selectedImagePath = getPath(selectedImageUri);
            bitmap = BitmapFactory.decodeFile(selectedImagePath);
        } else {
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


}

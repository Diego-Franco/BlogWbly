package com.defch.blogwbly.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.defch.blogwbly.R;
import com.defch.blogwbly.adapters.AdapterPostPictures;
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

    private static final String POST_VALUE = "PostValue";
    private static final String POST_OBJECT = "PostObject";
    public static String MAP = "snapMap";
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;
    private static final int MAP_REQUEST = 1004;


    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;
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

    @InjectView(R.id.post_textview_title)
    TextView mTVTitle;
    @InjectView(R.id.post_textview_text)
    TextView mTVText;
    @InjectView(R.id.post_edittext_title)
    EditText mETTitle;
    @InjectView(R.id.post_edittext_text)
    EditText mETText;
    @InjectView(R.id.post_listview)
    ListView mListView;

    private float mUploadButtonHeight;
    private float mUploadMenuButtonHeight;
    private int mNavBarHeight = -1;
    private boolean uploadMenuOpen = false;
    private boolean uploadMenuShowing = false;

    private AdapterPostPictures adapterPictures;
    private ArrayList<BlogPictureView> pictures;

    private PostValue pValue;
    private BlogPost post;

    public enum PostValue {
        VIEW, EDIT, CREATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_activity);
        setupToolbar();
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

        mETTitle.setFocusableInTouchMode(false);
        mETTitle.setFocusable(false);
        mETTitle.setFocusableInTouchMode(true);
        mETTitle.setFocusable(true);
        mETText.setFocusableInTouchMode(false);
        mETText.setFocusable(false);
        mETText.setFocusableInTouchMode(true);
        mETText.setFocusable(true);

        mETTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                animateUploadMenuButton(true);
                return true;
            }
        });


        pValue = (PostValue) getIntent().getSerializableExtra(POST_VALUE);

        if(pValue != null) {

            if(pValue == PostValue.CREATE) {
                // create new model
                mTVTitle.setVisibility(View.GONE);
                mTVText.setVisibility(View.GONE);
                mETTitle.setVisibility(View.VISIBLE);
                mETText.setVisibility(View.VISIBLE);


            }  else if(pValue == PostValue.EDIT){
                // edit the model
                post = (BlogPost) getIntent().getParcelableExtra(POST_OBJECT);
            } else if(pValue == PostValue.VIEW) {
                // load the model
                post = (BlogPost) getIntent().getParcelableExtra(POST_OBJECT);

            }
        }

    }

    private void setupToolbar() {
        setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                animateUploadMenuButton(false);
            }
        }
        return ret;
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

    //TODO get the menuItem and show edit option when the user click save
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                //save the post
                break;
            case R.id.action_edit:

                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                mListView.setAdapter(adapterPictures);
                mListView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });
                setListViewHeightBasedOnChildren(mListView);
            } else {
                adapterPictures.notifyDataSetChanged();
            }
        }
    }

    //TODO implement snap picture from map
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

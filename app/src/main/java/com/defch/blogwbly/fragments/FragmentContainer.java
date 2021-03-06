package com.defch.blogwbly.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.MainActivity;
import com.defch.blogwbly.activities.PostActivity;
import com.defch.blogwbly.adapters.AdapterPostPictures;
import com.defch.blogwbly.ifaces.FContainerIfaces;
import com.defch.blogwbly.ifaces.PostInterfaces;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.BlogPictureView;
import com.defch.blogwbly.ui.ContainerLayout;

import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.GridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by DiegoFranco on 4/18/15.
 */
public class FragmentContainer extends FragmentContainerBase implements View.OnClickListener, FContainerIfaces{

    private static final String TAG = FragmentContainer.class.getSimpleName();

    private static final String RESTORE_TITLE = "title";
    private static final String RESTORE_CONTENT = "content";
    private static final String RESTORE_PICTURES = "pictures";

    private static final String FRAGMENT_TAG = "fragment_container";
    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    @InjectView(R.id.post_textview_title)
    EditText title;
    @InjectView(R.id.post_textview_text)
    EditText content;

    @InjectView(R.id.list)
    TwoWayView pictureList;

    private String titleSaved, contentSaved;

    private MenuItem menuItemSave;

    private AdapterPostPictures adapterPictures;
    private ArrayList<BlogPictureView> pictures = new ArrayList<>();

    private BlogPost bPost;
    private int keyLayout;
    public ContainerLayout containerLayout;
    private PostActivity.PostValue postValue;
    private double latitude, longitude;

    private PostInterfaces postInterfaces;

    public static FragmentContainer createInstance(int keyLayout, PostActivity.PostValue postValue) {
        FragmentContainer fragmentContainer = new FragmentContainer();

        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, keyLayout);
        args.putSerializable(POST_VALUE, postValue);
        fragmentContainer.setArguments(args);
        return fragmentContainer;
    }

    private void changeLayoutManagerToRecyclerView() {
        switch (keyLayout) {
            case 0:
            case 1:
            case 3:
                ((GridLayoutManager)pictureList.getLayoutManager()).setNumColumns(adapterPictures.getItemCount());
                ((GridLayoutManager)pictureList.getLayoutManager()).setNumRows(1);
                ((GridLayoutManager)pictureList.getLayoutManager()).setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
                break;
        }
    }

    public static FragmentContainer createInstance(int keyLayout, PostActivity.PostValue postValue, BlogPost post) {
        FragmentContainer fragmentContainer = new FragmentContainer();

        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, keyLayout);
        args.putSerializable(POST_VALUE, postValue);
        args.putParcelable(POST_OBJECT, post);
        fragmentContainer.setArguments(args);

        return fragmentContainer;
    }

    public void setPostInterfaces(PostInterfaces postInterfaces) {
        this.postInterfaces = postInterfaces;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.keyLayout = getArguments().getInt(KEY_LAYOUT);
        this.containerLayout = ContainerLayout.getLatyout(keyLayout);
        this.postValue = (PostActivity.PostValue) getArguments().getSerializable(POST_VALUE);
        this.bPost = getArguments().getParcelable(POST_OBJECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //LogUtil.v(TAG, Integer.toString(containerLayout.key));
        return inflater.inflate(containerLayout.layoutId, container, false);
    }

    private void setupToolbar() {
        ((PostActivity)getActivity()).setStatusBarColor(getResources().getColor(app.getWTheme().darkColor));
        mToolBar.setBackgroundColor(getResources().getColor(app.getWTheme().primaryColor));
        ((PostActivity)getActivity()).setSupportActionBar(mToolBar);
        ((PostActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((PostActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        ((PostActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        ((PostActivity)getActivity()).setContainerIfaces(this);
        title.setFocusable(false);
        content.setFocusable(false);
        title.setOnLongClickListener(longClickTextView);
        content.setOnLongClickListener(longClickTextView);

        if(postValue == PostActivity.PostValue.VIEW || postValue == PostActivity.PostValue.EDIT) {
            if(bPost.getThumbnails() != null) {
                for(int i = 0; i < bPost.getThumbnails().size(); i ++) {
                    BlogPictureView pictureView = new BlogPictureView();
                    pictureView.setPicture(bPost.getThumbnails().get(i));
                    if(bPost.isMapView()) {
                        pictureView.setLatitude(bPost.getLatitude());
                        pictureView.setLongitude(bPost.getLongitude());
                        pictureView.setIsMapPicture(true);
                    }
                    pictures.add(pictureView);
                }
                setAdapter();
            }
            title.setText(bPost.getTitle());
            content.setText(bPost.getSubtitle());
        }
        if(savedInstanceState != null) {
            restoreInstateSaved(savedInstanceState);
        }
    }

    private void restoreInstateSaved(Bundle savedInstanceState) {
        titleSaved = savedInstanceState.getString(RESTORE_TITLE);
        contentSaved = savedInstanceState.getString(RESTORE_CONTENT);
        pictures = savedInstanceState.getParcelableArrayList(RESTORE_PICTURES);

        if(titleSaved != null) {
            title.setText(titleSaved);
        }
        if(contentSaved != null) {
            content.setText(contentSaved);
        }
        if(pictures != null) {
            setAdapter();
        } else {
            pictures = new ArrayList<>();
        }
    }

    View.OnLongClickListener longClickTextView = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(bPost != null) {
                postInterfaces.longClickedOnEditText(true, (EditText) v);
            } else {
                postInterfaces.longClickedOnEditText(true, null);
            }
            return true;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_post, menu);
        menuItemSave = menu.findItem(R.id.action_save);
        if(postValue == PostActivity.PostValue.VIEW) {
            menuItemSave.setVisible(false);
        } else if(postValue == PostActivity.PostValue.CREATE || postValue == PostActivity.PostValue.EDIT) {
            menuItemSave.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                ((PostActivity)getActivity()).app.retrievePostFromDB();
                ((PostActivity)getActivity()).newIntent(MainActivity.class);
                break;
            case R.id.action_save:
                savePost();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePost() {
        if(bPost != null) {
            bPost.setTitle(title.getText().toString());
            bPost.setSubtitle(content.getText().toString());
            if(latitude != Double.MIN_VALUE && longitude != Double.MIN_VALUE) {
                bPost.setLatitude(latitude);
                bPost.setLongitude(longitude);
            }
            if(pictures.size() > 0) {
                ArrayList<Bitmap> bmaps = new ArrayList<>();
                for(int i = 0; i < pictures.size(); i++) {
                    BlogPictureView pictureView = new BlogPictureView();
                    pictureView.setPicture(bPost.getThumbnails().get(i));
                    bmaps.add(pictureView.getPicture());
                }
                bPost.setThumbnails(bmaps);
            }
            app.updatePostOnDB(bPost);
        } else {
            bPost = new BlogPost();
            if(latitude > 0.0 && longitude > 0.0) {
                bPost.setLatitude(latitude);
                bPost.setLongitude(longitude);
            }
            bPost.setTitle(title.getText().toString());
            bPost.setSubtitle(content.getText().toString());
            bPost.setLayoutId(keyLayout);
            if(pictures.size() > 0) {
                ArrayList<Bitmap> bmaps = new ArrayList<>();
                for(int i = 0; i < pictures.size(); i++) {
                    BlogPictureView pictureView = pictures.get(i);
                    bmaps.add(pictureView.getPicture());
                }
                bPost.setThumbnails(bmaps);
            }
            app.savePostOnDB(bPost);
            ((PostActivity)getActivity()).app.retrievePostFromDB();
            ((PostActivity)getActivity()).newIntent(MainActivity.class);
        }
    }

    @OnClick({R.id.post_textview_title, R.id.post_textview_text})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_textview_title:
            case R.id.post_textview_text:
                postInterfaces.clickTextToEdit((EditText) v);
                break;
        }
    }

    @Override
    public void receiveText(String string, int id) {
        switch (id) {
            case R.id.post_textview_title:
                title.setText(string);
                break;
            case R.id.post_textview_text:
                content.setText(string);
                break;
        }
    }

    @Override
    public void showTextWithRitchText(SpannableStringBuilder s, EditText editText) {
        switch (editText.getId()) {
            case R.id.post_textview_title:
                title.setText(s);
                break;
            case R.id.post_textview_text:
                content.setText(s);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BlogPictureView pictureView = new BlogPictureView();
        Bitmap bmp;
        if(data != null) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (resultCode == getActivity().RESULT_OK) {
                        Bundle extras = data.getExtras();
                        bmp = (Bitmap) extras.get("data");
                        pictureView.setPicture(bmp);
                        pictures.add(pictureView);
                        setAdapter();
                    }
                    break;
                case VIDEO_REQUEST:
                    if (resultCode == getActivity().RESULT_OK) {
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
    }

    private void setAdapter() {
        if(pictures.size() > 0) {
            if(adapterPictures == null) {
                adapterPictures = new AdapterPostPictures(getActivity(), pictures, postValue);
                pictureList.setAdapter(adapterPictures);
            } else {
                adapterPictures.notifyDataSetChanged();
            }
            changeLayoutManagerToRecyclerView();
        }
    }

    public void setSnapMap(Bitmap bitmap, double latitude, double longitude) {
        BlogPictureView pictureView = new BlogPictureView();
        pictureView.setPicture(bitmap);
        pictures.add(pictureView);
        setAdapter();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
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
                parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImageUri, "r");
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(title.getText().toString().length() > 0) {
            outState.putString(RESTORE_TITLE, title.getText().toString());
        }
        if(content.getText().toString().length() > 0) {
            outState.putString(RESTORE_CONTENT, content.getText().toString());
        }
        if(pictures != null) {
            if(pictures.size() > 0)
            outState.putParcelableArrayList(RESTORE_PICTURES, pictures);
        }
        super.onSaveInstanceState(outState);
    }


}

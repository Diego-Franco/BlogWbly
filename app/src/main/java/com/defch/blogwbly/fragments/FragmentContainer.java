package com.defch.blogwbly.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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
import com.defch.blogwbly.adapters.AdapterPostPictures;
import com.defch.blogwbly.ifaces.PostInterfaces;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.BlogPictureView;
import com.defch.blogwbly.ui.ContainerLayout;
import com.defch.blogwbly.util.LogUtil;

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

//TODO implement new blank view for drag and drop
public class FragmentContainer extends FragmentContainerBase implements View.OnClickListener{

    private static final String TAG = FragmentContainer.class.getSimpleName();

    private static final String KEY_LAYOUT = "key_layout";
    private static final String POST_VALUE = "post_value";
    private static final String POST_OBJECT = "post_object";
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    @InjectView(R.id.post_textview_title)
    TextView title;
    @InjectView(R.id.post_textview_text)
    TextView content;

    @InjectView(R.id.list)
    TwoWayView pictureList;

    private MenuItem menuItemEdit, menuItemSave;

    private AdapterPostPictures adapterPictures;
    private ArrayList<BlogPictureView> pictures;

    private BlogPost bPost;
    private int keyLayout;
    private ContainerLayout containerLayout;
    private PostActivity.PostValue postValue;

    private PostInterfaces postInterfaces;

    public static FragmentContainer createInstance(int keyLayout, PostActivity.PostValue postValue) {
        FragmentContainer fragmentContainer = new FragmentContainer();

        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, keyLayout);
        args.putSerializable(POST_VALUE, postValue);
        fragmentContainer.setArguments(args);

        return fragmentContainer;
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
        pictures = new ArrayList<>();
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
        if(postValue == PostActivity.PostValue.VIEW) {

        } else if(postValue == PostActivity.PostValue.CREATE || postValue == PostActivity.PostValue.EDIT) {

        }
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

    @OnClick({R.id.post_textview_title, R.id.post_textview_text})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_textview_title:
            case R.id.post_textview_text:
                postInterfaces.clickTextToEdit(v.getId());
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BlogPictureView pictureView = new BlogPictureView(getActivity().getApplicationContext());
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
        if(pictures != null && pictures.size() > 0) {
            if(adapterPictures == null) {
                adapterPictures = new AdapterPostPictures(getActivity().getApplicationContext(), pictures, postValue);
                //mListView.setAdapter(adapterPictures);
            } else {
                adapterPictures.notifyDataSetChanged();
            }
        }
    }

    public void setSnapMap(Bitmap bitmap) {
        BlogPictureView pictureView = new BlogPictureView(getActivity().getApplicationContext());
        pictureView.setPicture(bitmap);
        pictures.add(pictureView);
        setAdapter();
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
}

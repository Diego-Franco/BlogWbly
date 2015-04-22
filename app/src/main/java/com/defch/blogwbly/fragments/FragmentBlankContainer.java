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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.MainActivity;
import com.defch.blogwbly.activities.PostActivity;
import com.defch.blogwbly.adapters.AdapterPostPictures;
import com.defch.blogwbly.ifaces.FContainerIfaces;
import com.defch.blogwbly.ifaces.PostInterfaces;
import com.defch.blogwbly.model.BlogPost;
import com.defch.blogwbly.ui.BlogPictureView;
import com.defch.blogwbly.ui.DDListener;

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
 * Created by DiegoFranco on 4/21/15.
 */
public class FragmentBlankContainer extends FragmentContainerBase implements View.OnClickListener, FContainerIfaces {

    private static final String TAG = FragmentBlankContainer.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1001;
    private static final int VIDEO_REQUEST = 1002;
    private static final int GALLERY_REQUEST = 1003;

    @InjectView(R.id.mtoolbar)
    Toolbar mToolBar;

    @InjectView(R.id.layout_container)
    FrameLayout frameLayout;

    @InjectView(R.id.post_textview_title)
    EditText edtxTitle;
    @InjectView(R.id.post_textview_text)
    EditText edtxContent;
    @InjectView(R.id.list_h)
    TwoWayView listH;
    @InjectView(R.id.list_v)
    TwoWayView listV;


    private AdapterPostPictures adapterPictures;
    private ArrayList<BlogPictureView> pictures = new ArrayList<>();

    private double latitude, longitude;

    private PostInterfaces postInterfaces;

    public void setPostInterfaces(PostInterfaces postInterfaces) {
        this.postInterfaces = postInterfaces;
    }

    public static FragmentBlankContainer createInstance() {
        return new FragmentBlankContainer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.container_6, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        ((PostActivity)getActivity()).setContainerIfaces(this);
        edtxTitle.setFocusable(false);
        edtxContent.setFocusable(false);
        edtxTitle.setOnTouchListener(new DDListener(frameLayout, edtxTitle, postInterfaces));
        edtxContent.setOnTouchListener(new DDListener(frameLayout, edtxContent, postInterfaces));
        listH.setOnTouchListener(new DDListener(frameLayout, listH));
        listV.setOnTouchListener(new DDListener(frameLayout, listV));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_blank_container, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.action_save:
                if(validateIfSomeIsVisible() && canSaveThePost()) {
                    savePost();
                } else {
                    Toast.makeText(getActivity(), R.string.warning_save, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_add_title:
                changeTitleVisibility();
                break;
            case R.id.action_add_content:
                changeContentVisibility();
                break;
            case R.id.action_add_images:
                createDialogForPicturesList();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateIfSomeIsVisible() {
        if(edtxTitle.getVisibility() == View.VISIBLE || edtxContent.getVisibility() == View.VISIBLE || listH.getVisibility() == View.VISIBLE || listV.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private boolean canSaveThePost() {
        if(edtxTitle.getText().toString().length() > 0 || edtxContent.getText().toString().length() > 0) {
            return true;
        }
        return false;
    }

    private void savePost() {
            BlogPost blogPost = new BlogPost();
            if(latitude > 0.0 && longitude > 0.0) {
                blogPost.setLatitude(latitude);
                blogPost.setLongitude(longitude);
            }
            blogPost.setTitle(edtxTitle.getText().toString());
            blogPost.setSubtitle(edtxContent.getText().toString());
            blogPost.setLayoutId(5);
            if(pictures.size() > 0) {
                ArrayList<Bitmap> bmaps = new ArrayList<>();
                for(int i = 0; i < pictures.size(); i++) {
                    BlogPictureView pictureView = new BlogPictureView(getActivity().getApplicationContext());
                    pictureView.setPicture(blogPost.getThumbnails().get(i));
                    bmaps.add(pictureView.getPicture());
                }
                blogPost.setThumbnails(bmaps);
            }
            app.savePostOnDB(blogPost);
        ((PostActivity)getActivity()).app.retrievePostFromDB();
        ((PostActivity)getActivity()).newIntent(MainActivity.class);
    }

    @Override
    public void receiveText(String string, int id) {
        switch (id) {
            case R.id.post_textview_title:
                edtxTitle.setText(string);
                break;
            case R.id.post_textview_text:
                edtxContent.setText(string);
                break;
        }
    }

    @Override
    public void showTextWithRitchText(SpannableStringBuilder s, EditText editText) {
        switch (editText.getId()) {
            case R.id.post_textview_title:
                edtxTitle.setText(s);
                break;
            case R.id.post_textview_text:
                edtxContent.setText(s);
                break;
        }
    }

    @OnClick({R.id.post_textview_title, R.id.post_textview_text})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_textview_title:
            case R.id.post_textview_text:

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(hListIsVisible() || vListIsVisible()) {
            BlogPictureView pictureView = new BlogPictureView(getActivity().getApplicationContext());
            Bitmap bmp;
            if (data != null) {
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
        } else {
            Toast.makeText(getActivity(), R.string.warning_pictures ,Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        if(pictures != null && pictures.size() > 0) {
            if(adapterPictures == null) {
                adapterPictures = new AdapterPostPictures(getActivity(), pictures, PostActivity.PostValue.CREATE);
                if(listH.getVisibility() == View.VISIBLE) {
                    listH.setAdapter(adapterPictures);
                } else if(listV.getVisibility() == View.VISIBLE) {
                    listV.setAdapter(adapterPictures);
                }
            } else {
                adapterPictures.notifyDataSetChanged();
            }
            changeLayoutManagerToRecyclerView();
        }
    }

    private void changeLayoutManagerToRecyclerView() {
        if(listH.getVisibility() == View.VISIBLE) {
            ((GridLayoutManager)listH.getLayoutManager()).setNumColumns(adapterPictures.getItemCount());
            ((GridLayoutManager)listH.getLayoutManager()).setNumRows(1);
            ((GridLayoutManager)listH.getLayoutManager()).setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        }
    }

    public void setSnapMap(Bitmap bitmap, double latitude, double longitude) {
        if(hListIsVisible() || vListIsVisible()) {
            BlogPictureView pictureView = new BlogPictureView(getActivity().getApplicationContext());
            pictureView.setPicture(bitmap);
            pictures.add(pictureView);
            setAdapter();
            this.latitude = latitude;
            this.longitude = longitude;
        } else {
            Toast.makeText(getActivity(), R.string.warning_pictures ,Toast.LENGTH_SHORT).show();
        }
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

    private boolean titleIsVisible() {
        if(edtxTitle.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private void changeTitleVisibility() {
        if(titleIsVisible()) {
            edtxTitle.setVisibility(View.GONE);
        } else {
            edtxTitle.setVisibility(View.VISIBLE);
        }
    }

    private boolean contentIsVisible() {
        if(edtxContent.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private void changeContentVisibility() {
        if(contentIsVisible()) {
            edtxContent.setVisibility(View.GONE);
        } else {
            edtxContent.setVisibility(View.VISIBLE);
        }
    }

    private void createDialogForPicturesList() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title)
                .items(R.array.list_arrays)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                changeHListVisibility();
                                break;
                            case 1:
                                changeVListVisibility();
                                break;
                        }
                    }
                }).show();
    }

    private boolean hListIsVisible() {
        if(listH.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private void changeHListVisibility() {
        if(hListIsVisible()) {
            listH.setVisibility(View.GONE);
            ((GridLayoutManager)listH.getLayoutManager()).setOrientation(TwoWayLayoutManager.Orientation.HORIZONTAL);
        } else {
            listH.setVisibility(View.VISIBLE);
        }
    }

    private boolean vListIsVisible() {
        if(listV.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    private void changeVListVisibility() {
        if(vListIsVisible()) {
            listV.setVisibility(View.GONE);
        } else {
            listV.setVisibility(View.VISIBLE);
        }
    }

}

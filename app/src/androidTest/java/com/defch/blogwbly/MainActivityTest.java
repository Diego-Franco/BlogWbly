package com.defch.blogwbly;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.defch.blogwbly.activities.MainActivity;
import com.defch.blogwbly.adapters.AdapterBlogList;
import com.defch.blogwbly.model.BlogPost;

import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;

/**
 * Created by DiegoFranco on 4/22/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private ImageView emptyImage;
    private LinearLayout layout;
    private Toolbar toolbar;
    private TwoWayView recyclerView;
    private RecyclerView supporRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        emptyImage = (ImageView)mainActivity.findViewById(R.id.main_img_empty);
        layout = (LinearLayout)mainActivity.findViewById(R.id.layout);
        toolbar = (Toolbar)mainActivity.findViewById(R.id.mtoolbar);
        recyclerView = (TwoWayView) mainActivity.findViewById(R.id.my_recycler_view);
        supporRecyclerView = (RecyclerView) mainActivity.findViewById(R.id.my_recycler_view);
    }

    public void testPreconditions() {
        assertNotNull("MainActivity is null", mainActivity);
        assertNotNull("emptyImage is null", emptyImage);
        assertNotNull("layout is null", layout);
        assertNotNull("toolbar is null", toolbar);
        assertNotNull("recyclerView is null", recyclerView);
        assertNotNull("supporRecyclerView is null", supporRecyclerView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void testMainActivity_emptyImage() {
        if(emptyImage.getVisibility() == View.VISIBLE) {
            final Drawable expectedDrawable = mainActivity.getDrawable(R.mipmap.ic_empty);
            final Drawable currentDrawable = emptyImage.getDrawable();
            assertEquals(expectedDrawable, currentDrawable);
        }
    }

    public void testMainActivity_clickMenuOption() {
        int menuAddId = R.id.action_add;
        getInstrumentation().invokeMenuActionSync(mainActivity, menuAddId, 0);
    }

    public void testMainActivity_recyclerFull() {
        ArrayList<Bitmap> bPics = new ArrayList<>();
        ArrayList<BlogPost> blogPostArrayList = new ArrayList<>();
        Bitmap bmp = BitmapFactory.decodeResource(mainActivity.getResources(), R.mipmap.ic_launcher);
        bPics.add(bmp);
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("this is a title");
        post.setSubtitle("this is a content");
        post.setThumbnails(bPics);
        blogPostArrayList.add(post);
        mAdapter = new AdapterBlogList(mainActivity, blogPostArrayList);
        assertNotNull("Adapter is null", mAdapter);
        assertSame(1, mAdapter.getItemCount());

    }

}

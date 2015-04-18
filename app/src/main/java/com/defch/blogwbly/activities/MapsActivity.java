package com.defch.blogwbly.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.defch.blogwbly.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.InjectView;
import butterknife.OnClick;

public class MapsActivity extends BaseActivity implements View.OnClickListener{

    public static String MAP_LOCATION = "map_location";
    public static String MAP = "snapMap";
    private static final int MAP_REQUEST = 1004;

    private static final int WIDTH_PX = 400;
    private static final int HEIGHT_PX = 400;

    @InjectView(R.id.map_set_btn)
    Button setBtn;

    PostActivity.ImageLoad imageLoad;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Bitmap bmp;
    private PostActivity postActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gmap_activity);
        setUpMapIfNeeded();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        bmp = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, conf); // this creates a MUTABLE bitmap
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            // Check if we were successful in obtaining the map.

        }
    }



    @OnClick(R.id.map_set_btn)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_set_btn:
                mMap.snapshot(callback, bmp);
                break;
        }
    }

    GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
        @Override
        public void onSnapshotReady(Bitmap bitmap) {
            if(bitmap != null) {
                //return bitmap
                MapsActivity.this.finish();
            }
        }
    };


}

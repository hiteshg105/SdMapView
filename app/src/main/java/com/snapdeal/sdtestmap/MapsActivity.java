package com.snapdeal.sdtestmap;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.snapdeal.drag.pedrovgs.DraggableView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context mContext;
    private static final LatLng NEBOUND = new LatLng(84.64077737381076, 175.41476134210825);
    private static final LatLng SWBOUND = new LatLng(0.9941377077895567, -179.0293239802122);
    private static final LatLngBounds MAPBOUNDARY = new LatLngBounds(SWBOUND, NEBOUND);
    private LatLng lastCenter = new LatLng(63.03869387456829, 73.98576982319355);
    private float mCurrentZoom;
    private RelativeLayout mLayout;
    private Handler mHandler;
    private boolean mIsVisible;
    private ImageView mHeart;
    DraggableView draggableView;
    ListView episodesListView;

    private final GoogleMap.OnCameraChangeListener mOnCameraChangeListener =
            new GoogleMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    mCurrentZoom = cameraPosition.zoom;
                    float maxZoom = 9f;
                    float minZoom = 3f;
                    if (cameraPosition.zoom > maxZoom) {
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
//                        UiSettings mMapSettings =mMap.getUiSettings();
//                        mMapSettings.setZoomControlsEnabled(true);
                    }

                    if (cameraPosition.zoom >= 1) {
                        addGroundOverlay();
                    }

                    if (cameraPosition.zoom < minZoom)
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));


//                    if(cameraPosition.target.latitude <17.262){
//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(17.262,-67.085)));
//                    }

                    LatLng tempCenter = mMap.getCameraPosition().target;
                    LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                    if (cameraPosition.target.latitude < 17.262 || cameraPosition.target.longitude > 170 || cameraPosition.target.longitude < -170) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(lastCenter));
                    } else
                        lastCenter = tempCenter;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_maps);
        initializeDraggableView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mLayout = (RelativeLayout) findViewById(R.id.horizontal_view);
        mHeart = (ImageView) findViewById(R.id.heart);
//        mHeart.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                int eid = event.getAction();
//                switch (eid) {
//
//
//                    case MotionEvent.ACTION_DOWN:
//                    {
//                        int x = (int) event.getRawX();
//                        int y = (int) event.getRawY();
//                        break;
//                    }
//
//
//                    case MotionEvent.ACTION_MOVE:
//
//                    {
//                        RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) mHeart.getLayoutParams();
//                        int x = (int) event.getRawX();
//                        int y = (int) event.getRawY();
//                        mParams.leftMargin = x - 50;
//                        mParams.topMargin = y - 50;
//                        mHeart.setLayoutParams(mParams);
//                        int visacard_width = mHeart.getWidth() / 2;
//                        int visacard_height = mHeart.getHeight() / 2;
//                        int i1 = x - visacard_width;
//                        int j1 = y - visacard_height;
//                        int k1 =  mHeart.getWidth();
//                        int l1 = mHeart.getHeight();
//                        mHeart.setX(i1);
//                        mHeart.setY(j1);
////                        visacard_width = visacard.getWidth();
////                        visacard_height = visacard.getHeight();
////                        int visacard_top = mHeart.getTop();
////                        visacard_left = mHeart.getLeft();
////                        visacard_right = mHeart.getRight();
////                        visacard_bottom = mHeart.getBottom();
////                        text.setX(k1);
////                        text.setY(l1);
//
//                        break;
//                    }
//                    case MotionEvent.ACTION_UP:
//                    {
//                        break;
//                    }
//
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

        mHeart.setOnTouchListener(new View.OnTouchListener() {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eid = event.getAction();
                switch (eid) {
                    case MotionEvent.ACTION_MOVE:
                        PointF mv = new PointF(event.getX() - DownPT.x, event.getY() - DownPT.y);
                        mHeart.setX((int) (StartPT.x + mv.x));
                        mHeart.setY((int) (StartPT.y + mv.y));
                        StartPT = new PointF(mHeart.getX(), mHeart.getY());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DownPT.x = event.getX();
                        DownPT.y = event.getY();
                        StartPT = new PointF(mHeart.getX(), mHeart.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        // Nothing have to do
                        mHeart.setX(StartPT.x);
                        mHeart.setY(StartPT.y);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mapFragment.getMapAsync(this);

//       setUpHandler();
//        mHandler.sendEmptyMessage(0);

    }

    private void initializeDraggableView() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                draggableView = (DraggableView) findViewById(R.id.draggable_view);
                draggableView.setVisibility(View.GONE);
            }
        }, 10);

    }

    private void setUpHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                checkBoundaries();
                sendEmptyMessageDelayed(0, 100);
            }
        };
    }

    private void checkBoundaries() {
        LatLng tempCenter = mMap.getCameraPosition().target;
        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        if (!MAPBOUNDARY.contains(visibleBounds.northeast) || !MAPBOUNDARY.contains(visibleBounds.southwest)) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));
        } else
            lastCenter = tempCenter;
    }

//    private void setUpHandler(){
//        mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                checkBoundaries();
//                sendEmptyMessageDelayed(0, 5);
//            }
//        };
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        getTile("/tsne/526/[0.2,0.8]_T0.5_P200.0_tilesfull/tile_");

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mMap.getCameraPosition().zoom == 9) {
                    draggableView.setVisibility(View.VISIBLE);
                    draggableView.closeToLeft();
                    draggableView.maximize();
                    initializeList();
                } else {
                    Log.v("LAT-Long", String.valueOf(latLng.latitude) + " " + String.valueOf(latLng.longitude));
                    Point mPoint = mMap.getProjection().toScreenLocation(latLng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));
//                if (mIsVisible) {
//                    mLayout.setVisibility(View.GONE);
//                    mIsVisible = false;
//                } else {
//                    mLayout.setVisibility(View.VISIBLE);
//                    mIsVisible = true;
//                }
                    Log.v("Pixel Coordinate", String.valueOf(mPoint.x) + " " + String.valueOf(mPoint.y));
                }

            }
        });
        // Because the demo WMS layer we are using is just a white background map, switch the base layer
        // to satellite so we can see the WMS overlay.
    }

    private void initializeList() {
        episodesListView = (ListView) findViewById(R.id.lv_episodes);
        String[] mobileArray = {"Android", "IPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X"};

        episodesListView.setAdapter(new ArrayAdapter<String>(this, R.layout.activity_layout, R.id.tv_name, mobileArray));
    }

    private void getTile(String mUrl) {
        BaseTileProvider mBaseProvider = new BaseTileProvider(this, 20, 20);
        CustomMapTileProvider wmsTileProvider = new CustomMapTileProvider(this, 20, 20);
        TileOverlayOptions mOptions = new TileOverlayOptions();
        mMap.addTileOverlay(mOptions.tileProvider(mBaseProvider).zIndex(0));
        mMap.addTileOverlay(mOptions.tileProvider(wmsTileProvider).zIndex(1));
        mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(74.9, -54), 3));
        mMap.setOnCameraChangeListener(mOnCameraChangeListener);
        UiSettings mMapSettings = mMap.getUiSettings();
        mMapSettings.setZoomControlsEnabled(true);
        mMapSettings.setRotateGesturesEnabled(false);
        mMapSettings.setMyLocationButtonEnabled(false);
        mMapSettings.setCompassEnabled(false);
    }

    private void addGroundOverlay() {
        LatLng NEWARK = new LatLng(69.8146241613047, -85.0167590752244);

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.camera_pic))
                .position(NEWARK, 8600f, 6500f);
        newarkMap.zIndex(10);
        mMap.addGroundOverlay(newarkMap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                getTile("http://52.7.54.202:8060/tsne/526/tiles/tile_");

                return true;
            case R.id.item2:
                getTile("/tsne/526/[0.2,0.8]_T0.5_P200.0_tilesfull/tile_");
                return true;

            case R.id.item3:
                getTile("/static/526/tilesfull/tile_");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

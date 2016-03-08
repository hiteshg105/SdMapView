package com.snapdeal.sdtestmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.net.URL;

public class BaseTileProvider implements TileProvider {

    private static final int BUFFER_SIZE = 16 * 1024;

    private AssetManager mAssets;
    private Context mContext;
    URL url = null;

    public BaseTileProvider(Context context, int width, int height) {
        mContext = context;
//        super(width,height);


    }


    @Override
    public Tile getTile(int x, int y, int zoom) {
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.bg_mapview);

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] image = stream.toByteArray();
        //readTileImage(x, y, zoom);
        return image == null ? null : new Tile(256, 256, image);
    }



}
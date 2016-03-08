package com.snapdeal.sdtestmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CustomMapTileProvider implements TileProvider {

    private static final int BUFFER_SIZE = 16 * 1024;

    private AssetManager mAssets;
    private Context mContext;
    URL url= null;

    public CustomMapTileProvider( Context context ,int width,int height) {
        mContext=context;
//        super(width,height);


    }


//    public Coordinate getNormalizedCoordinate(Coordinate c, int zoom)
//    {
//
//        // tile range in one direction range is dependent on zoom level
//        // 0 = 1 tile, 1 = 2 tiles, 2 = 4 tiles, 3 = 8 tiles, etc
//        int tileRange = 1 << zoom;
//        c.y=tileRange-1-c.y;
//
//        // don't repeat across y-axis (vertically)
//        if (c.y < 0 || c.y >= tileRange) {
//            return null;
//        }
//
//        // repeat across x-axis
//        if (c.x < 0 || c.x >= tileRange) {
//            return null;
//            // x = (x % tileRange + tileRange) % tileRange;
//        }
//
//        return c;
//    }



    @Override
    public Tile getTile(int x, int y, int zoom) {
        byte[] image = readTileImage(x, y, zoom);
        return image == null ? null : new Tile(256, 256, image);
    }

    private byte[] readTileImage(int x, int y, int zoom) {
//        InputStream in = null;
//        ByteArrayOutputStream buffer = null;
//
//        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        return byteArray;

        if(zoom>9 || zoom <2)
            return null;
        Bitmap src= GetBitmapfromUrl("http://52.7.54.202:8060/tsne/191/[0.2,0.8]_T0.5_P200.0_tileshybrid/tile_" + zoom + "_" + x + "-" + y + ".jpg");
        Log.d("TILE_URL", "http://52.7.54.202:8060/tsne/526/[0.2,0.8]_T0.5_P200.0_tilesfull/tile_" + zoom + "_" + x + "-" + y + ".jpg");

      Bitmap finalBitmap=  Bitmap.createScaledBitmap(src,256,256,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
//        try {
//            in = mAssets.open(getTileFilename(x, y, zoom));
//
//            buffer = new ByteArrayOutputStream();
//
//            int nRead;
//            byte[] data = new byte[BUFFER_SIZE];
//
//            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
//                buffer.write(data, 0, nRead);
//            }
//            buffer.flush();
//
//            return buffer.toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (in != null) try { in.close(); } catch (Exception ignored) {}
//            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
//        }
    }

    public Bitmap GetBitmapfromUrl(String scr) {
        try {
            URL url=new URL(scr);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input=connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(input);
            return bmp;



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
    private String getTileFilename(int x, int y, int zoom) {
        return "map/" + zoom + '/' + x + '/' + y + ".png";
    }
}
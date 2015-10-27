package Modules;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amora on 10/23/2015.
 */
public class ThumbnailDownloader<T> extends HandlerThread {

    private static final String TAG              = "ThumbnailDownloader";
    private static final int    MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    Map<T, String> requestMap =
            Collections.synchronizedMap(new HashMap<T, String>());

    Handler     mResponseHandler;
    Listener<T> mListener;

    public interface Listener<T>{
        void onThumbnailDownloaded(T token, Bitmap thumbnail);
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void setListener(Listener<T> listener){
        mListener = listener;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_DOWNLOAD){
                    T givenObject = (T) msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(givenObject));
                    handleRequest(givenObject);
                }
            }
        };
    }

    public void queueThumbnail(T token, String url){
        Log.i(TAG, "Got a URL: " + url);
        requestMap.put(token, url);

        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();

    }

    private void handleRequest(final T token){

        try{
            final String url = requestMap.get(token);
            if(url == null) return;

            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable(){
                @Override
                public void run() {
                    if(requestMap.get(token) != url){
                        return;
                    }

                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });
        }catch (IOException ioe){
            Log.e(TAG, "Error downloading image", ioe);
        }

    }

    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}

package Fragments;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.amora.photogallery.Activities.R;


import java.util.ArrayList;

import Modules.FlickrFetcher;
import Modules.GalleryItem;
import Modules.ThumbnailDownloader;

/**
 * Created by amora on 10/21/2015.
 */
public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    GridView               mGridView;
    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView>    mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        new FetchItemsTask().execute();

        mThumbnailThread =
                new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>(){
            @Override
            public void onThumbnailDownloaded(ImageView token, Bitmap thumbnail) {
                if(isVisible()){
                    token.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView);

        setupAdapter();

        return v;


    }

    void setupAdapter(){

        if(getActivity() == null || mGridView == null) return;

        if(mItems != null){

            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        }else{
            mGridView.setAdapter(null);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{

        @Override
        protected ArrayList<GalleryItem>  doInBackground(Void... params) {

            String query = "android";

            if(query != null){
                return new FlickrFetcher().search(query);
            }else{
                return new FlickrFetcher().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {
        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brian_up_close);
            GalleryItem item = getItem(position);

            for(int i = 1 ; i < 10; ++i){
                int previousPosition = position - i;
                int subsequentPosition = position + i;
                if(previousPosition >= 0){
                    mThumbnailThread.queueBitmap(getItem(previousPosition).getUrl());
                }

                if(subsequentPosition  <= (getCount() - 1)){
                    mThumbnailThread.queueBitmap(getItem(subsequentPosition).getUrl());
                }
            }

            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }


    }


}

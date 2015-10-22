package com.example.amora.photogallery.Activities;

import android.support.v4.app.Fragment;

import Fragments.PhotoGalleryFragment;

/**
 * Created by amora on 10/21/2015.
 */
public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();


    }
}

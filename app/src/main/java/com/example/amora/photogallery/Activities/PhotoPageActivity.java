package com.example.amora.photogallery.Activities;

import android.support.v4.app.Fragment;

import com.example.amora.photogallery.Fragments.PhotoPageFragment;

/**
 * Created by amora on 12/19/2015.
 */
public class PhotoPageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PhotoPageFragment();
    }
}

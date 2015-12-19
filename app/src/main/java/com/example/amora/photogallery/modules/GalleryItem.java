package com.example.amora.photogallery.Modules;

/**
 * Created by amora on 10/23/2015.
 */
public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;
    private String mOwner;

    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getOwner(){
        return mOwner;
    }

    public void setOwner(String owner){
        mOwner = owner;
    }

    public String getPhotoPageUrl(){
        return "http://www.flickr.com/photos/" + mOwner + "/" + mId;
    }
}

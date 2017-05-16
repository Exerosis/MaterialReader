package me.exerosis.materialreader.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.rometools.rome.feed.synd.SyndFeed;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class FeedModel implements Parcelable {
    private Observable<SyndFeed> feed;
    private String url;

    public static FeedModel obtain(Observable<SyndFeed> feed, String url) {
        return new FeedModel(feed, url);
    }

    private FeedModel(Observable<SyndFeed> feed, String url) {
        this.feed = feed.observeOn(AndroidSchedulers.mainThread());
        this.url = url;
    }

    public void refresh(Context context) {
        FeedStore.refresh(context, url);
    }

    public Observable<SyndFeed> getSource() {
        return feed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(url);
        url = null;
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel in) {
            String url = in.readString();
            for (FeedModel feedModel : FeedStore.getFeedsUnsafe())
                if (feedModel.url.equals(url))
                    return feedModel;
            throw new IllegalStateException("Couldn't locate FeedModel");
        }

        @Override
        public Object[] newArray(int size) {
            return new FeedModel[size];
        }
    };
}
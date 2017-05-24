package me.exerosis.materialreader;

import android.content.Context;
import android.graphics.Bitmap;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.squareup.picasso.Picasso;

import org.jdom2.Element;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class FeedUtils {
    private FeedUtils() {
    }

    public static Observable<Bitmap> getImage(Context context, SyndEntry entry) {
        //Check media:thumbnail and media:content
        for (Element element : entry.getForeignMarkup()) {
            String url = element.getAttributeValue("url");
            if (url != null)
                return getBitmap(context, url);
        }

        //Check enclosures.
        for (SyndEnclosure enclosure : entry.getEnclosures()) {
            String url = enclosure.getUrl();
            if (url != null)
                return getBitmap(context, url);
        }

        //Check Description
        List<String> urls = getImages(Jsoup.parse(entry.getDescription().getValue()));
        if (!urls.isEmpty())
            return getBitmap(context, urls);

        //Check Content
        for (SyndContent content : entry.getContents()) {
            urls = getImages(Jsoup.parse(content.getValue()));
            if (!urls.isEmpty())
                return getBitmap(context, urls);
        }

        try {
            urls = getImages(Jsoup.connect(entry.getLink()).get().select("article").first());
            if (!urls.isEmpty())
                return getBitmap(context, urls);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Observable.just(null);
    }

    private static Observable<Bitmap> getBitmap(Context context, String url) {
        return Observable.fromCallable(() -> Picasso.with(context).load(url).get());
    }

    private static Observable<Bitmap> getBitmap(Context context, List<String> urls) {
        return Observable.fromCallable(() -> {
            Bitmap largest = null;
            for (String url : urls) {
                Bitmap bitmap = Picasso.with(context).load(url).get();
                if (largest == null || bitmap.getByteCount() > largest.getByteCount())
                    largest = bitmap;
            }
            if (largest == null)
                throw new IOException("Failed to get any images!");
            return largest;
        });
    }

    private static List<String> getImages(org.jsoup.nodes.Element document) {
        List<String> urls = new ArrayList<>();
        for (org.jsoup.nodes.Element element : document.select("img"))
            urls.add(element.attr("src"));
        return urls;
    }

}
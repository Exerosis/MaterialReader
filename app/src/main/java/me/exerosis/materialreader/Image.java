package me.exerosis.materialreader;

public class Image {
    private final String url;
    private final int height;
    private final int width;

    public Image(String url, int width, int height) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }
}
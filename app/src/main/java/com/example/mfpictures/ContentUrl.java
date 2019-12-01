package com.example.mfpictures;

/*
Content provides with the necessary content such as the constructor that is called
 in the DisplayPhotosActivity to fill the arrayList(RecyclerView) and necessary methods
 */
public class ContentUrl {

    private String imageUrl;
    private String userName;
    private int likes;
    private int favorites;
    private int downloads;
    private int comments;
    private int views;

    public ContentUrl(String imageUrl, String userName, int likes, int favorites, int downloads, int views, int comments){
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.likes = likes;
        this.favorites = favorites;
        this.downloads = downloads;
        this.comments = comments;
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public int getLikes() {
        return likes;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getDownloads() {
        return downloads;
    }

    public int getComments() {
        return comments;
    }

    public int getViews() {
        return views;
    }
}

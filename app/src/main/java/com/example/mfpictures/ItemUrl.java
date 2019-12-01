package com.example.mfpictures;

/*
ItemUrl provides with the necessary content such as the constructor that is called
 in the ItemActivity to fill the arrayList(RecyclerView) and methods that are used to get
 the name and the picture of a current item in the recyclerView and is basically the reason
 that the pictures and the text that will fill each item in the recyclerView
 */
public class ItemUrl {
    private String category;
    private String itemPictureName;
    private int itemPicture;

    public ItemUrl(String category, int itemPicture, String itemPictureName) {
        this.category = category;
        this.itemPictureName = itemPictureName;
        this.itemPicture = itemPicture;
    }

    public String getItemPictureName() {
        return itemPictureName;
    }

    public int getItemPicture() {
        return itemPicture;
    }

    public String getCategory() {
        return category;
    }
}

package com.example.studyhub_smda2;

public class Folder {
    String name;
    int imageCount;

    public Folder(String name, int imageCount) {
        this.name = name;
        this.imageCount = imageCount;
    }

    public String getName() { return name; }
    public int getImageCount() { return imageCount; }
    public void setImageCount(int imageCount) { this.imageCount = imageCount; }
}
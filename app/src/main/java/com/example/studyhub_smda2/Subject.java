package com.example.studyhub_smda2;

public class Subject {
    private String name;
    private int folderCount;
    private int iconResId;

    public Subject(String name, int folderCount, int iconResId) {
        this.name        = name;
        this.folderCount = folderCount;
        this.iconResId   = iconResId;
    }

    public String getName()             { return name; }
    public int    getFolderCount()      { return folderCount; }
    public int    getIconResId()        { return iconResId; }
    public void   setFolderCount(int c) { folderCount = c; }
}
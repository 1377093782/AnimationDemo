package com.example.liuan.screenview.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdwinWu on 2016/11/30.
 */
public class MediaEntity {
    private List<MediaItem> mPhotoItems =new ArrayList<>();
    private int count;
    private boolean isFull;
    private int type = 0; //0表示title 1表示数据 2表示广告
    private String title;
    private String parentPath;
    private int childNum=0;
    private boolean select=false;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getChildNum() {
        return childNum;
    }

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

    public List<MediaItem> getPhotoItems() {
        return mPhotoItems;
    }

    public void addPhotoItem(MediaItem photoItem) {
        mPhotoItems.add(photoItem);
        count = mPhotoItems.size();
        if (count ==3) {
            isFull = true;
        }
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    @Override
    public String toString() {
        return "MediaEntity{" +
                "mPhotoItems=" + mPhotoItems +
                ", count=" + count +
                ", isFull=" + isFull +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", childNum=" + childNum +
                ", select=" + select +
                '}';
    }
}

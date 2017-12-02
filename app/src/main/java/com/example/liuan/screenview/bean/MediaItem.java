package com.example.liuan.screenview.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.common.assist.Check;


/**
 * Created by EdwinWu on 2016/11/30.
 */
public class MediaItem implements Comparable<MediaItem>,Parcelable {
    /**
     * 真实的文件名
     */
    private String mRealName;
    /**
     * 加密后的文件名
     */
    private String mName;
    /**
     * 真实的文件路径
     */
    private String mRealPath;
    /**
     * 加密后的文件路径
     */
    private String mPath;
    /**
     * 真实的父目录名
     */
    private String mRealParentName;
    /**
     * 真实的父路径
     */
    private String mRealParentPath;

    /**
     * 选中
     */
    private boolean mIsSelect;

    public long getDuring() {
        return mDuring;
    }

    public void setDuring(long during) {
        mDuring = during;
    }

    private long mDuring;

    public boolean isSelect() {
        return mIsSelect;
    }

    public void setSelect(boolean select) {
        mIsSelect = select;
    }

    public MediaItem() {
    }

    public String getRealParentPath() {
        return mRealParentPath;
    }

    public void setRealParentPath(String realParentPath) {
        mRealParentPath = realParentPath;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getRealPath() {
        return mRealPath;
    }

    public void setRealPath(String realPath) {
        mRealPath = realPath;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getRealParentName() {
        return mRealParentName;
    }

    public void setRealParentName(String realParentName) {
        mRealParentName = realParentName;
    }

    @Override
    public int compareTo(MediaItem another) {
        if (another ==null || Check.isEmpty(another.getRealParentPath())) {
            return -1;
        }
        if (Check.isEmpty(mRealParentPath)) {
            return 1;
        }
        int position = mRealParentPath.compareTo(another.getRealParentPath());
        if (position == 0) {
            if (Check.isEmpty(another.getRealName())) {
                return -1;
            }
            if (Check.isEmpty(mRealName)) {
                return 1;
            }
            position = another.getRealName().compareTo(mRealName);
        }
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRealName);
        dest.writeString(this.mName);
        dest.writeString(this.mRealPath);
        dest.writeString(this.mPath);
        dest.writeString(this.mRealParentName);
        dest.writeString(this.mRealParentPath);
        dest.writeByte(mIsSelect ? (byte) 1 : (byte) 0);
        dest.writeLong(this.mDuring);
    }

    protected MediaItem(Parcel in) {
        this.mRealName = in.readString();
        this.mName = in.readString();
        this.mRealPath = in.readString();
        this.mPath = in.readString();
        this.mRealParentName = in.readString();
        this.mRealParentPath = in.readString();
        this.mIsSelect = in.readByte() != 0;
        this.mDuring = in.readLong();
    }

    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>() {
        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

        @Override
        public MediaItem[] newArray(int size) {
            return new MediaItem[size];
        }
    };
}

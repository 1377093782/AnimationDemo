package com.example.liuan.screenview.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.liuan.screenview.LockActivity;
import com.example.liuan.screenview.fragment.PhotosFragment;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.adapter.ItemChildPhotoAdapter;
import com.example.liuan.screenview.adapter.ItemParentPhotoAdapter;
import com.example.liuan.screenview.bean.MediaEntity;
import com.example.liuan.screenview.bean.MediaItem;
import com.example.liuan.screenview.utils.FileManager;
import com.example.liuan.screenview.utils.MediaVaultUtil;
import com.example.liuan.screenview.utils.PreferUtils;
import com.example.liuan.screenview.view.RotateLoading;
import com.litesuits.android.log.Log;
import com.litesuits.common.assist.Check;
import com.litesuits.common.utils.SdCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

import static com.example.liuan.screenview.fragment.PhotosFragment.PHOTO_EDIT_REQUEST_CODE;


public class AddActivity extends LockActivity implements View.OnClickListener {

    public static final int LOADING_FINISH_WHAT = 100;
    public static final String CATEGORY = "添加界面";
    public String CATEGORY2 = "视频添加页面";
    public static final String ACTION = "点击";
    public static final String ACTION_LONG = "长按";
    @Bind(R.id.lv_parent)
    ListView mLvParent;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    @Bind(android.R.id.list)
    PinnedSectionListView mList;
    @Bind(R.id.rl_child)
    RelativeLayout mRlChild;
    @Bind(R.id.iv_lock)
    ImageView mIvLock;

    public List<MediaEntity> mPhotoEntities = new ArrayList<>();
    public Map<String, List<MediaItem>> mMap = new HashMap<>();
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.loading)
    RotateLoading mLoading;
    @Bind(R.id.tv_no_data)
    TextView mTvNoData;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2);
    private LoadingTask mLoadingTask;
    private LockTask mLockTask;
    private ItemChildPhotoAdapter mChildPhotoAdapter;
    private ItemParentPhotoAdapter mParentPhotoAdapter;
    private List<MediaItem> mSelectList = new ArrayList<>();
    private boolean mIsStopLock = false;
    private AlertDialog mDialog;
    private boolean mIsVideo = false;
    private int mNewCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOADING_FINISH_WHAT) {
                Object[] objects = (Object[]) msg.obj;
                if (objects.length == 2) {
                    setData((List<MediaEntity>) objects[0], (Map<String, List<MediaItem>>) objects[1]);

                } else {
                    if (mLoading.isStart()) {
                        mLoading.stop();
                        mTvNoData.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        mLoading.start();
        setImageViewEnable(false);
        if (getIntent().hasExtra("type")) {
            mIsVideo = getIntent().getBooleanExtra("type", false);
        }
        if (mIsVideo) {
            mTvNoData.setText(R.string.no_video);
            CATEGORY2 = "视频添加页面";
        } else {
            mTvNoData.setText(R.string.no_photo);
            CATEGORY2 = "图片添加页面";
        }
        mLoadingTask = new LoadingTask(this, mHandler, mIsVideo);
        mLoadingTask.executeOnExecutor(mExecutorService);
        mTvTitle.setText(R.string.album);
        mIvBack.setOnClickListener(this);
        mIvBack.setImageResource(R.drawable.ic_arrow_selector);
        mIvLock.setOnClickListener(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingTask != null) {
            mLoadingTask.cancel(true);
        }
        if (mLockTask != null) {
            mLockTask.cancel(true);
        }
        mHandler.removeMessages(LOADING_FINISH_WHAT);
        mExecutorService.shutdown();
        if (getIntent().hasExtra("from")) {
            if (TextUtils.equals("report", getIntent().getStringExtra("from"))) {
                String action = "新照片";
                if (mIsVideo) {
                    action = "新视频";
                }
                String[] videos = null;
                if (mIsVideo) {
                    PreferUtils.getNewVideoPathsArray(AddActivity.this);
                } else {
                    PreferUtils.getNewPhotoPathsArray(AddActivity.this);
                }
                if (!Check.isEmpty(videos)) {
                    for (String video : videos) {
                        File file = new File(video);
                        if (file.exists()) {
                            mNewCount++;
                        }
                    }
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data", mNewCount);
                setResult(RESULT_OK, resultIntent);
                //    Firebase.getInstance(getApplicationContext()).logEvent("每日隐私报告", action, "返回");

            }
        }
    }

    public void addSelectItem(MediaItem photoItem) {
        if (photoItem.isSelect()) {
            if (!mSelectList.contains(photoItem)) {
                mSelectList.add(photoItem);
            }
        } else {
            if (mSelectList.contains(photoItem)) {
                mSelectList.remove(photoItem);
            }
        }
        if (mSelectList.size() > 0) {
            setImageViewEnable(true);
        } else {
            setImageViewEnable(false);
        }
    }


    public void setImageViewEnable(boolean enable) {
        mIvLock.setEnabled(enable);
    }

    //设置数据
    public void setData(List<MediaEntity> photoEntities, Map<String, List<MediaItem>> map) {
        if (photoEntities != null && photoEntities.size() > 0 && map != null && map.size() > 0) {
            mPhotoEntities.clear();
            mPhotoEntities.addAll(photoEntities);
            mMap.clear();
            mMap.putAll(map);
            initChildView();
            initParentView();
            showChild(null);
            if (mLoading.isStart()) {
                mLoading.stop();
            }
        } else {
            if (mLoading.isStart()) {
                mLoading.stop();
                mTvNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initParentView() {
        mParentPhotoAdapter = new ItemParentPhotoAdapter(this, mMap);
        mLvParent.setAdapter(mParentPhotoAdapter);
        mLvParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mParentPhotoAdapter.getItem(position);
                if (mMap.get(key) != null && mMap.get(key).size() > 0) {
                    showChild(mMap.get(key).get(0).getRealParentPath());
                } else {
                    showChild(null);
                }
            }
        });
    }


    //设置listView
    public void initChildView() {
        mChildPhotoAdapter = new ItemChildPhotoAdapter(this, mPhotoEntities, mMap, true, mIsVideo);
        mChildPhotoAdapter.setActivity(this);
        mList.setAdapter(mChildPhotoAdapter);
        mChildPhotoAdapter.setClickListener(new ItemChildPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mChildPhotoAdapter.getItemViewType(position) == 0) {
                    //标题，显示父目录
                    MediaEntity entity = mChildPhotoAdapter.getItem(position);
                    if (mMap.containsKey(entity.getParentPath())) {
                        if (mParentPhotoAdapter != null) {
                            showParent(mParentPhotoAdapter.getViewPosition(entity.getParentPath()));
                        }
                    } else {
                        showParent(0);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int positon) {

            }
        });
    }

    //显示父目录
    public void showParent(int position) {
        mRlParent.setVisibility(View.VISIBLE);
        mLvParent.setSelection(position);
        mRlChild.setVisibility(View.GONE);
    }

    //显示子目录
    public void showChild(String parentPath) {
        mRlChild.setVisibility(View.VISIBLE);
        mList.setSelection(getParentPosition(parentPath));
        mRlParent.setVisibility(View.GONE);
    }

    //根据父目录路径找到childlistview对应的第一条位置
    public int getParentPosition(String parentPath) {
        int position = 0;
        if (!TextUtils.isEmpty(parentPath) && mPhotoEntities != null && mPhotoEntities.size() > 0) {
            for (int i = 0; i < mPhotoEntities.size(); i++) {
                if (mPhotoEntities.get(i).getType() == 0) {
                    continue;
                }
                if (mPhotoEntities.get(i).getPhotoItems() != null
                        && mPhotoEntities.get(i).getPhotoItems().size() > 0
                        && parentPath.equals(mPhotoEntities.get(i).getPhotoItems().get(0).getRealParentPath())) {
                    return i == 0 ? 0 : i - 1;
                }
            }
        }
        return position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.iv_lock:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isExSD()) {
                    showConfirmDialog();
                } else {
                    showMoveDialog();
                }
                break;
        }
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setMessage(String.format(getString(R.string.android_kitkat_not_message), getString(R.string.app_name)));
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.encrypt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mIsStopLock = false;
                showMoveDialog();
                //mLockTask.executeOnExecutor(mExecutorService);
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((TextView) mDialog.findViewById(android.R.id.message)).setTextColor(getResources().getColor(R.color.gray_deep));
    }

    private void showMoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_runing);
        builder.setTitle(R.string.moving);
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIsStopLock = true;
                dialog.dismiss();
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        if (!mExecutorService.isShutdown()) {
            mLockTask = new LockTask(AddActivity.this, mSelectList, mDialog, mIsVideo);
            mLockTask.executeOnExecutor(mExecutorService);

        }
        if (getIntent().hasExtra("from")) {
            if (TextUtils.equals("report", getIntent().getStringExtra("from"))) {
                String action = "新照片";
                if (mIsVideo) {
                    action = "新视频";
                }
                //      Firebase.getInstance(getApplicationContext()).logEvent("每日隐私报告", action, "加锁");

            }
        }

    }

    private boolean isExSD() {
        String inSdPath = SdCardUtil.getSDCardPath();
        File exFile = FileManager.getExternalSDCard(this);
        String exSdPath = null;
        if (exFile != null && exFile.exists()) {
            exSdPath = exFile.getAbsolutePath();
        }
        if (Check.isEmpty(exSdPath) || exSdPath.trim().equals(inSdPath.trim())) {
            return false;
        }
        for (MediaItem item : mSelectList) {
            if (item.getRealPath().startsWith(exSdPath)) {
                return true;
            }
        }
        return false;
    }

    private class LoadingTask extends AsyncTask<Void, Void, List<MediaEntity>> {
        private Context mContext;
        private List<MediaEntity> mList = new ArrayList<>();
        private HashMap<String, List<MediaItem>> mMap = new HashMap<>();
        private Handler mHandler;
        private boolean mIsVideo;

        public LoadingTask(Context context, Handler handler, boolean isVideo) {
            mContext = context;
            mHandler = handler;
            mIsVideo = isVideo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MediaEntity> doInBackground(Void... params) {
            if (mContext != null && mHandler != null) {
                List<MediaItem> list = null;
                if (mIsVideo) {
                    Log.d("video----->");
                    list = MediaVaultUtil.getDecryptedVideo(mContext);
                } else {
                    list = MediaVaultUtil.getDecryptedPhoto(mContext);
                }
                if (list != null && list.size() > 0) {
                    mList.clear();
                    mMap.clear();
                    MediaEntity photoEntity = null;
                    MediaEntity titleEntity = null;
                    List<MediaItem> mapList = null;
                    for (MediaItem item : list) {
                        item.setSelect(false);
                        String realParentPath = item.getRealParentPath();
                        if (mMap.containsKey(realParentPath)) {
                            mMap.get(realParentPath).add(item);
                            //已经添加过目录
                            if (photoEntity.isFull()) {
                                //满了，下一行添加
                                photoEntity = new MediaEntity();
                                photoEntity.setType(1);
                                photoEntity.addPhotoItem(item);
                                photoEntity.setSelect(false);
                                mList.add(photoEntity);
                            } else {
                                //没满，继续向该行添加
                                photoEntity.setSelect(false);
                                photoEntity.addPhotoItem(item);
                            }
                        } else {
                            //发现新的目录
                            titleEntity = new MediaEntity();
                            titleEntity.setType(0);
                            titleEntity.setTitle(item.getRealParentName());
                            titleEntity.setCount(1);
                            titleEntity.setParentPath(item.getRealParentPath());
                            titleEntity.setSelect(false);
                            mList.add(titleEntity);
                            photoEntity = new MediaEntity();
                            photoEntity.setType(1);
                            photoEntity.addPhotoItem(item);
                            photoEntity.setSelect(false);
                            mList.add(photoEntity);
                            mapList = new ArrayList<>();
                            mapList.add(item);
                            mMap.put(realParentPath, mapList);
                        }
                    }
                }
            }
            return mList;
        }

        @Override
        protected void onPostExecute(List<MediaEntity> list) {
            super.onPostExecute(list);
            if (mHandler != null) {
                Message message = mHandler.obtainMessage();
                Object[] objects = new Object[2];
                objects[0] = mList;
                objects[1] = mMap;
                message.obj = objects;
                message.what = PhotosFragment.LOADING_FINISH_WHAT;
                mHandler.sendMessage(message);
            }
        }
    }

    private class LockTask extends AsyncTask<Void, Void, Void> {

        private AddActivity activity;
        private AlertDialog dialog;
        private List<MediaItem> selects = new ArrayList<>();
        private boolean isVideo;

        public LockTask(AddActivity activity, List<MediaItem> selects, AlertDialog dialog, boolean isVideo) {
            this.activity = activity;
            this.dialog = dialog;
            //this.selects = selects;
            this.selects.clear();
            this.selects.addAll(selects);
            this.isVideo = isVideo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            if (selects != null && activity != null && !mIsStopLock) {
                Iterator<MediaItem> iterator = selects.iterator();
                while (iterator.hasNext() && activity != null && !mIsStopLock) {
                    MediaItem item = iterator.next();
                    if (item.isSelect()) {
                        if (isVideo) {
                            MediaVaultUtil.lockVideo(activity, item);
                        } else {
                            MediaVaultUtil.lockPhoto(activity, item);
                        }
                        iterator.remove();
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            if ((endTime - startTime) < 2000 && !mIsStopLock) {
                SystemClock.sleep(2000 - (endTime - startTime));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (activity != null) {
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }
}

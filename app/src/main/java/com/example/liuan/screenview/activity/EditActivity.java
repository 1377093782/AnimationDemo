package com.example.liuan.screenview.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.liuan.screenview.fragment.VideosFragment;
import com.example.liuan.screenview.utils.MediaVaultUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

import static com.example.liuan.screenview.fragment.PhotosFragment.PHOTO_EDIT_REQUEST_CODE;


public class EditActivity extends LockActivity implements View.OnClickListener {

    public static final String CATEGORY = "photo编辑界面";
    public static final String ACTION_LONG = "长按";
    public static final String ACTION = "点击";
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.lv_parent)
    ListView mLvParent;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    @Bind(android.R.id.list)
    PinnedSectionListView mList;
    @Bind(R.id.rl_child)
    RelativeLayout mRlChild;
    @Bind(R.id.iv_trash)
    ImageView mIvTrash;
    @Bind(R.id.iv_lock)
    ImageView mIvLock;

    private List<MediaEntity> mPhotoEntities;
    private Map<String, List<MediaItem>> mMap;
    private List<MediaItem> mSelectList = new ArrayList<>();
    private ItemChildPhotoAdapter mChildPhotoAdapter;
    private ItemParentPhotoAdapter mParentPhotoAdapter;
    private AlertDialog mDialog;
    private TrashTask mTrashTask;
    private UnLockTask mUnLockTask;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2);
    private boolean mIsStopTrash = false;
    private boolean mIsStopUnLock = false;
    private boolean mIsVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mTvTitle.setText(R.string.select_file);
        mIvBack.setImageResource(R.drawable.ic_back_selector);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        if (!getIntent().hasExtra("from")) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        if (getIntent().getStringExtra("from").equals(PhotosFragment.class.getSimpleName())) {
            mPhotoEntities = PhotosFragment.mMediaEntities;
            mMap = PhotosFragment.mMap;
            mIsVideo = false;
        } else if (getIntent().getStringExtra("from").equals(VideosFragment.class.getSimpleName())) {
            mPhotoEntities = VideosFragment.mMediaEntities;
            mMap = VideosFragment.mMap;
            mIsVideo = true;
        }

        initChildView();
        initParentView();
        showChild(null);
        mIvTrash.setOnClickListener(this);
        mIvLock.setOnClickListener(this);
        setImageViewEnable(false);
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
        mIvTrash.setEnabled(enable);
        mIvLock.setEnabled(enable);
    }

//    public void setImageViewEnableByTitle() {
//        if (mSelectList.size()>0) {
//            setImageViewEnable(true);
//        }else{
//            setImageViewEnable(false);
//        }
////        if (mPhotoEntities != null) {
////            for (PhotoEntity entity : mPhotoEntities) {
////                if (entity.getType() == 0 && entity.isSelect()) {
////                    setImageViewEnable(true);
////                    return;
////                }
////            }
////        }
////        setImageViewEnable(false);
//    }

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
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mChildPhotoAdapter.getItemViewType(position) == 0 && id != R.id.iv_select) {
//                    //标题，显示父目录
//                    PhotoEntity entity = mChildPhotoAdapter.getItem(position);
//                    if (mMap.containsKey(entity.getParentPath())) {
//                        if (mParentPhotoAdapter != null) {
//                            showParent(mParentPhotoAdapter.getViewPosition(entity.getParentPath()));
//                        }
//                    } else {
//                        showParent(0);
//                    }
//                }
//            }
//        });
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
                    return i;
                }
            }
        }
        return position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_trash:
                showTranConfirmDialog();
                break;
            case R.id.iv_lock:
                showMoveConfirmDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PHOTO_EDIT_REQUEST_CODE && resultCode==RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void showMoveConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.move_out);
        builder.setMessage(R.string.move_out_message);
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showMoveDialog();
                mIsStopUnLock = false;
                if (!mExecutorService.isShutdown()) {
                    mUnLockTask = new UnLockTask(EditActivity.this, mSelectList, mDialog, mIsVideo);
                    mUnLockTask.executeOnExecutor(mExecutorService);

                }
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void showTranConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_out);
        builder.setMessage(R.string.delete_photo_message);
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDeletingDialog();
                mIsStopTrash = false;
                if (!mExecutorService.isShutdown()) {
                    mTrashTask = new TrashTask(EditActivity.this, mSelectList, mDialog);
                    mTrashTask.executeOnExecutor(mExecutorService);
                }
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
                mIsStopUnLock = true;
                dialog.dismiss();
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    public void showDeletingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_runing);
        builder.setTitle(R.string.deleting);
        builder.setCancelable(false);
        mDialog = builder.create();
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIsStopTrash = true;
                dialog.dismiss();
            }
        });
        mDialog.show();
        mDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_normal));
        mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (MediaItem mediaItem : mSelectList) {
            mediaItem.setSelect(false);
        }
        if (mTrashTask != null) {
            mIsStopTrash = true;
            mTrashTask.cancel(true);
        }
        mExecutorService.shutdown();
    }

    public class TrashTask extends AsyncTask<Void, Void, Void> {

        //        private List<PhotoEntity> list;
//        private Map<String, List<PhotoItem>> map;
        private EditActivity activity;
        private AlertDialog dialog;
        private List<MediaItem> selects = new ArrayList<>();

        public TrashTask(EditActivity activity, List<MediaItem> selects, AlertDialog dialog) {
//            this.list = list;
//            this.map = map;
            this.activity = activity;
            this.dialog = dialog;
            //this.selects = selects;
            this.selects.clear();
            this.selects.addAll(selects);
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            if (selects != null && activity != null && !mIsStopTrash) {
                Iterator<MediaItem> iterator = selects.iterator();
                while (iterator.hasNext() && activity != null && !mIsStopTrash) {
                    MediaItem item = iterator.next();
                    if (item.isSelect()) {
                        MediaVaultUtil.trashPhoto(item);
                        iterator.remove();
                    }
                }
            }
//            if (map != null && map.size() > 0) {
//                Set<Map.Entry<String, List<PhotoItem>>> set = map.entrySet();
//                Iterator<Map.Entry<String, List<PhotoItem>>> iterator = set.iterator();
//                while (iterator.hasNext() && activity != null && !mIsStopTrash) {
//                    Map.Entry<String, List<PhotoItem>> entry = iterator.next();
//                    List<PhotoItem> list = entry.getValue();
//                    Iterator<PhotoItem> iterator1 = list.iterator();
//                    while (iterator1.hasNext() && activity != null && !mIsStopTrash) {
//                        PhotoItem item = iterator1.next();
//                        if (item.isSelect()) {
//                            PhotoVaultUtil.unlockPhoto(activity, item);
//                        }
//                    }
//                }
//            }

            long endTime = System.currentTimeMillis();
            if ((endTime - startTime) < 2000 && !mIsStopTrash) {
                SystemClock.sleep(2000 - (endTime - startTime));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
            if (activity != null) {
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }

    public class UnLockTask extends AsyncTask<Void, Void, Void> {

        private EditActivity activity;
        private AlertDialog dialog;
        private List<MediaItem> selects = new ArrayList<>();
        private boolean isVideo;

        public UnLockTask(EditActivity activity, List<MediaItem> selects, AlertDialog dialog, boolean isVideo) {
            this.activity = activity;
            this.dialog = dialog;
//            this.selects = selects;
            this.selects.clear();
            this.selects.addAll(selects);
            this.isVideo = isVideo;
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            if (selects != null && activity != null && !mIsStopUnLock) {
                Iterator<MediaItem> iterator = selects.iterator();
                while (iterator.hasNext() && activity != null && !mIsStopUnLock) {
                    MediaItem item = iterator.next();
                    if (item.isSelect()) {
                        if (mIsVideo) {
                            MediaVaultUtil.unlockVideo(activity, item);
                        } else {
                            MediaVaultUtil.unlockPhoto(activity, item);
                        }
                        iterator.remove();
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            if ((endTime - startTime) < 2000 && !mIsStopUnLock) {
                SystemClock.sleep(2000 - (endTime - startTime));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
            if (activity != null) {
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
    }
}

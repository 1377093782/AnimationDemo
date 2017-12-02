package com.example.liuan.screenview.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.liuan.screenview.BaseFragment;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.activity.AddActivity;
import com.example.liuan.screenview.activity.EditActivity;
import com.example.liuan.screenview.adapter.ItemChildPhotoAdapter;
import com.example.liuan.screenview.adapter.ItemParentPhotoAdapter;
import com.example.liuan.screenview.bean.MediaEntity;
import com.example.liuan.screenview.bean.MediaItem;
import com.example.liuan.screenview.utils.MediaVaultUtil;
import com.litesuits.common.assist.Check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

public class PhotosFragment extends BaseFragment implements View.OnClickListener {

    public static final String CATEGORY = "Photo界面";
    public static final String ACTION = "点击";
    public static final int LOADING_FINISH_WHAT = 100;
    public static final int PHOTO_EDIT_REQUEST_CODE = 200;
    public static final int PHOTO_ADD_REQUEST_CODE = 300;

    @Bind(R.id.iv_no_data)
    ImageView mIvNoData;
    @Bind(R.id.rl_no_data)
    RelativeLayout mRlNoData;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    @Bind(android.R.id.list)
    PinnedSectionListView mList;
    @Bind(R.id.rl_child)
    RelativeLayout mRlChild;
    @Bind(R.id.lv_parent)
    ListView mLvParent;
    @Bind(R.id.add)
    ImageView mAdd;
    @Bind(R.id.rl_root)
    RelativeLayout mRlRoot;
    private View mView;
    public static List<MediaEntity> mMediaEntities = new ArrayList<>();
    //private List<PhotoParentItem> mPhotoParentItems = new ArrayList<>();
    public static Map<String, List<MediaItem>> mMap = new HashMap<>();
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2);
    private LoadingTask mLoadingTask;
    private ItemChildPhotoAdapter mChildPhotoAdapter;
    private ItemParentPhotoAdapter mParentPhotoAdapter;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOADING_FINISH_WHAT) {
                Object[] objects = (Object[]) msg.obj;
                if (objects.length == 2) {
                    setData((List<MediaEntity>) objects[0], (Map<String, List<MediaItem>>) objects[1]);

                }
            }
        }
    };
    private boolean mIsEdit = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mContext instanceof Activity) {
            if (((Activity) mContext).getMenuInflater() != null) {
                ((Activity) mContext).getMenuInflater().inflate(R.menu.edit, menu);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (mMediaEntities == null || mMediaEntities.size() == 0 || mMap == null || mMap.size() == 0) {
                Toast.makeText(getContext(), R.string.no_data_string, Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent intent = new Intent(getContext(), EditActivity.class);
            intent.putExtra("from", PhotosFragment.class.getSimpleName());
            startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
       //     Firebase.getInstance(mContext).logEvent(PhotosFragment.CATEGORY, PhotosFragment.ACTION, "编辑");
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PHOTO_EDIT_REQUEST_CODE || requestCode == PHOTO_ADD_REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
            if (mLoadingTask != null) {
                mLoadingTask.cancel(true);
            }
           // Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "获取加密的数据");
            mIsEdit = true;
            if (!mExecutorService.isShutdown()) {
                mLoadingTask = new LoadingTask(getActivity(), mHandler);
                mLoadingTask.executeOnExecutor(mExecutorService);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, mView);
        showEmpty();
        return mView;
    }

    //设置数据
    public void setData(List<MediaEntity> photoEntities, Map<String, List<MediaItem>> map) {
        if (photoEntities != null && photoEntities.size() > 0 && map != null && map.size() > 0) {
            mMediaEntities.clear();
            mMediaEntities.addAll(photoEntities);
            mMap.clear();
            mMap.putAll(map);
            initChildView();
            initParentView();
            showChild(null);
        } else {
            if (photoEntities != null && photoEntities.size() > 0) {
                mMediaEntities.clear();
                mMediaEntities.addAll(photoEntities);
                initChildView();
                showChild(null);
                showOneData();
            } else {
                showEmpty();
            }
       //     Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "没有加密数据");

        }
    }

    private void showOneData() {
        mRlNoData.setVisibility(View.VISIBLE);
        mRlChild.setVisibility(View.VISIBLE);
        mRlParent.setVisibility(View.GONE);
    }

    private void initParentView() {
        mParentPhotoAdapter = new ItemParentPhotoAdapter(mContext, mMap);
        mLvParent.setAdapter(mParentPhotoAdapter);
        mLvParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mParentPhotoAdapter.getItem(position);
                if (mMap.get(key) != null && mMap.get(key).size() > 0) {
                    showChild(mMap.get(key).get(0).getRealParentPath());
                 //   Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "显示子级目录");
                    ;

                } else {
                    showChild(null);
                    //Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "显示子级目录");
                }
            }
        });
    }

    //设置listView
    public void initChildView() {
        mChildPhotoAdapter = new ItemChildPhotoAdapter(mContext, mMediaEntities, mMap, false, false);
        mChildPhotoAdapter.setFragment(this);
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
                            //Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "显示父级目录");
                           //;
                        }
                    } else {
                        showParent(0);
                      //  Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "显示父级目录");
                      //  ;
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int positon) {

            }
        });
        if (mIsEdit) {
//            addAdView(false);
            mIsEdit = false;
        }
    }

    //显示空数据
    public void showEmpty() {
        mRlNoData.setVisibility(View.VISIBLE);
        mRlChild.setVisibility(View.GONE);
        mRlParent.setVisibility(View.GONE);
    }

    //显示父目录
    public void showParent(int position) {
        mRlParent.setVisibility(View.VISIBLE);
        mLvParent.setSelection(position);
        mRlNoData.setVisibility(View.GONE);
        mRlChild.setVisibility(View.GONE);
    }

    //显示子目录
    public void showChild(String parentPath) {
        mRlChild.setVisibility(View.VISIBLE);
        mList.setSelection(getParentPosition(parentPath));
        mRlParent.setVisibility(View.GONE);
        mRlNoData.setVisibility(View.GONE);

    }

    //根据父目录路径找到childlistview对应的第一条位置
    public int getParentPosition(String parentPath) {
        int position = 0;
        if (!TextUtils.isEmpty(parentPath) && mMediaEntities != null && mMediaEntities.size() > 0) {
            for (int i = 0; i < mMediaEntities.size(); i++) {
                if (mMediaEntities.get(i).getType() == 0) {
                    continue;
                }
                if (mMediaEntities.get(i).getPhotoItems() != null
                        && mMediaEntities.get(i).getPhotoItems().size() > 0
                        && parentPath.equals(mMediaEntities.get(i).getPhotoItems().get(0).getRealParentPath())) {
                    return i;
                }
            }
        }
        return position;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingTask = new LoadingTask(getContext().getApplicationContext(), mHandler);
        mLoadingTask.executeOnExecutor(mExecutorService);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingTask != null) {
            mLoadingTask.cancel(true);
        }
        mExecutorService.shutdown();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeMessages(LOADING_FINISH_WHAT);
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivityForResult(intent, PHOTO_ADD_REQUEST_CODE);
        //    Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "添加photo");
           // ;
        }
    }

    private class LoadingTask extends AsyncTask<Void, Void, List<MediaEntity>> {
        private Context mContext;
        private List<MediaEntity> mList = new ArrayList<>();
        private HashMap<String, List<MediaItem>> mMap = new HashMap<>();
        private Handler mHandler;

        public LoadingTask(Context context, Handler handler) {
            mContext = context;
            mHandler = handler;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MediaEntity> doInBackground(Void... params) {
            if (mContext != null && mHandler != null) {
                List<MediaItem> list = MediaVaultUtil.getEncryptedPhoto(mContext);
//                MediaEntity mediaEntity = new MediaEntity();
//                mediaEntity.setType(2);
//                mList.add(0, mediaEntity);
                if (!Check.isEmpty(list)) {
                    mList.clear();
                    mMap.clear();
//                    mList.add(0, mediaEntity);
                    MediaEntity photoEntity = null;
                    int parentNum = 0;
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
//                            titleEntity.setChildNum(childNum);
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
//                            titleEntity.setChildNum(childNum);
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

//    public void addAdView(final boolean isPhotoShow) {
//        if (!AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded(NativeAdType.PHOTO_LIST)) {
//            AdAppHelper.getInstance(getApplicationContext()).loadNewNative(NativeAdType.PHOTO_LIST);
//        }
//        int time = 500;
//        if (isPhotoShow) {
//            time = 0;
//        }
//        if (AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded(NativeAdType.PHOTO_LIST) || AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150)!=null) {
//            if (mRlChild != null) {
//                mRlChild.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mChildPhotoAdapter != null && mChildPhotoAdapter.mAdItemView != null) {
//                            mChildPhotoAdapter.mAdItemView.setTag(R.id.ad_which, NativeAdType.PHOTO_LIST);
//                            if (!isPhotoShow) {
//                                mChildPhotoAdapter.starAnimator(mChildPhotoAdapter.mAdItemView);
//                            } else {
//                                mChildPhotoAdapter.onlyAddAdView(mChildPhotoAdapter.mAdItemView);
//                            }
//
//                        }
//                    }
//                }, time);
//            }
//            if (mIsEdit) {
//                Firebase.getInstance(getApplicationContext()).logEvent("广告", "广告准备好", "Photos_编辑后界面native");
//
//            }
//        } else {
//            if (mIsEdit) {
//                Firebase.getInstance(getApplicationContext()).logEvent("广告", "广告没准备好", "Photos_编辑后界面native");
//
//            }
//        }
//        if (mIsEdit) {
//            Firebase.getInstance(getApplicationContext()).logEvent("广告", "show", "Photos_编辑后界面native");
//
//        }
//    }

}

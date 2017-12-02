package com.example.memorycleaner;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

	private static final String meminfoPath = "/proc/meminfo";

	private static final String TAG = "MainActivity";

	private Button mBtnClean;
	private TextView mMemUesed;
	private Context mContext;

	int delaytime = 50;

	private float memUsed;
	private float memTotal;

	private MeminfoFileObserver mMeminfoFileObserver;
	private ImageView mIvMeminfo;

	private ClipDrawable drawable;
	private int mLevel;

	private Handler handler = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			dataRefresh();
			handler.postDelayed(this, delaytime);
		}

	};

	private boolean animaling = false;

	private boolean downing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	protected void dataRefresh() {
		// TODO Auto-generated method stub

		if (animaling) {
			if (mLevel > 0 && downing) {
				drawable.setLevel(mLevel);
				mLevel -= 100;
			} else {
				memUsed = 100 * (memTotal - getAvailMemory(mContext))/ memTotal;
				downing = false;
				// mLevel=0;
				if (mLevel < 0)
					mLevel = 0;
			}

			// Log.d(TAG, "mLevel = "+memUsed);
			// Log.d(TAG, "animaling   memUsed = "+memUsed);

			if (mLevel < memUsed * 100 && !downing) {
				// Log.d(TAG, "mLevel = "+memUsed);
				// Log.d(TAG, "memUsed = "+memUsed);

				mLevel = mLevel + 100;
				// Log.d(TAG, "animaling   mLevel = "+mLevel);
				// Log.d(TAG, "animaling   memUsed*100 = "+memUsed*100);

				drawable.setLevel(mLevel);

				if (mLevel > memUsed * 100) {
					animaling = false;
				}
			}

			mMemUesed.setText((int) mLevel / 100 + "%");

			return;
		}

		if (!animaling) {
			// Log.d(TAG,
			// "getAvailMemory(mContext) = "+getAvailMemory(mContext));
			memUsed = 100 * (memTotal - getAvailMemory(mContext)) / memTotal;
			//Log.d(TAG, "==========memUsed = " + memUsed);
			drawable.setLevel((int) memUsed * 100);
			mMemUesed.setText(((int) memUsed) + "%");
			return;
		}
		// updateUI();
	}

	private void init() {
		// TODO Auto-generated method stub
		mMeminfoFileObserver = new MeminfoFileObserver(meminfoPath);
		mMeminfoFileObserver.startWatching();

		mContext = getApplicationContext();
		mIvMeminfo = (ImageView) findViewById(R.id.clean_schedule_img);
		mBtnClean = (Button) findViewById(R.id.btn_clear);

		mMemUesed = (TextView) findViewById(R.id.clean_value_tip);

		mBtnClean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// freeMemory();
				// Log.d(TAG,
				// "getAvailMemory(mContext) = "+getAvailMemory(mContext));
				// Log.d(TAG,
				// "getTotalMemory(mContext) = "+getTotalMemory(mContext));
				freeMemory();

				mLevel = drawable.getLevel();
				downing = true;
				animaling = true;

				// animalMemifo();
				// Log.d(TAG,
				// "getAvailMemory(mContext) = "+getAvailMemory(mContext));
			}
		});

		memTotal = getTotalMemory(mContext);
		Log.d(TAG, "memTotal = " + memTotal);
		drawable = (ClipDrawable) mIvMeminfo.getDrawable();

	
		animaling = false;

		dataRefresh();

		handler.postDelayed(task, delaytime);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class MeminfoFileObserver extends FileObserver {

		private static final String TAG = "MeminfoFileObserver";

		public MeminfoFileObserver(String path) {
			super(path);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onEvent(int event, String path) {
			// TODO Auto-generated method stub

			// Log.d(TAG,"onEvent--------event="+event);

			switch (event) {

			case android.os.FileObserver.ATTRIB:
				// 文件被修改
				/**
				 * 相关操作
				 * 
				 */
				// Log.d(TAG,"onEvent file modify");
				break;

			}
		}

	};

	void freeMemory() {
		ActivityManager activityManger = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger
				.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);

				// System.out.println("pid            "+apinfo.pid);
				// System.out.println("processName              "+apinfo.processName);
				// System.out.println("importance            "+apinfo.importance);
				String[] pkgList = apinfo.pkgList;

				if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
					// Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						// 2.2以上是过时的,请用killBackgroundProcesses代替
						// activityManger.restartPackage(pkgList[j]);
						activityManger.killBackgroundProcesses(pkgList[j]);
						Log.i(TAG, "kill " + pkgList[j]);
					}
				}
			}
	}

	private long getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		// return Formatter.formatFileSize(context, initial_memory);//
		// Byte转换为KB或者MB，内存大小规格化
		return initial_memory / (1024 * 1024);
	}

	private long getAvailMemory(Context context) {
		// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		// return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
		return mi.availMem / (1024 * 1024);
	}
}

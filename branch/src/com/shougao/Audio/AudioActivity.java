package com.shougao.Audio;

import java.util.List;

import com.shougao.Audio.component.ScrollableViewGroup;
import com.shougao.Audio.media.IMediaService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity implements OnClickListener, Runnable {
	/** Called when the activity is first created. */
	boolean isOver = true;//用于启动画面
	boolean exitFLG = false;// 标示推出Activity
	int intPlayMode = 1;// 初始化顺序播放
//	private String pref_playMode = "pref_playMode";
	int intPlayState = 0; // 0 stop, 1play.
	int runThread = 1;// 控制刷新seekbar线程，与退出activity同步，1.表示在运行。
	private LinearLayout screenup = null;
	private ImageButton btnPlayMode, btnPlay, btnNext, btnList, ImgLyric,
			IndMenu, btnPrev;
	private ImageButton IndMain = null;
	private ImageView vPlayMode, vPlay;
	private IMediaService localMediaService = null;
	private ListView musicListView;
	private ArrayAdapter<String> adapter = null;
	private SeekBar mSeekBar = null;
	private TextView currentProcessText = null;
	private TextView currentDurationText = null;
	private TextView mTitle = null;
	private TextView mArtist = null;
	private TextView mAlbum = null;
	private TextView mComment = null;
	private TextView mNumber = null;
	private int totalTime;
	private Handler mPercentHandler = new Handler();// 在initplayer中使用HandlerThread.getLooper初始化
	private HandlerThread handlerThread = new HandlerThread("updateSeekTime");
	ScrollableViewGroup viewGroup = null;
	private String currentPlayAudio = null;
//	private static String PREF = "audio_pref";
	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏程序的标题栏，豆豆音乐播放器几个字
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.main);		
		screenup=(LinearLayout)findViewById(R.id.screenup);
		

		mContext = this;
		mTitle = (TextView) findViewById(R.id.musicTitle);
		mArtist = (TextView) findViewById(R.id.musicArtist);
		mAlbum = (TextView) findViewById(R.id.musicAlbum);
		mNumber = (TextView) findViewById(R.id.musicNumber);
		mComment = (TextView) findViewById(R.id.musicCommon);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrev = (ImageButton) findViewById(R.id.btnPrev);
		IndMain = (ImageButton) findViewById(R.id.IndMain);
		btnPlayMode = (ImageButton) findViewById(R.id.IndPlayMode);
		btnList = (ImageButton) findViewById(R.id.ImgList);
		viewGroup = (ScrollableViewGroup) findViewById(R.id.ViewFlipper);
		ImgLyric = (ImageButton) findViewById(R.id.ImgLyric);
		IndMenu = (ImageButton) findViewById(R.id.IndMenu);
		musicListView = (ListView) findViewById(R.id.PlayList);
		musicListView.setOnItemClickListener(musicListOnItemClickListenerClick);
		vPlayMode = (ImageView) findViewById(R.id.imgPlayMode);
		vPlay = (ImageView) findViewById(R.id.imgPlay);
		btnPlay.setOnClickListener(this);
		btnPlayMode.setOnClickListener(this);
		btnList.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		ImgLyric.setOnClickListener(this);
		IndMenu.setOnClickListener(this);
		IndMain.setOnClickListener(this);
		bindService(new Intent("com.shougao.Audio.REMOTE_SERVICE"),
				mServiceConn, Context.BIND_AUTO_CREATE);
		mSeekBar = (SeekBar) findViewById(R.id.skbGuage);
		currentProcessText = (TextView) findViewById(R.id.txtLapse);
		currentDurationText = (TextView) findViewById(R.id.txtDuration);
		mSeekBar.setOnSeekBarChangeListener(mSeekBarOnClickListener);
		// mComment.setVisibility(View.GONE);
		System.out.println("debug....=============thread2======"
				+ Thread.currentThread().getId());
		
//		restorePref();
		
//		/**
//		 * 如果保存了推出前的内容，则使用之前的内容赋值
//		 * 值是在onSaveInstanceState中保存的，保存到一个bundle结构中
//		 */
//		if(savedInstanceState != null){
//			System.out.println("intPlayMode:1:" + intPlayMode);
//			intPlayMode = savedInstanceState.getInt("intPlayMode");
//			System.out.println("intPlayMode:2:" + intPlayMode);
//		}
		
		

		new Thread(this).start();
	}

	/**
	 * 添加启动画面功能，包括从oncreate中的Thread start，run，screenHandler， show。
	 * 单独起这个线程完成启动画面的显示
	 * 2011-8-25
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isOver = true;
		show();
	}
	
    private void show(){
        Message msg=new Message();
        if(isOver){
            msg.what=0;
            screenHandler.sendMessage(msg);
        }else{
            msg.what=1;
            screenHandler.sendMessage(msg);
        }
    }
    
    Handler screenHandler = new Handler(){
    	
    	@Override
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		switch(msg.what){
    		case 0://隐藏
    			System.out.println("Activity......gone");
    			screenup.setVisibility(View.GONE);
    			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    			break;
    		case 1:
    			 Toast.makeText(getApplicationContext(), "加载中", Toast.LENGTH_SHORT).show();
    			 System.out.println("Activity......show");
                 break;
    		}
    	}
    };
    
	public void onStart(){
		super.onStart();
		System.out.println("Activity...onStart");
	}
	
//	/**
//	 * android两种保存状态的方式，onSaveInstanceState和getSharedPreferences
//	 * 这个是第二种
//	 * 2011-8-24
//	 */
//	private void restorePref() {
//		// TODO Auto-generated method stub
//		SharedPreferences setting = getSharedPreferences(PREF,0);
//		int intplaymode = setting.getInt(pref_playMode, 1);
//		intPlayMode = intplaymode;
//	}

	/*
	 * 完成横竖屏切换 2011-8-15 (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// 检测屏幕的方向：纵向或横向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 当前为横屏， 在此处添加额外的处理代码
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 当前为竖屏， 在此处添加额外的处理代码
		}
		// 检测实体键盘的状态：推出或者合上
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			// 实体键盘处于推出状态，在此处添加额外的处理代码
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			// 实体键盘处于合上状态，在此处添加额外的处理代码
		}
	}

	private ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			localMediaService = IMediaService.Stub.asInterface(service);
			System.out.println("DEBUG>>>ServiceConnection.");
			initPlayer();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			localMediaService = null;
			System.out.println("DEBUG>>>ServiceDisConnection.");
		}
	};

	OnSeekBarChangeListener mSeekBarOnClickListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			int process = mSeekBar.getProgress();
			int duration = 0;
			try {
				duration = localMediaService.getDuration();
				localMediaService.seek(duration * process / 100);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	/**
	 * 初始化mediaplayer中系统自带的mediaplayer信息
	 */
	protected void initPlayer() {
		// 初始化service中的meidaplayer
		System.out.println("debug......initPlayer");
		// TODO Auto-generated method stub
		try {
			localMediaService.initPlayer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 显示播放列表
		try {
			adapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_list_item_1,
					localMediaService.getPlayList());
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		musicListView.setAdapter(adapter);
	}

	/*
	 * 这个需要实时更新，在play,和onitemclick的第一次启动，目的是为了担心两种方式没有启动。 这个是和activity在一个线程中
	 * 2011-8-10
	 * update:
	 * 需要在回到homescreen之后长按home键onResume刷新这个时间，再次调用
	 */
	public void updateSeekBar() {
		System.out.println("debug...updateSeekBar1");
		mPercentHandler.post(updateSeekbar);
	}

	Runnable updateSeekbar = new Runnable() {
		
		@Override
		public void run() {
			System.out.println("debug...updateSeekBar2");
			String value = null;
			int position = 0;
			// TODO Auto-generated method stub
			if (exitFLG) {
				return;
			}
			System.out.println(exitFLG);
			try {
				position = localMediaService.getCurrentProcess() / 1000;// 当前播放位置
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("debug....====" + position);
			if (totalTime == 0) {
				return;
			}
			if (totalTime != 0) {
				mSeekBar.setProgress(100 * position / totalTime);
			}
			if ((position % 60 < 9) || (position % 60 == 9)) {
				value = (String.valueOf(position / 60) + ":" + "0" + String
						.valueOf(position % 60));
			} else {
				value = (String.valueOf(position / 60) + ":" + String
						.valueOf(position % 60));
			}
			System.out.println("value:" + value);
			currentProcessText.setText(value);
			if (exitFLG == true) {
				// mPercentHandler.removeCallbacks(updateSeekbar);
			}
			// updateDurationTime();//为了实现播放结束后自动播放下一曲时，自动更新下一曲的持续时间。
			updateInfo();
			mPercentHandler.postDelayed(updateSeekbar, 1000);
			// 更新播放信息，在线程中，这个超级费时间，拖死ACTIVITY
			// 做一个时间判断函数，一旦播放结束更新。
			System.out.println("position == totalTime:" + position + ":"
					+ totalTime);
			if (position == totalTime) {
				System.out.println("debug...Thread");
				updateInfo();
				System.out.println("debug....=============thread======"
						+ Thread.currentThread().getId());
			}
		}
	};

	/*
	 * onItemClick 参数： AdapterView<?>
	 * arg0代表向spinner中加载的一系列字符串，是一个适配器，是字符串和Spinner之间的桥梁
	 * 如果需要访问与被选项相关的数据，执行程序可以调用getItemAtPosition(position)。 parent
	 * 发生点击动作的AdapterView。 view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
	 * position　视图在adapter中的位置。 id 被点击元素的行id。
	 */
	OnItemClickListener musicListOnItemClickListenerClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> listView, View v, int position,
				long id) {
			System.out.println("!!!!!position:" + position);
			try {
				localMediaService.passSelectedFile(position);
				updateInfo();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 改变播放状态
			if (intPlayState == 0) {
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
			}
			updateSeekBar();
		}
	};

	/*
	 * 获得歌曲详细信息，只显示3个信息
	 */
	private void updateMp3Info() {
		try {
			List<String> musicInfo = localMediaService.getMp3Info();
			if (musicInfo.get(0) != null) {
				mTitle.setText("title:" + musicInfo.get(0).toString());
			} else {
				mTitle.setVisibility(View.GONE);
			}
			if (musicInfo.get(0) != null) {
				mArtist.setText("artist:" + musicInfo.get(1).toString());
			} else {
				mArtist.setVisibility(View.GONE);
			}
			if (musicInfo.get(0) != null) {
				mAlbum.setText("album:" + musicInfo.get(2).toString());
			} else {
				mAlbum.setVisibility(View.GONE);
			}
			try {
				currentPlayAudio = localMediaService.getCurrentPlayAudio();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mNumber.setText(currentPlayAudio);
			// if(musicInfo.get(0) != null){
			// mComment.setText(musicInfo.get(3).toString());
			// }else{
			// mComment.setVisibility(View.GONE);
			// }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * 更新播放文件持续时间。
	 * 
	 * @author:zhangqingcai0815@gmail.com
	 */
	private void updateDurationTime() {
		// TODO Auto-generated method stub
		String duration = null;
		try {
			totalTime = localMediaService.getDuration() / 1000;
		} catch (RemoteException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		if ((totalTime % 60 < 9) || (totalTime % 60 == 9)) {
			duration = String.valueOf(totalTime / 60) + ":" + "0"
					+ String.valueOf(totalTime % 60);
		} else {
			duration = String.valueOf(totalTime / 60) + ":"
					+ String.valueOf(totalTime % 60);
		}
		currentDurationText.setText(duration);
	}

	/*
	 * 更新歌曲信息，包括从，上一曲、下一曲、播放完成下一曲、点击播放、时更新的信息,seekbar的实时时间进度。 内容有，歌曲持续时间、歌曲信息。
	 */
	public void updateInfo() {
		updateMp3Info();
		updateDurationTime();
		// updateSeekBar();线程单独执行
	}

	@Override
	public void onClick(View v) {
		//判断是不是没有音乐文件，没有加载完也会认为没有文件
		if(adapter == null){
			return;
		}
		if(0 == adapter.getCount()){
			Toast.makeText(getApplicationContext(), "尚未搜索到mp3文件...", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.IndMain:
			viewGroup.snapToScreen(0);// 跳转到第一屏幕
			break;
		case R.id.btnPlay:
			System.out.println("=========adapter:" + adapter.getCount());

			switch (intPlayState) {
			case 0:
				// System.out.println("========= 0  :" + intPlayState);
				try {
					localMediaService.play();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateInfo();
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
				intPlayState = 1;
				// 开始执行seekbar的线程，这个线程一旦启动将一直执行
				updateSeekBar();
				break;
			case 1:
				// System.out.println("=========1 :" + intPlayState);
				try {
					localMediaService.pause();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				vPlay.setImageResource(R.drawable.img_playback_bt_play);
				intPlayState = 0;
				break;
			}
			break;
		case R.id.btnNext:
			System.out.println("DEBUG>>>next");
			try {
				localMediaService.next();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateInfo();
			break;

		case R.id.btnPrev:
			System.out.println("DEBUG>>>prevent");
			// CurrentPlayMode pm = new CurrentPlayMode();
			// System.out.println(":" + pm.getPlayMode());
			try {
				localMediaService.prev();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateInfo();
			// mPercentHandler.removeCallbacks(handlerThread);
			break;

		case R.id.ImgList:
			System.out.println("DEBUG>>>ImgList=========");
			viewGroup.snapToScreen(1);// 跳转到第2个屏幕
			// try {
			// adapter = new ArrayAdapter<String>(getApplicationContext(),
			// android.R.layout.simple_list_item_1,
			// localMediaService.getPlayList());
			// } catch (RemoteException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// musicListView.setAdapter(adapter);
			break;

		case R.id.IndPlayMode:
			intPlayMode = intPlayMode + 1;
			intPlayMode = (intPlayMode % 4);
			System.out.println("========current play mode:" + intPlayMode);
			switch (intPlayMode) {
			case 1:
				vPlayMode.setImageResource(R.drawable.icon_playmode_normal);
				Toast.makeText(getApplicationContext(), "顺序播放",
						Toast.LENGTH_SHORT).show();
				try {
					localMediaService.setRepeatMode();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				vPlayMode.setImageResource(R.drawable.icon_playmode_repeat);
				Toast.makeText(getApplicationContext(), "循环播放",
						Toast.LENGTH_SHORT).show();
				try {
					localMediaService.setRepeatMode();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3:
				vPlayMode
						.setImageResource(R.drawable.icon_playmode_repeat_single);
				Toast.makeText(getApplicationContext(), "单曲重复",
						Toast.LENGTH_SHORT).show();
				try {
					localMediaService.setRepeatMode();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 0:
				vPlayMode.setImageResource(R.drawable.icon_playmode_shuffle);
				Toast.makeText(getApplicationContext(), "随机播放",
						Toast.LENGTH_SHORT).show();
				try {
					localMediaService.setRepeatMode();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			break;

		case R.id.ImgLyric:
			System.out.println("========imglyric");
			System.out.println("DEBUG>>> main thread"
					+ Thread.currentThread().getId());
			viewGroup.snapToScreen(2);// 跳转到第三个屏幕
			break;

		case R.id.IndMenu:
			// menu 菜单， 添加about和exit
			showInfo();
			break;
		}
	}

	/**
	 * 提示退出系统
	 * 
	 */
	public void showInfo() {
		new AlertDialog.Builder(this)
				.setTitle("info")
				.setMessage("确认退出？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(),
								"欢迎使用！" + "tel:15010611780", Toast.LENGTH_LONG)
								.show();
						exitPlayer();
					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("返回");
					}
				}).show();
	}



	/**
	 * 添加菜单功能，使用menu.add添加
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "about").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "exit").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
//	/**
//	 * 追加菜单点击操作处理的时候，覆盖onMenuItemSelected()方法
//	 */
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		// TODO Auto-generated method stub
//		if (item.getItemId() == 1) {
//			finish();
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}

	/**
	 * 对键盘menu键的响应处理
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case Menu.FIRST + 1:
		// Toast.makeText(this, "about" + "\n" + "mp3音乐播放器" + "\n" + "作者：张庆财",
		// Toast.LENGTH_LONG).show();
		case Menu.FIRST + 1:
			openOptionsDialog();
			break;
		case Menu.FIRST + 2:
			exitPlayer();
			break;
		}
		return false;
	}

	/**
	 * 添加menu中的关于对话框功能
	 */
	private void openOptionsDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("        简介")
				.setMessage(R.string.app_about_msg1)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	/**
	 * 退出player之前的清理对象，标志位设置
	 */
	private void exitPlayer() {
		exitFLG = true;// 推出player之后就不能在读取service中数据，必须放到exitPlayer之前
		if (runThread == 1) {
			runThread = 0;
		}
		// TODO Auto-generated method stub
		try {
			localMediaService.stop();
			localMediaService.release();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unbindService(mServiceConn);
		System.out.println("Activity...unBindService");
		try {
			finish();
			System.out.println("Activity...finish");
		} catch (Exception e) {
		}
	}
	
	/**
	 * 这是个自动执行函数，在OnOptionMenu执行之后执行
	 */
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "谢谢您的使用！" + "tel:15010611780", Toast.LENGTH_LONG)
				.show();
	}

	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("Activity...onRestoreInstanceState");
		
	}

	/**
	 * 为了保证程序正确性， 为了再次打开程序和原来打开过的一样，
	 * 在activity运行到onPause或者onStop状态时，先保存资料，写上持久层操作代码
	 * 然后在onCreate时读出来
	 * 2011-8-24
	 * 记录之前的运行结果还没有实现
	 */
	@Override
	public void onPause(){
		System.out.println("Activity...onPause.");
		super.onPause();
		
//		SharedPreferences setting = getSharedPreferences(PREF, 0);
//		Editor editor = setting.edit();
//		editor.putInt(pref_playMode, intPlayMode);
//		editor.commit();
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume(){
		super.onResume();
//		updateSeekBar();
		System.out.println("Activity...onResume.");
	}
	/**
	 * 维护一个map对象，存放被回收的数据内容，也可能不回收，使用前判断null
	 * 内存被回收，则重新启动需要调用带参数的onCreate
	 */
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("intPlayMode", intPlayMode);
	}

	public void onStop() {
		mPercentHandler.removeCallbacks(updateSeekbar);
		exitFLG = true;
		super.onStop();
		System.out.println("Activity...onStop.");
	}
	
	/**
	 * onStop->onRestart->onStart->onResume(回到原来程序过程)
	 * 2011-8-25
	 * 为了解决onStop后能继续显示信息,添加updateSeekBar和updageSeekBar的控制变量exitFLG
	 */
	@Override
	public void onRestart(){
		super.onRestart();
		updateSeekBar();
		exitFLG = false;
		System.out.println("Activity...onRestart.");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Activity...onDestroy.");
		// android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 屏蔽了返回键执行默认的stop， destroy
	 * 实现方式1.程序只执行stop并回到home
	 * 实现方式2.程序弹出退出确认框
	 * 
	 * 2011-8-24
	 */
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event) {  
		if(keyCode ==  event.KEYCODE_BACK && event.getRepeatCount() == 0){
//			Intent home = new Intent(Intent.ACTION_MAIN);//方式1
//			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			home.addCategory(Intent.CATEGORY_HOME);
//			startActivity(home);
			showInfo();//方式2
			System.out.println("Activity...onBack.");
		}
		return super.onKeyDown(keyCode, event);//如果不是back键正常响应
	}
}







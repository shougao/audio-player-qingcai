package com.shougao.Audio;

import java.util.ArrayList;
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
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	static boolean exitFLG = false;//标示推出Activity
	static int intPlayMode = 1;//初始化顺序播放
	static int intPlayState = 0; // 0 stop, 1play.
	static int runThread = 1;//控制刷新seekbar线程，与退出activity同步，1.表示在运行。
	private ImageButton btnPlayMode, btnPlay, btnNext, btnList, ImgLyric,
			IndMenu, btnPrev;
	private ImageView vPlayMode, vPlay;
	private IMediaService localMediaService =null;
	private ListView musicListView;
	private ArrayAdapter<String> adapter = null;
	private SeekBar mSeekBar= null;
	private TextView currentProcessText = null;
	private TextView currentDurationText = null;
	private TextView mTitle = null;
	private TextView mArtist = null;
	private TextView mAlbum = null;
	private TextView mComment = null;
	private static int totalTime; 
	private static Handler mPercentHandler = new Handler();//在initplayer中使用HandlerThread.getLooper初始化
	ScrollableViewGroup viewGroup = null; 
	HandlerThread handlerThread = new HandlerThread("updateSeekTime");
	private List<String> musicInfo = null;
	Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		mTitle  = (TextView)findViewById(R.id.musicTitle);
		mArtist  = (TextView)findViewById(R.id.musicArtist);
		mAlbum  = (TextView)findViewById(R.id.musicAlbum);
		mComment  = (TextView)findViewById(R.id.musicGenere);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrev = (ImageButton) findViewById(R.id.btnPrev);
		btnPlayMode = (ImageButton) findViewById(R.id.IndPlayMode);
		btnList = (ImageButton) findViewById(R.id.ImgList);
		viewGroup = (ScrollableViewGroup)findViewById(R.id.ViewFlipper);
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
		bindService(new Intent("com.shougao.Audio.REMOTE_SERVICE"),
				mServiceConn, Context.BIND_AUTO_CREATE);
		mSeekBar = (SeekBar)findViewById(R.id.skbGuage);
		currentProcessText = (TextView)findViewById(R.id.txtLapse);
		currentDurationText = (TextView)findViewById(R.id.txtDuration);
		mSeekBar.setOnSeekBarChangeListener(mSeekBarOnClickListener);
		
	}
	/*
	 * 完成横竖屏切换
	 * 2011-8-15
	 * (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {	 
		super.onConfigurationChanged(newConfig); 
		// 检测屏幕的方向：纵向或横向 
		if (this.getResources().getConfiguration().orientation  
				== Configuration.ORIENTATION_LANDSCAPE) { 
			//当前为横屏， 在此处添加额外的处理代码 
		} 
		else if (this.getResources().getConfiguration().orientation  
				== Configuration.ORIENTATION_PORTRAIT) { 
			//当前为竖屏， 在此处添加额外的处理代码 
		} 
		//检测实体键盘的状态：推出或者合上     
		if (newConfig.hardKeyboardHidden  
				== Configuration.HARDKEYBOARDHIDDEN_NO){  
			//实体键盘处于推出状态，在此处添加额外的处理代码 
		}  
		else if (newConfig.hardKeyboardHidden 
				== Configuration.HARDKEYBOARDHIDDEN_YES){  
			//实体键盘处于合上状态，在此处添加额外的处理代码 
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

	OnSeekBarChangeListener mSeekBarOnClickListener = new OnSeekBarChangeListener(){
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
				localMediaService.seek(duration * process/100);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};
	protected void initPlayer() {
//		初始化service中的meidaplayer
		System.out.println("debug......initPlayer");
		// TODO Auto-generated method stub
		try {
			localMediaService.initPlayer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		显示播放列表
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
	

	public void updateSeekBar(){
//		mPercentHandler = new Handler(handlerThread.getLooper());
		mPercentHandler.post(updateSeekbar);
	}

	Runnable updateSeekbar = new Runnable(){
		
		@Override
		public void run() {
			System.out.println("debug....=============hghhhhhhhhhhh======..null");
			String value = null;
			int position = 0;
			// TODO Auto-generated method stub
			try {
				position = localMediaService.getCurrentProcess()/1000;//当前播放位置
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(totalTime == 0){
				return;
			}
			if(totalTime != 0){
				mSeekBar.setProgress(100*position/totalTime);
			}
			if((position%60 < 9)||(position%60 == 9)){
				value = (String.valueOf(position/60) +":"+ "0" + String.valueOf(position%60));
			}else{
				value = (String.valueOf(position/60) +":"+ String.valueOf(position%60));
			}
			currentProcessText.setText(value);
			if(exitFLG == true){
				mPercentHandler.removeCallbacks(updateSeekbar);
			}
			System.out.println("exitFLG" + exitFLG);
			updateDurationTime();//为了实现播放结束后自动播放下一曲时，自动更新下一曲的持续时间。
			mPercentHandler.postDelayed(updateSeekbar, 1000);
		}
	};
	
	
	/* 
	 * onItemClick 参数：
	 * AdapterView<?> arg0代表向spinner中加载的一系列字符串，是一个适配器，是字符串和Spinner之间的桥梁
	 *  如果需要访问与被选项相关的数据，执行程序可以调用getItemAtPosition(position)。
	 *		parent  发生点击动作的AdapterView。
	 *		view 在AdapterView中被点击的视图(它是由adapter提供的一个视图)。
	 *		position　视图在adapter中的位置。
	 *		id 被点击元素的行id。
	 */
	OnItemClickListener musicListOnItemClickListenerClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
			System.out.println("!!!!!position:" + position);
			try {
				localMediaService.passSelectedFile(position);
				System.out.println("position:" + position);
				updateDurationTime();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//改变播放状态
			if(intPlayState ==0){
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
			}
		}
	};
//	ArrayList<String> mp3Info = null;
	private void updateMp3Info(){
		try {
			musicInfo = localMediaService.getMp3Info();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("================================");
		System.out.println(musicInfo.indexOf(1)+"=======================================================99999999999");
//		mTitle.setText(musicInfo.indexOf(0));
		mArtist.setText("asdkfjkkjkj");
//		mAlbum.setText(musicInfo.indexOf(2));
//		mComment.setText(musicInfo.indexOf(3));
	}
	
	/*
	 * 更新播放文件持续时间。
	 * @author:zhangqingcai0815@gmail.com
	 */
	private void updateDurationTime() {
		// TODO Auto-generated method stub
		String duration = null;
		try {
			totalTime = localMediaService.getDuration()/1000;
		} catch (RemoteException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		if((totalTime%60 <9)||(totalTime%60 == 9)){
			duration = String.valueOf(totalTime/60)+":"+"0" + String.valueOf(totalTime%60);
		}else{
			duration = String.valueOf(totalTime/60)+":"+String.valueOf(totalTime%60);
		}
		currentDurationText.setText(duration);
	}
	//监听拖动条


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			System.out.println("=========intPlayState:" + intPlayState);
			switch (intPlayState) {
			case 0:
				// System.out.println("========= 0  :" + intPlayState);
				try {
					localMediaService.play();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				updateSeekBar();
				updateDurationTime();
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
				intPlayState = 1;
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
			updateMp3Info();
			try {
				localMediaService.next();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateDurationTime();
			break;
			
		case R.id.btnPrev:
			System.out.println("DEBUG>>>prevent");
//			CurrentPlayMode pm = new CurrentPlayMode();
//			System.out.println(":" + pm.getPlayMode());
			try {
				localMediaService.prev();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateDurationTime();
			break;

		case R.id.ImgList:
			System.out.println("DEBUG>>>ImgList=========");
//			try {
//				adapter = new ArrayAdapter<String>(getApplicationContext(),
//						android.R.layout.simple_list_item_1,
//						localMediaService.getPlayList());
//			} catch (RemoteException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			musicListView.setAdapter(adapter);
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
			System.out.println("DEBUG>>> main acitvity thread"
					+ Thread.currentThread().getId());
			break;

		case R.id.IndMenu:
			// menu 菜单， 添加about和exit
			showInfo();	
			break;
		}
	}
	
	public void showInfo(){
		new AlertDialog.Builder(this)
		.setTitle("info")
		.setMessage("确认退出？")
		.setPositiveButton("确认", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "欢迎使用！" + "tel:15010611780", Toast.LENGTH_LONG).show();
				exitFLG = true;//推出player之后就不能在读取service中数据，必须放到exitPlayer之前
				exitPlayer();
//				unbindService(mServiceConn);
//				System.out.println("=======unbindService");
				finish();
				
			}
		}).setNegativeButton("返回", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("返回");
			}
		})
		.show();
		
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			finish();
		}
		return super.onMenuItemSelected(featureId, item);
	}

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

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Toast.makeText(this, "about" + "\n" + "mp3音乐播放器" + "\n" + "作者：张庆财",
					Toast.LENGTH_LONG).show();
			break;
		case Menu.FIRST + 2:
			exitPlayer();
			break;
		}
		return false;
	}

	private void exitPlayer() {
		if(runThread == 1){
			runThread = 0;
		}
		// TODO Auto-generated method stub
		try {
			localMediaService.stop();
			localMediaService.release();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("=======unbindSerfdfvice============");
			e.printStackTrace();
		}
		
		unbindService(mServiceConn);
		try{
		finish();
		}catch(Exception e){
			System.out.println("=======unbindService");
	}}
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "欢迎使用！" + "tel:15010611780", Toast.LENGTH_LONG)
				.show();
	}
	
	public void onStop(){
		exitFLG = true;
		super.onStop();
		System.out.println("Activity: onStop.");
		exitFLG = true;
	}
	@Override
	public void onDestroy(){
//		mPercentHandler.removeCallbacks(handlerThread);
		super.onDestroy();
		System.out.println("Activity: onDestroy.");
	}
}

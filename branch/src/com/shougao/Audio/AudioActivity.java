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
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	boolean exitFLG = false;// ��ʾ�Ƴ�Activity
	int intPlayMode = 1;// ��ʼ��˳�򲥷�
	int intPlayState = 0; // 0 stop, 1play.
	int runThread = 1;// ����ˢ��seekbar�̣߳����˳�activityͬ����1.��ʾ�����С�
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
	private Handler mPercentHandler = new Handler();// ��initplayer��ʹ��HandlerThread.getLooper��ʼ��
	private HandlerThread handlerThread = new HandlerThread("updateSeekTime");
	ScrollableViewGroup viewGroup = null;
	private String currentPlayAudio = null;
	Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
	}

	/*
	 * ��ɺ������л� 2011-8-15 (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// �����Ļ�ķ�����������
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// ��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// ��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
		}
		// ���ʵ����̵�״̬���Ƴ����ߺ���
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			// ʵ����̴����Ƴ�״̬���ڴ˴���Ӷ���Ĵ������
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			// ʵ����̴��ں���״̬���ڴ˴���Ӷ���Ĵ������
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
	 * ��ʼ��mediaplayer��ϵͳ�Դ���mediaplayer��Ϣ
	 */
	protected void initPlayer() {
		// ��ʼ��service�е�meidaplayer
		System.out.println("debug......initPlayer");
		// TODO Auto-generated method stub
		try {
			localMediaService.initPlayer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ʾ�����б�
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
	 * �����Ҫʵʱ���£���play,��onitemclick�ĵ�һ��������Ŀ����Ϊ�˵������ַ�ʽû�������� ����Ǻ�activity��һ���߳���
	 */
	public void updateSeekBar() {
		mPercentHandler.post(updateSeekbar);
	}

	Runnable updateSeekbar = new Runnable() {

		@Override
		public void run() {
			String value = null;
			int position = 0;
			// TODO Auto-generated method stub
			if (exitFLG) {
				return;
			}
			System.out.println(exitFLG);
			try {
				position = localMediaService.getCurrentProcess() / 1000;// ��ǰ����λ��
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
			// updateDurationTime();//Ϊ��ʵ�ֲ��Ž������Զ�������һ��ʱ���Զ�������һ���ĳ���ʱ�䡣
			updateInfo();
			mPercentHandler.postDelayed(updateSeekbar, 1000);
			// ���²�����Ϣ�����߳��У����������ʱ�䣬����ACTIVITY
			// ��һ��ʱ���жϺ�����һ�����Ž������¡�
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
	 * onItemClick ������ AdapterView<?>
	 * arg0������spinner�м��ص�һϵ���ַ�������һ�������������ַ�����Spinner֮�������
	 * �����Ҫ�����뱻ѡ����ص����ݣ�ִ�г�����Ե���getItemAtPosition(position)�� parent
	 * �������������AdapterView�� view ��AdapterView�б��������ͼ(������adapter�ṩ��һ����ͼ)��
	 * position����ͼ��adapter�е�λ�á� id �����Ԫ�ص���id��
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
			// �ı䲥��״̬
			if (intPlayState == 0) {
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
			}
			updateSeekBar();
		}
	};

	/*
	 * ��ø�����ϸ��Ϣ��ֻ��ʾ3����Ϣ
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
	 * ���²����ļ�����ʱ�䡣
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
	 * ���¸�����Ϣ�������ӣ���һ������һ�������������һ����������š�ʱ���µ���Ϣ,seekbar��ʵʱʱ����ȡ� �����У���������ʱ�䡢������Ϣ��
	 */
	public void updateInfo() {
		updateMp3Info();
		updateDurationTime();
		// updateSeekBar();�̵߳���ִ��
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.IndMain:
			viewGroup.snapToScreen(0);// ��ת����һ��Ļ
			break;
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
				updateInfo();
				vPlay.setImageResource(R.drawable.img_playback_bt_pause);
				intPlayState = 1;
				// ��ʼִ��seekbar���̣߳�����߳�һ��������һֱִ��
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
			viewGroup.snapToScreen(1);// ��ת����2����Ļ
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
				Toast.makeText(getApplicationContext(), "˳�򲥷�",
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
				Toast.makeText(getApplicationContext(), "ѭ������",
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
				Toast.makeText(getApplicationContext(), "�����ظ�",
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
				Toast.makeText(getApplicationContext(), "�������",
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
			viewGroup.snapToScreen(2);// ��ת����������Ļ
			break;

		case R.id.IndMenu:
			// menu �˵��� ���about��exit
			showInfo();
			break;
		}
	}

	public void showInfo() {
		new AlertDialog.Builder(this)
				.setTitle("info")
				.setMessage("ȷ���˳���")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getApplicationContext(),
								"��ӭʹ�ã�" + "tel:15010611780", Toast.LENGTH_LONG)
								.show();
						exitPlayer();
					}
				})
				.setNegativeButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.out.println("����");
					}
				}).show();

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
		 * add()�������ĸ������������ǣ� 1��������������Ļ���дMenu.NONE,
		 * 2��Id���������Ҫ��Android�������Id��ȷ����ͬ�Ĳ˵� 3��˳���Ǹ��˵�������ǰ������������Ĵ�С����
		 * 4���ı����˵�����ʾ�ı�
		 */
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "about").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "exit").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	/**
	 * �Լ���menu������Ӧ����
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case Menu.FIRST + 1:
		// Toast.makeText(this, "about" + "\n" + "mp3���ֲ�����" + "\n" + "���ߣ������",
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

	private void openOptionsDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("        ���")
				.setMessage(R.string.app_about_msg1)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	/**
	 * �˳�player֮ǰ��������󣬱�־λ����
	 */
	private void exitPlayer() {
		exitFLG = true;// �Ƴ�player֮��Ͳ����ڶ�ȡservice�����ݣ�����ŵ�exitPlayer֮ǰ
		if (runThread == 1) {
			runThread = 0;
		}
		// TODO Auto-generated method stub
		try {
			localMediaService.stop();
			localMediaService.release();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("=======unbindSerice============");
			e.printStackTrace();
		}
		unbindService(mServiceConn);
		try {
			finish();
		} catch (Exception e) {
			System.out.println("=======unbindService");
		}
	}

	/**
	 * ���Ǹ��Զ�ִ�к�������OnOptionMenuִ��֮��ִ��
	 */
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "лл����ʹ�ã�" + "tel:15010611780", Toast.LENGTH_LONG)
				.show();
	}

	public void onStop() {
		mPercentHandler.removeCallbacks(updateSeekbar);
		exitFLG = true;
		super.onStop();
		System.out.println("Activity...onStop.");
		exitFLG = true;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Activity...onDestroy.");
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

}

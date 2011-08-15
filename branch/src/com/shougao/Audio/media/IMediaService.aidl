package com.shougao.Audio.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.shougao.Audio.DataBase.AUDIO_TAG;
//import com.shougao.Audio.DataBase.AUDIO_TAG_1;
import android.os.Parcelable;

interface IMediaService{
AUDIO_TAG[] getAudio();
int getCurrentOrderIndex();
String getCurrentPlayAudio();
int getCurrentPlayIndex();
int getCurrentProcess();
int getDuration();
void setDuration();
int getMediaTime();
void seek(int paramInt);
List<String> getPlayList();
void play();
void stop();
void release();
void pause();
void prev();
void next();
void initPlayer();
int getPlayState();
int getRepeatMode();
int getShuffleMode();
int getTotalPlayNum();
int setRepeatMode();
void setCurrentPlayIndex();
void setShuffleMode(int paramInt);
void setMediaTime(int paramInt);
AUDIO_TAG[] setPlayList();
void setStopTimer(long paramInt);
void updatePlayList(out AUDIO_TAG[] paramInt);
void passSelectedFile(int paramInt);
}

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
AUDIO_TAG[] getCurrentPlayAudio();
int getCurrentPlayIndex();
int getDruation();
int getMediaTime();
List<String> getPlayList();
void play();
void stop();
void pause();
void prev();
void next();
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
}

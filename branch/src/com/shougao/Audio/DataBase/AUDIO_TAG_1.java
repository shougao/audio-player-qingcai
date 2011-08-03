package com.shougao.Audio.DataBase;

import android.os.Parcel;
import android.os.Parcelable;


public class AUDIO_TAG_1 implements Parcelable.Creator {

	@Override
	public AUDIO_TAG createFromParcel(Parcel source) {
		// TODO Auto-generated method stub
		return new AUDIO_TAG(source);
	}

	@Override
	public AUDIO_TAG [] newArray(int size) {
		// TODO Auto-generated method stub
		return new AUDIO_TAG[size];
	}

}

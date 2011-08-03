package com.shougao.Audio.DataBase;

import android.os.Parcel;
import android.os.Parcelable;

public class AUDIO_TAG implements Parcelable {

	public String audio_album;
	public String audio_artist;
	public int audio_size;
	public String audio_title;
	public int audio_bitrate;
	private String audio_dir;
	private String audio_display_name;
	public int audio_duration;
	public int audio_exist;
	public int audio_frequency;
	public String audio_genre;
//	private int audio_id;
//	public int audio_is_music;
//	public int audio_is_readtag;
//	public int audio_track;
//	public int audio_year;
//	public String audio_composer;

	public AUDIO_TAG(Parcel source) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public static final Parcelable.Creator<AUDIO_TAG> CREATOR = new Parcelable.Creator<AUDIO_TAG>() {

		@Override
		public AUDIO_TAG createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new AUDIO_TAG(source);
		}

		@Override
		public AUDIO_TAG[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AUDIO_TAG[size];
		}

	}; // this method instand of AUDIO_TAG_1() function;

}

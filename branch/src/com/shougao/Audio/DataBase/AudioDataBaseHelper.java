package com.shougao.Audio.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioDataBaseHelper extends SQLiteOpenHelper {

	public SQLiteDatabase myDB = null;
	private static String DB_NAME = "audio.db";
	private static String DB_PATH = "/sdcard/Audio";

	public AudioDataBaseHelper(Context context, String name,CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		myDB = this.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE ";
		db.execSQL(sql);
		myDB = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

package com.shougao.Audio.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AudioDataBaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "audio.db";
	private static final int VERSION = 1;
	public SQLiteDatabase myDB = null;
	private static String TABLE_NAME = "AudioTag";
	private static int id = 0;
//	private static String DB_PATH = "/sdcard/Audio";

	public AudioDataBaseHelper(Context context, String name,CursorFactory factory, int version)
	{
		// 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类  
		super(context, DB_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		myDB = this.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
//		String sql = "CREATE TABLE IF NOT EXISTS" + TABLE_NAME + "(" + id + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + ")";
		String sql = "CREATE TABLE + TABLE_NAME + (id INTEGER PRIMARY KEY AUTOINCREMENT, fileName VARCHAR(50), path VARCHAR(20))";
		db.execSQL(sql);
		myDB = db;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
		onCreate(db);
	}
}

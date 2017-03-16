package com.ytrain.wxns.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String dbname = "szwxns";
	private static int version = 1;
	private static DatabaseHelper instance;

	private DatabaseHelper(Context context) {
		super(context, dbname, null, version);
	}

	// 变成全局单例
	public static DatabaseHelper Instance(Context context) {
		synchronized (DatabaseHelper.class) {
			if (instance == null) {
				instance = new DatabaseHelper(context);
			}
		}
		return instance;
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE IF NOT EXISTS unit(id int,wifiname varchar(200),domain varchar(200),imgUrl varchar(200),Url3D varchar(200),flag int,phones varchar(200),ssid varchar(80),descn text)");
			db.execSQL("CREATE TABLE IF NOT EXISTS part(id int,name varchar(200),parentId int,level int,ssid varchar(80),imageUrl varchar(255),isShowIndex int,sortIndex int)");
			db.execSQL("CREATE TABLE IF NOT EXISTS article(id int,title varchar(200),partId int,subtitle varchar(200),createTime varchar(40),imageUrl varchar(255),source varchar(200),content text,cacheDate varchar(40))");
			db.execSQL("CREATE TABLE IF NOT EXISTS update_part_record(parentId int,updateDate varchar(24))");
			db.execSQL("CREATE TABLE IF NOT EXISTS update_unit_record(id integer primary key autoincrement,updateDate varchar(24))");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

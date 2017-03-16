package com.ytrain.wxns.database;

import java.util.ArrayList;
import java.util.List;

import com.ssy.utils.DateUtils;
import com.ytrain.wxns.entity.ArticleEntity;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.entity.UnitEntity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InitDataSqlite {

	private DatabaseHelper dbOpenHelper;

	public InitDataSqlite(Context context) {
		dbOpenHelper = DatabaseHelper.Instance(context);
		while (dbOpenHelper.getWritableDatabase().isDbLockedByOtherThreads()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 批量保存热点单位信息
	 * 
	 * @param lists
	 * @throws Exception
	 */
	public synchronized void saveUnit(List<UnitEntity> lists) throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			for (UnitEntity unitEntity : lists) {
				String sql = "insert into unit(id,wifiname,domain,imgUrl,Url3D,flag,phones,ssid,descn) values(?,?,?,?,?,?,?,?,?)";
				database.execSQL(sql, new Object[] { unitEntity.getId(),
						unitEntity.getWifiname(), unitEntity.getDomain(),
						unitEntity.getImgUrl(), unitEntity.getUrlPanoramic(),
						unitEntity.getFlag(), unitEntity.getPhones(),
						unitEntity.getSsid(), unitEntity.getDescn() });
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 删除缓存
	 * 
	 * @throws Exception
	 */
	public synchronized void delete() throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			database.execSQL("delete from unit");
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 根据ssid获取单位
	 * 
	 * @param ssid
	 * @return
	 * @throws Exception
	 */
	public synchronized UnitEntity findUnitBySsid(String ssid) throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dbOpenHelper.getReadableDatabase();
			cursor = database.rawQuery("select * from unit where ssid=?",
					new String[] { ssid });
			UnitEntity ue = null;
			if (cursor.moveToNext()) {
				ue = new UnitEntity();
				ue.setId(cursor.getInt(0));
				ue.setWifiname(cursor.getString(1));
				ue.setDomain(cursor.getString(2));
				ue.setImgUrl(cursor.getString(3));
				ue.setUrlPanoramic(cursor.getString(4));
				ue.setFlag(cursor.getInt(5));
				ue.setPhones(cursor.getString(6));
				ue.setSsid(cursor.getString(7));
				ue.setDescn(cursor.getString(8));
			}
			return ue;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 从本地数据库中获取热点单位
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized List<UnitEntity> findCache() throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			List<UnitEntity> list = new ArrayList<UnitEntity>();
			database = dbOpenHelper.getReadableDatabase();
			cursor = database.rawQuery("select * from unit", null);
			UnitEntity ue = null;
			while (cursor.moveToNext()) {
				ue = new UnitEntity();
				ue.setId(cursor.getInt(0));
				ue.setWifiname(cursor.getString(1));
				ue.setDomain(cursor.getString(2));
				ue.setImgUrl(cursor.getString(3));
				ue.setUrlPanoramic(cursor.getString(4));
				ue.setFlag(cursor.getInt(5));
				ue.setPhones(cursor.getString(6));
				ue.setSsid(cursor.getString(7));
				ue.setDescn(cursor.getString(8));
				list.add(ue);
			}
			return list;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 批量保存栏目单位信息
	 * 
	 * @param lists
	 * @throws Exception
	 */
	public synchronized void savePart(List<PartEntity> lists) throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			for (PartEntity partEntity : lists) {
				String sql = "insert into part(id,name,parentId,level,ssid,imageUrl,isShowIndex,sortIndex) values(?,?,?,?,?,?,?,?)";
				database.execSQL(
						sql,
						new Object[] { partEntity.getId(),
								partEntity.getName(), partEntity.getParentId(),
								partEntity.getLevel(), partEntity.getSsid(),
								partEntity.getImageUrl(),
								partEntity.getIsShowIndex(),
								partEntity.getSortIndex() });
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				
				database.close();
			}
		}
	}

	/**
	 * 从本地数据库中根据Ssid获取栏目
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized List<PartEntity> findCacheBySsid(String ssid)
			throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			List<PartEntity> list = new ArrayList<PartEntity>();
			database = dbOpenHelper.getReadableDatabase();
			cursor = database
					.rawQuery(
							"select id,name,parentId,level,ssid,imageUrl,isShowIndex,sortIndex from part where ssid=?",
							new String[] { ssid });
			PartEntity pe = null;
			while (cursor.moveToNext()) {
				pe = new PartEntity();
				pe.setId(cursor.getInt(0));
				pe.setName(cursor.getString(1));
				pe.setParentId(cursor.getInt(2));
				pe.setLevel(cursor.getInt(3));
				pe.setSsid(cursor.getString(4));
				pe.setImageUrl(cursor.getString(5));
				pe.setIsShowIndex(cursor.getInt(6));
				pe.setSortIndex(cursor.getInt(7));
				list.add(pe);
			}
			return list;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 从本地数据库中根据上级编号获取栏目
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized List<PartEntity> findCacheByParentId(Integer parentId)
			throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			List<PartEntity> list = new ArrayList<PartEntity>();
			database = dbOpenHelper.getReadableDatabase();
			cursor = database
					.rawQuery(
							"select id,name,parentId,level,ssid,imageUrl,isShowIndex,sortIndex from part where parentId=?",
							new String[] { String.valueOf(parentId) });
			PartEntity pe = null;
			while (cursor.moveToNext()) {
				pe = new PartEntity();
				pe.setId(cursor.getInt(0));
				pe.setName(cursor.getString(1));
				pe.setParentId(cursor.getInt(2));
				pe.setLevel(cursor.getInt(3));
				pe.setSsid(cursor.getString(4));
				pe.setImageUrl(cursor.getString(5));
				pe.setIsShowIndex(cursor.getInt(6));
				pe.setSortIndex(cursor.getInt(7));
				list.add(pe);
			}
			return list;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 批量保存内容信息
	 * 
	 * @param lists
	 * @throws Exception
	 */
	public synchronized void saveArticle(List<ArticleEntity> lists)
			throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			for (ArticleEntity artEntity : lists) {
				database.execSQL(
						"delete from article where id=?",
						new Object[] { artEntity.getId() });

			}
			for (ArticleEntity artEntity : lists) {
				String sql = "insert into article(id,title,partId,subtitle,createTime,imageUrl,source,content,cacheDate) values(?,?,?,?,?,?,?,?,?)";
				database.execSQL(
						sql,
						new Object[] { artEntity.getId(), artEntity.getTitle(),
								artEntity.getPartId(), artEntity.getSubtitle(),
								artEntity.getCreateTime(),
								artEntity.getImageUrl(), artEntity.getSource(),
								artEntity.getContent(), DateUtils.curDate() });
			}
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 从本地数据库，根据partId，获取缓存
	 * 
	 * @param partId
	 * @return
	 * @throws Exception
	 */
	public synchronized List<ArticleEntity> findCacheByPartId(Integer partId)
			throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			List<ArticleEntity> list = new ArrayList<ArticleEntity>();
			database = dbOpenHelper.getReadableDatabase();
			cursor = database
					.rawQuery(
							"select id,title,partId,subtitle,createTime,imageUrl,source,content from article where partId=?",
							new String[] { String.valueOf(partId) });
			ArticleEntity ae = null;
			while (cursor.moveToNext()) {
				ae = new ArticleEntity();
				ae.setId(cursor.getInt(0));
				ae.setTitle(cursor.getString(1));
				ae.setPartId(cursor.getInt(2));
				ae.setSubtitle(cursor.getString(3));
				ae.setCreateTime(cursor.getString(4));
				ae.setImageUrl(cursor.getString(5));
				ae.setSource(cursor.getString(6));
				ae.setContent(cursor.getString(7));
				list.add(ae);
			}
			return list;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 清除缓存
	 */
	public synchronized void deleteArticle(Integer partId) throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			database.execSQL(
					"delete from article where date(cacheDate)<=date('now','-7 day') and partId=?",
					new Object[] { partId });
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 根据parentId保存更新记录的时间
	 * 
	 * @param parentId
	 * @throws Exception
	 */
	public synchronized void updatePartRecord(Integer parentId)
			throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			database.execSQL(
					"insert into update_part_record(parentId,updateDate) values(?,?)",
					new Object[] { parentId, DateUtils.curDate() });
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * Unit 更新记录
	 * 
	 * @throws Exception
	 */
	public synchronized void updateUnitRecord() throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			database.execSQL(
					"insert into update_unit_record(updateDate) values(?)",
					new Object[] { DateUtils.curDate() });
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 根据parentId和时间判断是否有更新过数据
	 * 
	 * @param partId
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean findCacheByParentIdAndDate(Integer parentId)
			throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dbOpenHelper.getReadableDatabase();
			cursor = database
					.rawQuery(
							"select parentId from update_part_record where parentId=? and updateDate=?",
							new String[] { String.valueOf(parentId),
									DateUtils.curDate() });
			boolean flag = false;
			if (cursor.moveToNext()) {
				flag = true;
			}
			return flag;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 根据日期判断Unit是否有更新过
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean findUnitCacheByDate() throws Exception {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dbOpenHelper.getReadableDatabase();
			cursor = database.rawQuery(
					"select * from update_unit_record where updateDate=?",
					new String[] { DateUtils.curDate() });
			boolean flag = false;
			if (cursor.moveToNext()) {
				flag = true;
			}
			return flag;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
	}

	/**
	 * 删除所有缓存
	 * 
	 * @throws Exception
	 */
	public synchronized void deleteAll() throws Exception {
		SQLiteDatabase database = null;
		try {
			database = dbOpenHelper.getWritableDatabase();
			database.beginTransaction();
			database.execSQL("delete from unit");
			database.execSQL("delete from part");
			database.execSQL("delete from article");
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}
}

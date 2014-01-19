package com.yi4all.davidapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;

import java.sql.SQLException;

public class CMSDBOpenHelper extends OrmLiteSqliteOpenHelper {

	public static final int DATABASE_VERSION = 2;

	public static final String DATABASE_NAME = "cms.db";

	// we do this so there is only one helper
	private static CMSDBOpenHelper helper = null;

	private Dao<ContentModel, Long> contentDao;
	private Dao<CompanyModel, Long> companyDao;
	private Dao<OrderModel, Long> orderDao;

	public CMSDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized CMSDBOpenHelper getHelper(Context context) {
		if (helper == null) {
			helper = new CMSDBOpenHelper(context);
			helper.getWritableDatabase();
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(CMSDBOpenHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ContentModel.class);
			TableUtils.createTable(connectionSource, CompanyModel.class);
			TableUtils.createTable(connectionSource, OrderModel.class);
		} catch (SQLException e) {
			Log.e(CMSDBOpenHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(CMSDBOpenHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ContentModel.class, true);
			TableUtils.dropTable(connectionSource, CompanyModel.class, true);
			TableUtils.dropTable(connectionSource, OrderModel.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(CMSDBOpenHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}

	}

	public Dao<ContentModel, Long> getContentDAO() throws SQLException {
		if (contentDao == null) {
			contentDao = getDao(ContentModel.class);
		}
		return contentDao;
	}
	
	public Dao<CompanyModel, Long> getCompanyDAO() throws SQLException {
		if (companyDao == null) {
			companyDao = getDao(CompanyModel.class);
		}
		return companyDao;
	}
	
	public Dao<OrderModel, Long> getOrderDAO() throws SQLException {
		if (orderDao == null) {
			orderDao = getDao(OrderModel.class);
		}
		return orderDao;
	}

	@Override
	public void close() {
		super.close();
		contentDao = null;
		companyDao = null;
	}

}

package com.yi4all.davidapp.service;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.yi4all.davidapp.db.CMSDBOpenHelper;
import com.yi4all.davidapp.db.CompanyModel;
import com.yi4all.davidapp.db.ContentModel;
import com.yi4all.davidapp.db.ContentType;
import com.yi4all.davidapp.db.OrderModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBServiceImpl implements IDBService {

	private static final String LOG_TAG = "DBServiceImpl";

	private CMSDBOpenHelper cmsHelper;
	
	private DBServiceImpl(Context context) {
		// this.commonHelper = CommonDBOpenHelper.getHelper(context);
		this.cmsHelper = CMSDBOpenHelper.getHelper(context);
	}

	public static IDBService getInstance(Context context) {
		return new DBServiceImpl(context);
	}

	@Override
	public void close() {
		if (cmsHelper != null && cmsHelper.isOpen()) {
			OpenHelperManager.releaseHelper();
			cmsHelper = null;
		}
	}

	public CMSDBOpenHelper getCmsHelper() {
		return cmsHelper;
	}


	@Override
	public List<ContentModel> getContentsByType(ContentType type) {
		try {
			Dao<ContentModel, Long> dba = cmsHelper.getContentDAO();
			QueryBuilder<ContentModel, Long> queryBuilder = dba.queryBuilder();
			queryBuilder.orderBy(ContentModel.CREATEDAT, false);
			
			Where<ContentModel, Long> where = queryBuilder.where();
			if(type != null){
				where.eq(ContentModel.CONTENTTYPE, type);
				where.and();
			}
			where.eq(ContentModel.DELETEFLAG, false);

			return dba.query(queryBuilder.prepare());

		} catch (SQLException e) {

			Log.e(LOG_TAG, e.getMessage());
		}
		return new ArrayList<ContentModel>();
	}

	@Override
	public void updateContents(List<ContentModel> list) {
		try {
			Dao<ContentModel, Long> udao = cmsHelper.getContentDAO();

			for (ContentModel im : list) {				
				ContentModel app = udao.queryForId(im.getId());
				if (app == null) {
					//create a new app
					udao.create(im);
				}else{
					app.setName(im.getName());
					app.setDescription(im.getDescription());
                    app.setBigPic(im.getBigPic());
                    app.setContentType(im.getContentType());
                    app.setDeleteFlag(im.isDeleteFlag());
                    app.setLanguage(im.getLanguage());
                    app.setPhoneNumber(im.getPhoneNumber());
                    app.setSmallPic(im.getSmallPic());
                    app.setUrl(im.getUrl());
					app.setCreatedAt(im.getCreatedAt());

					udao.update(app);
				}
			}

		} catch (SQLException e) {

			Log.e(LOG_TAG, e.getMessage());
		}

	}

    @Override
    public CompanyModel getDefaultCompany() {
        try {
            Dao<CompanyModel, Long> dba = cmsHelper.getCompanyDAO();

            List<CompanyModel> list = dba.queryForAll();
            if(list != null && list.size() > 0){
                return list.get(0);
            }

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public void updateCompany(CompanyModel cm) {
        try {
            Dao<CompanyModel, Long> dba = cmsHelper.getCompanyDAO();

            List<CompanyModel> list = dba.queryForAll();
            if(list != null && list.size() > 0){
                CompanyModel cm2 = list.get(0);
                cm2.setName(cm.getName());
                cm2.setDescription(cm.getDescription());
                cm2.setMarquee(cm.getMarquee());
                cm2.setCreatedAt(cm.getCreatedAt());

                dba.update(cm2);
            }else {
                dba.create(cm);
            }

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
    }

	@Override
	public List<OrderModel> getOrders() {
		try {
			Dao<OrderModel, Long> dba = cmsHelper.getOrderDAO();

			return dba.queryForAll();

		} catch (SQLException e) {

			Log.e(LOG_TAG, e.getMessage());
		}
		return new ArrayList<OrderModel>();
	}

	@Override
	public void createOrder(OrderModel cm) {
		try {
            Dao<OrderModel, Long> dba = cmsHelper.getOrderDAO();

                dba.create(cm);

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
	}
}

package com.yi4all.davidapp.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * It defines a staff in a corporation.
 * 
 * @author yanxin
 * @since 1.0
 */
@DatabaseTable(tableName = "Content")
public class ContentModel implements Serializable{

    public static final String DESCRIPTION = "DESCRIPTION";
    public final static String NAME = "NAME";
    public final static String URL = "URL";
    public final static String PHONENUMBER = "PHONENUMBER";
    public final static String SMALLPIC = "SMALLPIC";
    public final static String BIGPIC = "BIGPIC";
    public final static String CONTENTTYPE = "CONTENTTYPE";
    public final static String LANGUAGE = "LANGUAGE";
    public final static String DELETEFLAG = "DELETEFLAG";
    public final static String CREATEDAT = "CREATEDAT";

    @DatabaseField(id = true)
	public Long id;

    @DatabaseField(columnName = NAME)
    public String name;

    @DatabaseField(columnName = DESCRIPTION)
    public String description;

    @DatabaseField(columnName = URL)
    public String url;

    @DatabaseField(columnName = PHONENUMBER)
    public String phoneNumber;

    @DatabaseField(columnName = SMALLPIC)
    public String smallPic;

    @DatabaseField(columnName = BIGPIC)
    public String bigPic;

    @DatabaseField(columnName = CONTENTTYPE)
    public ContentType contentType;

    @DatabaseField(columnName = LANGUAGE)
    public ContentLanguage language;

    @DatabaseField(columnName = DELETEFLAG)
    private boolean deleteFlag;

    @DatabaseField(columnName = CREATEDAT)
	public Date createdAt;

    public ContentModel(){
    	
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ContentLanguage language) {
        this.language = language;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}

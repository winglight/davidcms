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
@DatabaseTable(tableName = "Order")
public class OrderModel implements Serializable{

    public static final String DESCRIPTION = "DESCRIPTION";
    public final static String SERVICE = "SERVICE";
    public final static String CONTENT = "CONTENT";
    public final static String CREATEDAT = "CREATEDAT";

    @DatabaseField(id = true)
	private Long id;

    @DatabaseField(columnName = SERVICE)
    private String service;

    @DatabaseField(columnName = DESCRIPTION)
    private String content;

    @DatabaseField(columnName = CREATEDAT)
	private Date createdAt;

    public OrderModel(){
    	
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}

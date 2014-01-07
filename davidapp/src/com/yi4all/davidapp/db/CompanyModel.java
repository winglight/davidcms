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
@DatabaseTable(tableName = "Company")
public class CompanyModel implements Serializable{

    public static final String DESCRIPTION = "DESCRIPTION";
    public final static String NAME = "NAME";
    public final static String MARQUEE = "MARQUEE";
    public final static String CREATEDAT = "CREATEDAT";

    @DatabaseField(id = true)
	private Long id;

    @DatabaseField(columnName = NAME)
    private String name;

    @DatabaseField(columnName = DESCRIPTION)
    private String description;

    @DatabaseField(columnName = MARQUEE)
    private String marquee;

    @DatabaseField(columnName = CREATEDAT)
	private Date createdAt;

    public CompanyModel(){
    	
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

    public String getMarquee() {
        return marquee;
    }

    public void setMarquee(String marquee) {
        this.marquee = marquee;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

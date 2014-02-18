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
    public static final String DESCRIPTION_S = "DESCRIPTION_S";
    public final static String NAME_S = "NAME_S";
    public final static String MARQUEE_S = "MARQUEE_S";
    public final static String CREATEDAT = "CREATEDAT";

    @DatabaseField(id = true)
	private Long id;

    @DatabaseField(columnName = NAME)
    private String name;

    @DatabaseField(columnName = DESCRIPTION)
    private String description;

    @DatabaseField(columnName = MARQUEE)
    private String marquee;

    @DatabaseField(columnName = NAME_S)
    private String name_s;

    @DatabaseField(columnName = DESCRIPTION_S)
    private String description_s;

    @DatabaseField(columnName = MARQUEE_S)
    private String marquee_s;

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

    public String getName_s() {
        return name_s;
    }

    public void setName_s(String name_s) {
        this.name_s = name_s;
    }

    public String getDescription_s() {
        return description_s;
    }

    public void setDescription_s(String description_s) {
        this.description_s = description_s;
    }

    public String getMarquee_s() {
        return marquee_s;
    }

    public void setMarquee_s(String marquee_s) {
        this.marquee_s = marquee_s;
    }
}

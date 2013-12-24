package models;

import models.status.ContentLanguage;
import models.status.ContentType;
import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * It defines a staff in a corporation.
 * 
 * @author yanxin
 * @since 1.0
 */
@Entity
@DiscriminatorValue(value = "content")
public class ContentModel extends Model implements Serializable{


	@Id
	public Long id;

    public String name;

    public String description;

    public String url;

    public String phoneNumber;

    public String smallPic;

    public String bigPic;

    public ContentType contentType;

    public ContentLanguage language;

	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date createdAt;

    public ContentModel(){
    	
    }
	
    // -- Queries
    
    public static Finder<Long, ContentModel> find = new Finder(Long.class, ContentModel.class);
    
    /**
     * Retrieve all users.
     */
    public static List<ContentModel> findAll() {
        return find.all();
    }

    public static List<ContentModel> findContentByType(ContentType contentType) {
        return find.where().eq("contentType", contentType).findList();
    }

	@Override
	public String toString() {
		return "Company [name:" + name
				+ "]";
	}
}

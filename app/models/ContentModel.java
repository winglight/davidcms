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
    public String name_s;

    public String description;
    public String description_s;

    public String url;

    public String phoneNumber;

    public String smsNumber;

    public String smallPic;

    public String bigPic;

    public ContentType contentType;

    public ContentLanguage language;

    public boolean deleteFlag;

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

	@Override
	public String toString() {
		return "Company [name:" + name
				+ "]";
	}

    public static List<ContentModel> getContentsByType(Long contentType) {
        return find.where().eq("contentType", contentType).eq("deleteFlag", false).findList();
    }

    public static void delete(Long cid) {
        find.ref(cid).delete();
    }

    public static void updateContentImage(Long content, String imgName, Boolean isBig) {
        ContentModel cm = find.byId(content);
        if(cm != null){
        if(isBig){
            cm.bigPic = imgName;
        }else {
            cm.smallPic = imgName;
        }
            cm.update();
        }
    }
}

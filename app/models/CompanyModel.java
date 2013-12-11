package models;

import models.status.UserRole;
import models.status.UserStatus;
import play.data.format.Formats;
import play.db.ebean.Model;
import util.Constants;

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "company")
public class CompanyModel extends Model implements Serializable{


	@Id
	public Long id;

    public String name;

    public String description;

	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date createdAt;

    public CompanyModel(){
    	
    }
	
    // -- Queries
    
    public static Finder<Long, CompanyModel> find = new Finder(Long.class, CompanyModel.class);
    
    /**
     * Retrieve all users.
     */
    public static List<CompanyModel> findAll() {
        return find.all();
    }

	@Override
	public String toString() {
		return "Company [name:" + name
				+ "]";
	}
}

package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import util.Constants;

import models.status.UserRole;
import models.status.UserStatus;

/**
 * It defines a staff in a corporation.
 * 
 * @author yanxin
 * @since 1.0
 */
@Entity
@DiscriminatorValue(value = "user")
public class UserModel extends Model implements Serializable{ 

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019366733339045946L;

	@Id
	public Long id;
	
    public String name;
    
    public String email;
    
    public String deviceId;
    
    public String password;

	@Enumerated(EnumType.STRING)
	public UserStatus status;
	
	@Enumerated(EnumType.STRING)
	public UserRole userRole;
	
	@Formats.DateTime(pattern="yyyy-MM-dd")
	public Date createdAt; 

    public UserModel(){
    	
    }
	
    // -- Queries
    
    public static Finder<Long,UserModel> find = new Finder(Long.class, UserModel.class);
    
    /**
     * Retrieve all users.
     */
    public static List<UserModel> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from loginName.
     */
    public static UserModel findByloginName(String loginName) {
        return find.where().eq(Constants.SESSION_USER_NAME, loginName).findUnique();
    }
    
    /**
     * Authenticate a User.
     */
    public static UserModel authenticate(String loginName, String password) {
        return find.where()
            .eq(Constants.SESSION_USER_NAME, loginName)
            .eq("status", UserStatus.Active)
            .eq("password", password)
            .findUnique();
    }

    public static boolean verifyUser(String loginName) {
        return find.where()
            .eq(Constants.SESSION_USER_NAME, loginName)
            .findUnique() == null;
    }
    
    public static boolean verifyEmail(String email) {
        return find.where()
            .eq("email", email)
            .findUnique() == null;
    }

	@Override
	public String toString() {
		return "User [name:" + name
				+ "]";
	}
}

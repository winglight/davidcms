package models.status;


/**
 * The status of account.
 * 
 * @author yanxin
 * @since 1.0.0
 * 
 */
public enum ContentType {

	Hall("HALL"),
    Service("SERVICE"),
    Activity("ACTIVITY"),
    SubAddress("SUBADDRESS");

	private ContentType(String displayName) {
		this.displayName = displayName;
	}

	String displayName;

	public String getDisplayName() {
		return this.displayName;
	}
}

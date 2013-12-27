/**
 * 
 */
package models.status;

/**
 * Stores a list of well-known roles in the system.
 * 
 * @author cyril
 * @since 0.2.0
 */
public enum UserRole {

	/**
	 * Corporation administrator.
	 */
	ADMIN(0, "ADMIN"),

	/**
	 * Department manager.
	 */
	DEVELOPER(1, "DEVELOPER"),

	/**
	 * Ordinary staff.
	 */
	USER(2, "USER");

    private int value = 0;

    private UserRole(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public int value() {
        return this.value;
    }
}

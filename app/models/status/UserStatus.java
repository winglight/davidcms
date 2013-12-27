package models.status;


/**
 * The status of account.
 * 
 * @author yanxin
 * @since 1.0.0
 * 
 */
public enum UserStatus {

	ACTIVE(0, "ACTIVE"), INACTIVE(1, "DISABLE");

    private int value = 0;

	private UserStatus(int value, String displayName) {
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

package com.yi4all.davidapp.db;


/**
 * The status of account.
 * 
 * @author yanxin
 * @since 1.0.0
 * 
 */
public enum ContentType {

	HALL(0, "HALL"),
    SERVICE(1, "SERVICE"),
    ACTIVITY(2, "ACTIVITY"),
    SUBADDRESS(3, "SUBADDRESS");

	private ContentType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    private int value = 0;
    private String displayName;

    public String getDisplayName() {
        return this.displayName;
    }

    public int value() {
        return this.value;
    }
}

package com.yi4all.davidapp.db;


/**
 * The status of account.
 * 
 * @author yanxin
 * @since 1.0.0
 * 
 */
public enum ContentLanguage {

	Traditional("Traditional"),
    Simple("Simple");

	private ContentLanguage(String displayName) {
		this.displayName = displayName;
	}

	String displayName;

	public String getDisplayName() {
		return this.displayName;
	}
}

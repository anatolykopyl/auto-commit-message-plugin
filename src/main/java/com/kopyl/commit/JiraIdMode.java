package com.kopyl.commit;

public enum JiraIdMode {
	NONE("None"),
	AUTODETECT("Autodetect"),
	FIXED("Fixed");

	private final String text;

	JiraIdMode(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}

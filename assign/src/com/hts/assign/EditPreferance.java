package com.hts.assign;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EditPreferance extends PreferenceActivity {
	public static boolean isPrefLoginFalse = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
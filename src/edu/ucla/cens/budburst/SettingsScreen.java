package edu.ucla.cens.budburst;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsScreen extends PreferenceActivity {

	PreferenceManager mPreferenceManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		mPreferenceManager = getPreferenceManager();
	}
	
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) 
	{
        String key = preference.getKey();
        if (key.equals(getString(R.string.settingsParamLogOut)))
        {
			PreferencesManager.cleanPreferences(this);
			PreferencesManager.logOut(this);
			Budburst.getDatabaseManager().destroyUserDatabases();
			sendBroadcast(new Intent(Constants.INTENT_ACTION_LOGGED_OUT));
			SettingsScreen.this.startActivity(new Intent(SettingsScreen.this, LoginScreen.class));
			this.finish();   
			
        }
        return true;
	}	
}
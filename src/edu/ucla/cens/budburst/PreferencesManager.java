package edu.ucla.cens.budburst;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PreferencesManager {
	public static String no_user = "NO_USER";
	public static String no_password = "NO_PASSWORD";
	public static boolean no_trace = true;
	
	private static final String TAG = "preferencesManager";
	
	public static void rememberUser(String username, Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putString("default_username", username);
		settingsEditor.commit();
	}
	
	public static String getDefaultUser(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		return settings.getString("default_username", no_user);
	}
	
	public static void letUserIn(String username, String password, Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putString("current_username", username);
		settingsEditor.putString("current_password", password);
		settingsEditor.commit();
	}
	
	//returns You if the username matches this users username
	public static String getUserIdentifier(Context ctx, String username) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		if(username.equals(settings.getString("current_username", no_user)))
				return "You";
		
		return username;
	}
	
	public static boolean isUserIn(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		String cur_user = settings.getString("current_username", no_user);
		if(!cur_user.equals(no_user)) return true;
		else return false;
	}
	
	public static String currentUser(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		return settings.getString("current_username", no_user);
	}
	
	public static String currentPassword(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		return settings.getString("current_password", no_password);
	}
	
	public static ArrayList<NameValuePair> currentAuthParams(Context ctx) {
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("username",currentUser(ctx)));
		data.add(new BasicNameValuePair("password",currentPassword(ctx)));
		return data;
	}
	
	public static String currentGETAuthParams(Context ctx) {
		return "&username="+currentUser(ctx)+"&password="+currentPassword(ctx);
	}
		
	public static void logOut(Context ctx) {
		//Remember current user as the default user
		rememberUser(currentUser(ctx), ctx);
		//Set user in to NO_USER
		letUserIn(no_user, no_password, ctx);
	}
	
	public static String getTopText(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		return settings.getString("top_text", "");
	}
	
	public static void setTopText(String text, Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putString("top_text", text);
		settingsEditor.commit();
	}
	
	public static boolean getTraceActive(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		return settings.getBoolean("trace", no_trace);		
	}
	
	public static void setTraceActive(boolean traceActive, Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		SharedPreferences.Editor settingsEditor = settings.edit();
		settingsEditor.putBoolean("trace", traceActive);
		settingsEditor.commit();
	}
	
		
	public static void cleanPreferences(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs),0);
		SharedPreferences.Editor settingsEditor = settings.edit();
				
		String vs = "version ";
        try {
			vs += ctx.getPackageManager().getPackageInfo("edu.ucla.cens.budburst", 0).versionName;
		} catch (NameNotFoundException e) {
			vs += "unknown";
		}
		
		if(getDefaultUser(ctx) != no_user) setTopText("Hi, " + currentUser(ctx) + "! You are running " + vs + " - the best Kullect yet!", ctx);
		else setTopText("", ctx);
		
		settingsEditor.commit();

		//Log.d(TAG, "Clean Preferences Called.");
	}
}

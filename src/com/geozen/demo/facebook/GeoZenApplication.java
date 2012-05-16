/**
 * @author matt@geozen.com
 */

package com.geozen.demo.facebook;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.facebook.android.Facebook;

public class GeoZenApplication extends Application {

	public static final boolean DEBUG = false;
	public static final String LOGTAG = "Android-Foursquare";
	public static final String APP_ID = "271769212892434";

	public SharedPreferences mPrefs;

	Facebook mFacebook = new Facebook(APP_ID);

	private static GeoZenApplication mInstance;

	@Override
	public void onCreate() {

		Log.i(LOGTAG, "Using Debug Log:\t" + DEBUG);
		mInstance = this;

		mFacebook = new Facebook(APP_ID);
		SessionStore.restore(mFacebook, this);

	}

	public static Context getContext() {
		return mInstance;
	}

	public boolean isReady() {
		return mFacebook.isSessionValid();
	}

	public boolean isConnected() {

		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

			if (netInfo != null) {
				connected = netInfo.isConnected();
			}
		}
		return connected;
	}

	public static GeoZenApplication getInstance() {

		return mInstance;
	}

}

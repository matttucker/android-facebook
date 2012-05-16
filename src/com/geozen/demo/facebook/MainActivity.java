/**
 * @author matt@geozen.com
 */

package com.geozen.demo.facebook;

import static com.geozen.demo.facebook.GeoZenApplication.DEBUG;
import static com.geozen.demo.facebook.GeoZenApplication.LOGTAG;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.android.AsyncFacebookRunner;

public class MainActivity extends Activity {

	private static GeoZenApplication mApp;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApp = (GeoZenApplication) getApplication();
		// Don't start the main activity if we don't have credentials
		if (!mApp.isReady()) {
			if (DEBUG)
				Log.d(LOGTAG, "Not ready for user.");
			redirectToSignInActivity();
			return;
		}

		setContentView(R.layout.main);

		mHandler = new Handler();
	}

	private void redirectToSignInActivity() {
		Intent intent = new Intent(this, SigninActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.signout:
			signout();
			return true;
		}

		return false;
	}

	public void signout() {
		SessionStore.clear(this);
		AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
				mApp.mFacebook);
		asyncRunner.logout(this, new LogoutRequestListener());
	}

	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response, final Object state) {
			// callback should be run in the original thread,
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					MainActivity.this.finish();
				}
			});
		}

	}

}

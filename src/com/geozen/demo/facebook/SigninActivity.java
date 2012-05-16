/**
 * @author matt@geozen.com
 */

package com.geozen.demo.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class SigninActivity extends Activity {

	private GeoZenApplication mApp;
	private String[] mPermissions = new String[] {};
	private Handler mHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.signin);

		mApp = (GeoZenApplication) getApplication();
		
		mHandler = new Handler();

		if (mApp.mFacebook.isSessionValid()) {
			// We're authed so go directly to the main activity.
			Intent intent = new Intent(this, MainActivity.class);

			startActivity(intent);
			finish();
			return;
		}

		ImageButton connect = (ImageButton) findViewById(R.id.connectButton);
		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mApp.mFacebook.authorize(SigninActivity.this, mPermissions,
						new LoginDialogListener());
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mApp.mFacebook.authorizeCallback(requestCode, resultCode, data);
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			
		
			
			mHandler.post(new Runnable() {
				public void run() {
					SessionStore.save(mApp.mFacebook, SigninActivity.this);
					
					Intent intent = new Intent(SigninActivity.this, MainActivity.class);
					startActivity(intent);
					
					SigninActivity.this.finish();
					
				}
			});
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(SigninActivity.this, error.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			Toast.makeText(SigninActivity.this, error.getMessage(),
					Toast.LENGTH_LONG).show();

		}

		public void onCancel() {
			Toast.makeText(SigninActivity.this, "Action Canceled",
					Toast.LENGTH_LONG).show();
		}
	}

}

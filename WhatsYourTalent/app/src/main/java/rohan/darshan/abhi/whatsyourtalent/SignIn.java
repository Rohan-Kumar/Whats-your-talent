package rohan.darshan.abhi.whatsyourtalent;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class SignIn extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    private static final int SOME_REQUEST_CODE = 1000;
    private boolean mIntentInProgress;
    EditText et, et2, et3, et4;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    SignInButton btnSignIn;
    GoogleApiClient mGoogleApiClient;
    static String email = "ERROR";
    static String user, fb, twitter, web,xyz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        et = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText4);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signInWithGplus();
                user = et.getText().toString();
                fb = et2.getText().toString();
                twitter = et3.getText().toString();
                web = et4.getText().toString();

                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        Person abc =Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        xyz=abc.getImage().getUrl();

        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, SOME_REQUEST_CODE);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                test Test = new test();
                Test.execute();

                SharedPreferences sharedPreferences = getSharedPreferences("SignIn", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Yes", 1);
                editor.commit();


                Intent intent = new Intent(SignIn.this, TimeLineActivity.class);

                startActivity(intent);

                finish();
            }
        }

        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    class test extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
//            Toast.makeText(SignIn.this, "Async", Toast.LENGTH_SHORT).show();

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://bitsmate.in/videoupload/signup.php?email=" + email + "&user_name=" + user + "&fb=" + fb+ "&twitter=" + twitter+ "&web=" + web+"&profile_pic_url="+xyz);
            HttpResponse resp = null;


            try {
                resp = httpclient.execute(httppost);

                HttpEntity ent = resp.getEntity();
                Log.d("test", "" + EntityUtils.toString(ent));
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            return null;
        }


    }
}

package com.example.user.ui

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import com.facebook.login.widget.LoginButton
import com.facebook.login.LoginResult
import org.json.JSONObject
import android.content.Intent
import android.view.View
import android.widget.Button
import rx.internal.operators.NotificationLite.getError
import android.widget.Toast
import java.util.*
import android.R.attr.data
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import com.example.user.Application
import com.example.user.network.Server
import com.facebook.*
import com.facebook.login.LoginManager


class LoginActivity : AppCompatActivity(){

    private val TAG = "LoginActivity"

    var APP_REQUEST_CODE = 99

    //페이스북 로그인, 콜백
    private var fbButton: LoginButton? = null
    private var callbackManager: CallbackManager? = null

    private fun init() {
        //akButton = (BootstrapButton) findViewById(R.id.ak_login);
        fbButton = findViewById(R.id.btn_confirm) as LoginButton
        fbButton?.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"))

        fbButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val id = loginResult.accessToken.userId
                val params = Bundle()
                params.putString("fields", "email,name,gender")
                GraphRequest(
                        com.facebook.AccessToken.getCurrentAccessToken(), //loginResult.getAccessToken(),
                        "/me",
                        params,
                        HttpMethod.GET,
                        GraphRequest.Callback { response ->
                            try {
                                Log.e("JSON", response.toString())
                                val data = response.jsonObject

                                val id = data.getString("email")
                                val name = data.getString("name")

                                Application.id = id
                                Application.name = name;

                                Server.registrationProcessWithRetrofit(this@LoginActivity, id, name);
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                ).executeAsync()
            }
            override fun onCancel() {
                Log.i("bhc :", "Login attempt canceled.")
            }
            override fun onError(e: FacebookException) {
                Log.i("bhc :", "Login attempt failed.")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        setContentView(R.layout.activity_login)
        init()

        if( AccessToken.getCurrentAccessToken() != null ){
            Application.id = Profile.getCurrentProfile().id;
            Application.name = Profile.getCurrentProfile().name;


            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
            finish();
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        //페이스북 로그인
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun getAppKeyHash() {
        try {
            var something = ""
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                something = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key", something)
            }
            Log.d("lol key : ", something)
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        }

    }
}

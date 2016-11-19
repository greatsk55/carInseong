package com.example.user.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.user.network.Server
import com.facebook.login.LoginManager
import com.jakewharton.rxbinding.view.RxView

class SearchActivity : AppCompatActivity() {

    val num by lazy {
        findViewById(R.id.tv_CarNumber) as EditText
    }

    fun init(){
        RxView.clicks(findViewById(R.id.submit)).subscribe { v->
            Server.infoWithRetrofit(this@SearchActivity, num.text.toString());
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init()
    }

    override fun onDestroy() {
        super.onDestroy()

        LoginManager.getInstance().logOut()
    }
}

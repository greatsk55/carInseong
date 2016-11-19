package com.example.user

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.example.user.dto.CarList
import com.example.user.dto.Comment
import com.facebook.FacebookSdk
import java.util.*


/**
 * Created by user on 16. 11. 18.
 */
class Application : Application() {

    companion object user{
        var id : String ?= null;
        var name : String ?= null;
        var commentList : ArrayList<Comment> ?= null;
    }


    override fun onCreate() {
        super.onCreate()

        //facebook 초기화
        FacebookSdk.sdkInitialize(applicationContext)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

}
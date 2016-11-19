package com.example.user.network

import retrofit2.Retrofit
import com.example.user.dto.User
import android.widget.Toast
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v7.appcompat.R
import android.util.Log
import android.view.View
import com.example.user.dto.Comment
import com.example.user.ui.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.R.string.cancel
import android.app.*
import com.example.user.dto.CarList
import com.example.user.Application
import com.example.user.ui.InfoActivity
import retrofit2.http.Field


/**
 * Created by user on 16. 11. 18.
 */

object Server {
    val BASE_URL = "http://54.199.162.248/"

    val interfaceService: API_Interface
        get() {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(LenientGsonConverterFactory.create())
                    .build()

            val mInterfaceService = retrofit.create<API_Interface>(API_Interface::class.java)
            return mInterfaceService
        }

    //회원가입 요청
    fun registrationProcessWithRetrofit(context: Activity, id: String, name: String) {
        val mProgressDialog = ProgressDialog(context)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setProgressStyle(R.attr.progressBarStyle)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()

        val mApiService = Server.interfaceService
        val mService = mApiService.registration(id, name)

        mService.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                val mLoginObject = response.body()
                Log.i("aaa",mLoginObject.isLogin)
                val returnedResponse = mLoginObject.isLogin

                if (returnedResponse.trim({ it <= ' ' }) == "1") {
                    // redirect to Main Activity page

                    if (mProgressDialog.isShowing)
                        mProgressDialog.dismiss()

                    val returnedUserID = mLoginObject.id
                    val returnedName = mLoginObject.name

                    var intent = Intent(context, SearchActivity::class.java)
                    context.startActivity(intent)
                    ActivityCompat.finishAfterTransition(context)
                }
                if (returnedResponse.trim({ it <= ' ' }) == "0") {
                    // use the registration button to register
                    if (mProgressDialog.isShowing)
                        mProgressDialog.dismiss()

                    Toast.makeText(context, "can\'t join. please retry.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                call?.cancel()
                Log.i("aaa", t?.message)
                Toast.makeText(context, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show()
            }
        })
    }


    //편의점 카테고리별 데이터 요청
    fun infoWithRetrofit(context: Activity, carNumber:String) {
        val mProgressDialog = ProgressDialog(context)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setProgressStyle(R.attr.progressBarStyle)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()

        val mApiService = Server.interfaceService
        val mService = mApiService.info(carNumber)

        mService.enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {

                val mlistObject = response.body()

                //여기서 초기화 안해줘서 계속 중첩하며 아이템 증가함
                Application.commentList = ArrayList<Comment>()

                for (data in mlistObject as ArrayList<Comment>) {
                    Application.commentList!!.add(data)
                }

                if (mProgressDialog.isShowing)
                    mProgressDialog.dismiss()

                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("number",carNumber);
                context.startActivity(intent)
            }

            override fun onFailure(call: Call<List<Comment>>?, t: Throwable?) {
                call?.cancel()

                if (mProgressDialog.isShowing)
                    mProgressDialog.dismiss()

                Toast.makeText(context, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show()
            }
        })
    }

    //편의점 카테고리별 데이터 요청
    fun submitWithRetrofit(context: Activity, carNumber:String, name: String, comment: String) {
        val mProgressDialog = ProgressDialog(context)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setProgressStyle(R.attr.progressBarStyle)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()

        val mApiService = Server.interfaceService
        val mService = mApiService.submitPost(carNumber,name,comment)

        mService.enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {

                val mlistObject = response.body()

                //여기서 초기화 안해줘서 계속 중첩하며 아이템 증가함
                Application.commentList = ArrayList<Comment>()

                for (data in mlistObject as ArrayList<Comment>) {
                    Application.commentList!!.add(data)
                }

                if (mProgressDialog.isShowing)
                    mProgressDialog.dismiss()

                val intent = Intent(context, InfoActivity::class.java)
                intent.putExtra("number",carNumber);
                context.startActivity(intent);
                context.finish();
            }

            override fun onFailure(call: Call<List<Comment>>?, t: Throwable?) {
                call?.cancel()

                if (mProgressDialog.isShowing)
                    mProgressDialog.dismiss()

                Toast.makeText(context, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show()
            }
        })
    }
}
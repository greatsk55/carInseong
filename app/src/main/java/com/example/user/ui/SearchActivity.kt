package com.example.user.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.user.StringUtil
import com.example.user.network.Server
import com.facebook.login.LoginManager
import com.jakewharton.rxbinding.view.RxView



class SearchActivity : AppCompatActivity() {

    val num by lazy {
        findViewById(R.id.tv_CarNumber) as EditText
    }

    val mic by lazy{
        findViewById(R.id.mic) as ImageView
    }


    fun init(){
        RxView.clicks(mic).subscribe { v->
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            try {
                startActivityForResult(intent, 100)
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(applicationContext, "Your device is not supported!",
                        Toast.LENGTH_SHORT).show()
            }
        }

        RxView.clicks(findViewById(R.id.submit)).subscribe { v->
            Server.infoWithRetrofit(this@SearchActivity, num.text.toString());
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            100 -> {
                if (resultCode == RESULT_OK && null != data) {
                    val result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    if( result[0].equals("전송") ){
                        Server.infoWithRetrofit(this@SearchActivity, num.text.toString());
                        return ;
                    }

                    num.setText(StringUtil.StringReplace(result[0].trim()))


                }
            }
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

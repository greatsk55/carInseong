package com.example.user.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.user.Application
import com.example.user.StringUtil
import com.example.user.network.Server
import com.jakewharton.rxbinding.view.RxView


class InfoActivity : AppCompatActivity() {

    private val list by lazy { findViewById(R.id.list) as RecyclerView }
    private val num by lazy { intent.getStringExtra("number") }
    private val contents by lazy{ findViewById(R.id.input) as EditText}
    private val adapter by lazy { InfoAdapter() as InfoAdapter }
    val mic by lazy{
        findViewById(R.id.mic) as ImageView
    }

    fun init(){
        adapter.setEmptyView(layoutInflater.inflate(R.layout.row_empty, null))
        list.setAdapter(adapter)
        list.setLayoutManager(LinearLayoutManager(this));

        (findViewById(R.id.tv_CarNumber)as (TextView)).setText(if(num!=null) num else "12가1208")

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
        RxView.clicks(findViewById(R.id.toolbarBtnClose)).subscribe { x->finish() }
        RxView.clicks(findViewById(R.id.submit)).subscribe { x->
            Server.submitWithRetrofit(this@InfoActivity, num?:"12가1208", Application.name?:"unknown", contents.text.toString() );
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
                        Server.submitWithRetrofit(this@InfoActivity, num?:"12가1208", Application.name?:"unknown", contents.text.toString() );
                        return ;
                    }

                    contents.setText(StringUtil.StringReplace(result[0]))


                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        init()
    }
}

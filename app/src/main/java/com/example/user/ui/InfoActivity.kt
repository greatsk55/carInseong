package com.example.user.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.TextView
import com.example.user.Application
import com.example.user.network.Server
import com.jakewharton.rxbinding.view.RxView


class InfoActivity : AppCompatActivity() {

    private val list by lazy { findViewById(R.id.list) as RecyclerView }
    private val num by lazy { intent.getStringExtra("number") }
    private val contents by lazy{ findViewById(R.id.input) as EditText}
    private val adapter by lazy { InfoAdapter() as InfoAdapter }

    fun init(){
        adapter.setEmptyView(layoutInflater.inflate(R.layout.row_empty, null))
        list.setAdapter(adapter)
        list.setLayoutManager(LinearLayoutManager(this));

        (findViewById(R.id.tv_CarNumber)as (TextView)).setText(if(num!=null) num else "12가1208")

        RxView.clicks(findViewById(R.id.toolbarBtnClose)).subscribe { x->finish() }
        RxView.clicks(findViewById(R.id.submit)).subscribe { x->
            Server.submitWithRetrofit(this@InfoActivity, num?:"12가1208", Application.name?:"unknown", contents.text.toString() );
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        init()
    }
}

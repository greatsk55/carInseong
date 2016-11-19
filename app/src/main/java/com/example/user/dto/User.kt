package com.example.user.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by user on 16. 11. 18.
 */


data class User(@SerializedName("id")val id: String, @SerializedName("name")val name: String, @SerializedName("isLogin")val isLogin: String)

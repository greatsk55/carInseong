package com.example.user.network

import com.example.user.dto.CarList
import com.example.user.dto.Comment
import com.example.user.dto.User
import retrofit2.Call;
import retrofit2.http.*

/**
 * Created by user on 16. 11. 18.
 */


interface API_Interface {
    @FormUrlEncoded
    @POST("reg.php/{id}/{name}")
    fun registration(@Field("id") id: String, @Field("name") name: String ): Call<User>

    @FormUrlEncoded
    @POST("info.php/{carNumber}")
    fun info(@Field("carNumber") num: String) : Call<List<Comment>>


    @FormUrlEncoded
    @POST("post.php/{carNumber}/{name}/{comment}")
    fun submitPost(@Field("carNumber") num: String,@Field("name") name: String,@Field("comment") comment: String) : Call<List<Comment>>


}


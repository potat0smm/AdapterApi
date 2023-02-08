package com.example.adapterapi

import com.example.adapterapi.Api.Constanst.BASE_URL
import com.example.adapterapi.Interface.PostApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val retrofit: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostApi::class.java)
    }
}
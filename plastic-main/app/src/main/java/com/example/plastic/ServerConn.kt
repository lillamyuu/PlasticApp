package com.example.plastic

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServerConn {
    val httpLoggingInterceptor = HttpLoggingInterceptor()

    private lateinit var dbService: DBService
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plastic-server.herokuapp.com")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun connect(){
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        dbService = retrofit.create(DBService::class.java)

    }

    fun getInstance():DBService{
        return dbService
    }
}
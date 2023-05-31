package com.ssafy.flowexample1

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ssafy.flowexample1.database.UserDatabase
import com.ssafy.flowexample1.repository.Repository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {
    companion object {
        val retrofit: Retrofit by lazy {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).build()

            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()

            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }

        lateinit var repository: Repository
    }

    override fun onCreate() {
        super.onCreate()
        repository = Repository(applicationContext)
        Log.d("싸피 애플리케이션", "onCreate: repository 생성")
    }
}
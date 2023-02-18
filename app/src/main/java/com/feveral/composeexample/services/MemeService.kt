package com.feveral.composeexample.services

import com.feveral.composeexample.models.Meme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MemeService {
    @GET("memes/trending")
    suspend fun getTrendingMemes(): List<Meme>

    companion object {
        var memeService: MemeService? = null
        fun getInstance(): MemeService {
            if (memeService == null) {
                memeService = Retrofit.Builder()
                    .baseUrl("https://memery.app/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(MemeService::class.java)
            }
            return memeService!!
        }
    }
}
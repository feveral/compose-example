package com.feveral.composeexample.services

import com.feveral.composeexample.models.Meme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MemeService {
    @GET("memes/trending")
    suspend fun getTrendingMemes(@Query("limit") limit: Int, @Query("skip") skip: Int): List<Meme>

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
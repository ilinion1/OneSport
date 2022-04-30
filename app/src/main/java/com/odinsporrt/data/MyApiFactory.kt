package com.odinsporrt.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyApiFactory {
    private const val BASE_URL = "https://hgfjkumntuk756.xyz/"

    fun create() : MyApiService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return retrofit.create(MyApiService::class.java)
    }
}
package com.odinsporrt.data

import retrofit2.http.GET

interface MyApiService {

    @GET("pumc.json")
    suspend fun getDataServer(): Contain
}
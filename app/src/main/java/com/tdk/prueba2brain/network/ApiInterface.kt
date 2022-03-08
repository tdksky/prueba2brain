package com.tdk.prueba2brain.network

import com.tdk.prueba2brain.model.GeneralRatesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {



    @GET("latest")
    fun getLatestRates(@Query ("access_key") accessToken : String = ACCESS_TOKEN) : Call<GeneralRatesResponse>

    companion object {

        private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

        const val ACCESS_TOKEN = "37e8240d60df50e26820c5448d99558b"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }

}
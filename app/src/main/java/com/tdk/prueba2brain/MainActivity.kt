package com.tdk.prueba2brain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.tdk.prueba2brain.model.GeneralRatesResponse
import com.tdk.prueba2brain.network.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callApi()
    }

    override fun onStart() {
        super.onStart()

    }

    private fun callApi(){
        val apiInterface = ApiInterface.create().getLatestRates()

        apiInterface.enqueue( object : Callback<GeneralRatesResponse>{
            override fun onResponse(call: Call<GeneralRatesResponse>?, response: Response<GeneralRatesResponse>?) {

                if(response?.body() != null){
                    Log.i("JOELAPI EXITOSO", response.body().toString())
                    setUpSpinner1(response.body().rates.keys.toList())
                }

            }

            override fun onFailure(call: Call<GeneralRatesResponse>?, error: Throwable?) {

                Log.i("JOELAPI ERROR", error.toString())

            }
        })

    }

    private fun setUpSpinner1(list: List<String>){
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, list)
        spinner_cambiario.adapter = adapter
    }
}
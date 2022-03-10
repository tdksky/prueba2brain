package com.tdk.prueba2brain.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.tdk.prueba2brain.MainActivity
import com.tdk.prueba2brain.R
import com.tdk.prueba2brain.model.GeneralRatesResponse
import com.tdk.prueba2brain.network.ApiInterface
import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        callApi()
        button_fxrates.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    override fun onStart() {
        super.onStart()

    }

    private fun callApi(){
        val apiInterface = ApiInterface.create().getLatestRates()

        apiInterface.enqueue( object : Callback<GeneralRatesResponse> {
            override fun onResponse(call: Call<GeneralRatesResponse>?, response: Response<GeneralRatesResponse>?) {

                if(response?.body() != null){
                    Log.i("JOELAPI EXITOSO", response.body().toString())
                    setUpSpinner3(response.body().rates.keys.toList())
                    setUpSpinner4(response.body().rates.keys.toList())
                }

            }

            override fun onFailure(call: Call<GeneralRatesResponse>?, error: Throwable?) {

                Log.i("JOELAPI ERROR", error.toString())

            }
        })

    }

    private fun setUpSpinner3(list: List<String>){
        val adapter3 = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, list)
        spinner_cambiario3.adapter = adapter3

    }
    private fun setUpSpinner4(list: List<String>){
        val adapter4 = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, list)
        spinner_cambiario4.adapter = adapter4

    }
}
package com.tdk.prueba2brain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.tdk.prueba2brain.adapter.CustomDropDownAdapter
import com.tdk.prueba2brain.model.GeneralRatesResponse
import com.tdk.prueba2brain.network.ApiInterface
import com.tdk.prueba2brain.ui.CalculatorActivity
import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callApi()
        button_calcular.setOnClickListener(){
            val intent = Intent(this, CalculatorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
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
                    setUpSpinner2(response.body().rates.keys.toList())
                }

            }

            override fun onFailure(call: Call<GeneralRatesResponse>?, error: Throwable?) {

                Log.i("JOELAPI ERROR", error.toString())

            }
        })

    }

    private fun setUpSpinner1(list: List<String>){
        val customDropDownAdapter = CustomDropDownAdapter(this, list)
        spinner_cambiario.adapter = customDropDownAdapter

    }
    private fun setUpSpinner2(list: List<String>){

        val customDropDownAdapter = CustomDropDownAdapter(this, list)
        spinner_cambiario2.adapter = customDropDownAdapter


    }
}
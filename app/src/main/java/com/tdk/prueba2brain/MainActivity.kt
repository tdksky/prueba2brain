package com.tdk.prueba2brain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.tdk.prueba2brain.adapter.CustomDropDownAdapter
import com.tdk.prueba2brain.model.GeneralRatesResponse
import com.tdk.prueba2brain.network.ApiInterface
import com.tdk.prueba2brain.ui.CalculatorActivity
import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var ratesItem : Map<String, Double> = mapOf()
    var spinnerOrigenValue : Double? = 0.0
    var spinnerDestinoValue : Double? = 0.0
    val df = DecimalFormat("#.###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callApi()
        button_calcular.setOnClickListener(){
            val intent = Intent(this, CalculatorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        spinner_cambiario.onItemSelectedListener = this
        spinner_cambiario2.onItemSelectedListener = this
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
                    ratesItem = response.body().rates
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

    override fun onItemSelected(parent : AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(parent?.id){
            R.id.spinner_cambiario-> {

                spinnerOrigenValue = ratesItem[spinner_cambiario.selectedItem]
                if(spinnerDestinoValue != null){

                    calculoRate(spinnerOrigenValue!!, spinnerDestinoValue!!)
                    codigoRate(spinner_cambiario.selectedItem as String,spinner_cambiario2.selectedItem as String)
                }
            }
            R.id.spinner_cambiario2->{
                spinnerDestinoValue = ratesItem[spinner_cambiario2.selectedItem]
                calculoRate(spinnerOrigenValue!!, spinnerDestinoValue!!)
                codigoRate(spinner_cambiario.selectedItem as String,spinner_cambiario2.selectedItem as String)
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun calculoRate(origen: Double, destino: Double){
        label_destino.text = df.format((1/origen)*destino)
        label_origen.text = "1"
    }

    private fun codigoRate(origen: String, destino: String){
        label_origen_code.text = origen
        label_destino_code.text = destino

    }
}
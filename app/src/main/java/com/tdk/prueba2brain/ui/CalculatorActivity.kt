package com.tdk.prueba2brain.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tdk.prueba2brain.MainActivity
import com.tdk.prueba2brain.R
import com.tdk.prueba2brain.adapter.CustomDropDownAdapter
import com.tdk.prueba2brain.model.GeneralRatesResponse
import com.tdk.prueba2brain.network.ApiInterface
import kotlinx.android.synthetic.main.activity_calculator.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class CalculatorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var ratesItem : Map<String, Double> = mapOf()
    var spinnerOrigenValue : Double? = 0.0
    var spinnerDestinoValue : Double? = 0.0
    val df = DecimalFormat("#.###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        callApi()
        textObserver()
        button_fxrates.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        spinner_cambiario3.onItemSelectedListener = this
        spinner_cambiario4.onItemSelectedListener = this
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
                    ratesItem = response.body().rates
                }

            }

            override fun onFailure(call: Call<GeneralRatesResponse>?, error: Throwable?) {

                Log.i("JOELAPI ERROR", error.toString())

            }
        })

    }

    private fun setUpSpinner3(list: List<String>){
        val customDropDownAdapter = CustomDropDownAdapter(this, list)
        spinner_cambiario3.adapter = customDropDownAdapter

    }
    private fun setUpSpinner4(list: List<String>){
        val customDropDownAdapter = CustomDropDownAdapter(this, list)
        spinner_cambiario4.adapter = customDropDownAdapter

    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(parent?.id){
            R.id.spinner_cambiario3-> {

                if (spinnerDestinoValue != null){

                }

            }
            R.id.spinner_cambiario4-> {
                spinnerDestinoValue = ratesItem[spinner_cambiario4.selectedItem]
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun calculoRate(origen: Double, destino: Double, cantidad: Int){

        label_price3.text = df.format(((1/origen)*destino)*cantidad)

    }


    private fun textObserver() {
        text_cantidad.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (!text.isNullOrEmpty()){
                    calculoRate(spinnerOrigenValue!!, spinnerDestinoValue!! , text.toString().toInt())
                    spinnerOrigenValue = ratesItem[spinner_cambiario3.selectedItem]
                }else{
                    label_price3.text = "0"
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

}
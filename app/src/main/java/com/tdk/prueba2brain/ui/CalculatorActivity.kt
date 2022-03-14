package com.tdk.prueba2brain.ui

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import java.util.*
import kotlin.math.sqrt

class CalculatorActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var ratesItem : Map<String, Double> = mapOf()
    var spinnerOrigenValue : Double? = 0.0
    var spinnerDestinoValue : Double? = 0.0
    val df = DecimalFormat("#.###")
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        callApi()
        textObserver()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

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
                    setUpSpinner3(response.body().rates.keys.toList())
                    setUpSpinner4(response.body().rates.keys.toList())
                    ratesItem = response.body().rates
                }

            }

            override fun onFailure(call: Call<GeneralRatesResponse>?, error: Throwable?) {

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
                spinnerDestinoValue = ratesItem[spinner_cambiario3.selectedItem]

                if (spinnerDestinoValue != null && spinnerOrigenValue != null && !text_cantidad.text.isNullOrEmpty()){
                    calculoRate(spinnerOrigenValue!!, spinnerDestinoValue!! , text_cantidad.text.toString().toInt())
                }

            }
            R.id.spinner_cambiario4-> {
                spinnerDestinoValue = ratesItem[spinner_cambiario4.selectedItem]

                if (spinnerDestinoValue != null && spinnerOrigenValue != null && !text_cantidad.text.isNullOrEmpty()){
                    calculoRate(spinnerOrigenValue!!, spinnerDestinoValue!! , text_cantidad.text.toString().toInt())
                }
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

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 10) {
                Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
                clearData()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun clearData(){

        label_price3.text = "0"
        text_cantidad.text.clear()
    }

}
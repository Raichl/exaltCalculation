package com.valab.exaltcalculation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.floor
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val tvPrice : TextView by lazy { findViewById(R.id.tvPrice) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvCalcAnswer = findViewById<TextView>(R.id.tvCalcAnswer)
        val etCalcNumber = findViewById<EditText>(R.id.etСalcNumber)


        checkPrice().start()

        etCalcNumber.doAfterTextChanged {
            val numb = it.toString().toFloatOrNull()
            tvCalcAnswer.text = if (numb != null ){
                priced(numb)
            }else{ this.getString(R.string.incorrectValue) }
        }
    }

    private fun priced(calcNumber: Float): String{
        return (calcNumber * PriceObject.exaltPrice).roundToInt().toString()
    }


    private fun checkPrice() = Thread{
        val jsonAsStr = getDataFromServer()
        val jsonObject = JSONTokener(jsonAsStr).nextValue() as JSONObject
        val jsonArray = jsonObject.getJSONArray("lines")
        for (i in 0 until jsonArray.length()){
            if (jsonArray.getJSONObject(i).getString("currencyTypeName") == "Exalted Orb"){
                val price = jsonArray.getJSONObject(i)
                    .getJSONObject("receive")
                    .getDouble("value")
                PriceObject.exaltPrice = floor(price * 100) / 100.0
                runOnUiThread{  tvPrice.text = PriceObject.exaltPrice.toString() }
            }
        }


    }

    private fun getDataFromServer(): String {
        val exaltURL = URL(this.getString(R.string.ninjaExaltPriceURL))
        val urlConnection = exaltURL.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.connectTimeout = 5000
        urlConnection.readTimeout = 5000

        return urlConnection.inputStream.bufferedReader().readText()
    }
}
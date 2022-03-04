package com.valab.exaltcalculation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvCalcAnswer = findViewById<TextView>(R.id.tvCalcAnsver)
        val etCalcNumber = findViewById<EditText>(R.id.etСalcNumber)

        checkPrice()
        
        tvPrice.text = PriceObject.toString()
        etCalcNumber.doAfterTextChanged {
            val numb = it.toString().toFloatOrNull()
            tvCalcAnswer.text = if (numb != null ){
                priced(numb)
            }else{ this.getString(R.string.incorrectValue) }
        }
    }

    private fun priced(calcNumber: Float): String{
        return (calcNumber * PriceObject.exaltPrice).toString()
    }

    private fun checkPrice(){
        val exaltURL = this.getString(R.string.ninjaExaltPriceURL)
        val doc = Jsoup.connect(exaltURL).get()
        val valueBlock = doc.select("span[class=css-fko97h]")
        val value = valueBlock.select("div")
            .select("span")
            .select("font")
            .text()
        PriceObject.exaltPrice = if (value.toFloatOrNull() != null){
            value.toFloat()
        }else{
            0f
        }
    }
}
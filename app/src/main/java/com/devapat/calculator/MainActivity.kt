package com.devapat.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

enum class MathSign {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

class MainActivity : AppCompatActivity() {

    private lateinit var displayTv: TextView
    private lateinit var btnNumberArray: Array<Button>
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var btnClearText: Button
    private lateinit var btnEqual: Button
    private var lastDot = false
    private var lastMathSign: MathSign? = null

    private var auxValue: BigDecimal? = null
    private var inputValue = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTv = findViewById(R.id.displayTv)
        btnNumberArray = arrayOf(
            findViewById(R.id.btn_zero),
            findViewById(R.id.btn_one),
            findViewById(R.id.btn_two),
            findViewById(R.id.btn_three),
            findViewById(R.id.btn_four),
            findViewById(R.id.btn_five),
            findViewById(R.id.btn_six),
            findViewById(R.id.btn_seven),
            findViewById(R.id.btn_eight),
            findViewById(R.id.btn_nine),
            findViewById(R.id.btn_point)
        )
        btnAdd = findViewById(R.id.btn_plus)
        btnSubtract = findViewById(R.id.btn_subtract)
        btnMultiply = findViewById(R.id.btn_multiply)
        btnDivide = findViewById(R.id.btn_divide)
        btnClearText = findViewById(R.id.btn_clear)
        btnEqual = findViewById(R.id.btn_equal)


        btnClearText.text = "AC"

        displayTv.text = inputValue

        btnNumberArray.forEach { btn ->
            btn.setOnClickListener {
                if (((btn.text == "." && !lastDot) || (btn.text != ".")) && inputValue.length <= 12) {
                    if(btn.text == ".") lastDot = true
                    if (inputValue == "") {
                        btnClearText.text = "C"
                    }
                    updateInput(inputValue + btn.text as String)
                }
            }
        }

        btnAdd.setOnClickListener {
            signPreprocess(MathSign.ADD)
        }

        btnSubtract.setOnClickListener {
            if (inputValue == "") {
                updateInput("-")
            } else {
                signPreprocess(MathSign.SUBTRACT)
            }
        }

        btnMultiply.setOnClickListener {
            signPreprocess(MathSign.MULTIPLY)
        }

        btnDivide.setOnClickListener {
            signPreprocess(MathSign.DIVIDE)
        }

        btnEqual.setOnClickListener {
            if(inputValue == "") {
                updateInput(auxValue.toString(), null )
                auxValue = null
                lastMathSign = null
            } else if (lastMathSign != null && auxValue != null ) {
                try {
                    val inputValueMath: BigDecimal = inputValue.toBigDecimal()
                    when (lastMathSign) {
                        MathSign.ADD -> auxValue = auxValue!!.add(inputValueMath)
                        MathSign.SUBTRACT -> auxValue = auxValue!!.subtract(inputValueMath)
                        MathSign.MULTIPLY -> auxValue = auxValue!!.multiply(inputValueMath)
                        MathSign.DIVIDE -> auxValue = auxValue!!.divide(inputValueMath, MathContext.DECIMAL32)
                    }
                } catch (e: Exception) {
                    displayToast(e.toString())
                }
                updateInput(auxValue.toString(), null)
                auxValue = null
                lastMathSign = null
            }
        }

        btnClearText.setOnClickListener{
            if (inputValue == "") {
                auxValue = null
                lastMathSign = null
                lastDot = false
            } else {
                btnClearText.text = "AC"
                lastDot = false
            }
            updateInput("")
        }
        
    }

    private fun updateInput(inputValueTemp: String, auxValueTemp: BigDecimal? = auxValue) {
        inputValue = inputValueTemp
        val displayText = "${auxValueTemp ?: ""}\n$inputValue"
        displayTv.text = displayText
    }

    private fun checkInputValue(tempInputValue: String): Boolean {
        if (tempInputValue == "") {
            displayToast("Enter valid input value!")
            return false
        }
        return true
    }

    private fun signPreprocess(mathSignInput: MathSign) {
        if(checkInputValue(inputValue)) {
            if (lastMathSign != null && auxValue != null) {
                val inputValueMath: BigDecimal = inputValue.toBigDecimal()
                try {
                    when (lastMathSign) {
                        MathSign.ADD -> auxValue = auxValue!!.add(inputValueMath)
                        MathSign.SUBTRACT -> auxValue = auxValue!!.subtract(inputValueMath)
                        MathSign.MULTIPLY -> auxValue = auxValue!!.multiply(inputValueMath)
                        MathSign.DIVIDE -> auxValue = auxValue!!.divide(inputValueMath, MathContext.DECIMAL32)
                    }
                    lastMathSign = mathSignInput
                    updateInput("")
                } catch (e: Exception) {
                    displayToast(e.toString())
                }
            } else {
                try {
                    auxValue = inputValue.toBigDecimal()
                    lastMathSign = mathSignInput
                    updateInput("")
                } catch (e: Exception) {
                    displayToast(e.toString())
                }
            }
        }
        lastDot = false
    }

    fun displayToast(value: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, inputValue, time).show()
    }
}
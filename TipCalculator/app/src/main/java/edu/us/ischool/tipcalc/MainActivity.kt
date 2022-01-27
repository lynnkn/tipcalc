package edu.us.ischool.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    // variables
    lateinit var tipBtn : Button
    lateinit var editText : EditText
    lateinit var tipSpinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize variables
        tipBtn = findViewById<Button>(R.id.tipBtn)
        editText = findViewById<EditText>(R.id.editText)
        tipSpinner = findViewById<Spinner>(R.id.tipSpinner)

        // disable button
        tipBtn.setEnabled(false)
        // setting up spinner values
        val tips = resources.getStringArray(R.array.tip_amounts)
        if (tipSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, tips
            )
            tipSpinner.adapter = adapter
        }

        // text input listener
        editText.addTextChangedListener(object: TextWatcher {
            // disable button
            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                var input = editText.text.toString()
                if (input.isEmpty() || input == "$") {
                    tipBtn.setEnabled(false)
                } else {
                    tipBtn.setEnabled(true)
                    formatText()
                }
            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int,
                                           after:Int) {
                // TODO Auto-generated method stub
            }
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })

        // tip button listener
        tipBtn.setOnClickListener{
            // cleaning input
            val tipString = tipSpinner.getSelectedItem().toString()
            val tipDouble = tipString.substring(0..(tipString.length - 2)).toInt() / 100.0
            val amountString = editText.text.toString().substring(1)

            // calculating tip
            calculateTip(amountString.toDouble(), tipDouble)
        }
    }

    private fun formatText() : Unit {
        var newText = editText.text.toString()

        // add dollar sign before input
        if (!newText.startsWith("$")) {
            newText = "$" +  newText
            editText.setText(newText);
            //Cursor moves to the end
            editText.setSelection(newText.length);
        }

        // handles decimal edge cases
        if (newText.contains(".")) {
            // if there are more than 2 decimal places
            if (newText.length - newText.indexOf(".") > 3) {
                newText = newText.substring(0..(newText.indexOf(".") + 2));
                editText.setText(newText);
                //Cursor moves to the end
                editText.setSelection(newText.length);
            }
        }
    }

    // calculates the tip amount for whatever amount is in the edit text
    private fun calculateTip(amount: Double, tip: Double = 0.15) {
        val rounded  =  Math.round((amount * tip) * 100.0) / 100.0
        var roundedStr = rounded.toString()
        // making sure output is always 2 decimal places
        if (roundedStr.length - roundedStr.indexOf(".") <= 2) {
            roundedStr += "0"
        }
        Toast.makeText(this, "$${roundedStr}", Toast.LENGTH_SHORT).show()
    }
}
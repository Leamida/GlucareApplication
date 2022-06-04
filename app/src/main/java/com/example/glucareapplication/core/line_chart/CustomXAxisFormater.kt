package com.example.glucareapplication.core.line_chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisFormater(private val xValsDateLabel: ArrayList<String>) : ValueFormatter() {


    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        return if (value.toInt() >= 0 && value.toInt() <= xValsDateLabel.size - 1) {
            xValsDateLabel[value.toInt()]
        } else {
            ("").toString()
        }
    }
}
package com.example.glucareapplication.core.line_chart

import com.github.mikephil.charting.formatter.ValueFormatter

class CustomYValueFormater:ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()} mg/dL"
    }
}
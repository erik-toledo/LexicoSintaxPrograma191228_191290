package com.example.analizadorsintactico

import android.util.Log

class Lexico {

    private var symbols: MutableMap<String, String> = mutableMapOf()
    private var canNueva: String = ""
    private var iterator: Int = 0

    fun analyzer(can: String): MutableMap<String, String> {
        var i = 0
        canNueva = can

        val tableKey: List<String> = listOf("x^2", "Numbers", "F(x)", "x", "+", "-", "/", "$", "=")
        do {
            if (canNueva.contains(tableKey[i]) || tableKey[i] == "Numbers") {
                if (tableKey[i] == "Numbers") {
                    counterNumbers(tableKey[i])
                    i++
                } else {
                    searchKey(tableKey[i])
                }
            } else {
                symbols.put(tableKey[i], iterator.toString())
                iterator = 0
                i++
            }
        } while (i <= (tableKey.size - 1))
        symbols.put("Error",canNueva)
        return symbols
    }

    private fun searchKey(key: String) {
        iterator++
        val index: Int = canNueva.indexOf(key)
        val newKey: String = canNueva.substring(index, (index + key.length))
        if (key == (newKey)) canNueva = canNueva.removeRange(index, (index + key.length))
    }

    private fun counterNumbers(key: String) {
        var number = ""
        canNueva.forEach {
            if (it.isDigit()) {
                number += it
            } else {
                if (number.isNotEmpty()) {
                    canNueva = canNueva.replace(number, "")
                    iterator++
                    number = ""
                }
            }
        }
        if (number.isNotEmpty()) iterator++
        canNueva = canNueva.replace(number, "")
        symbols.put(key = key, value = iterator.toString())
        iterator = 0
    }

}
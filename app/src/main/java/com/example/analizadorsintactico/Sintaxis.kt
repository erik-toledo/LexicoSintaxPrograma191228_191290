package com.example.analizadorsintactico

import android.util.Log
import androidx.core.text.isDigitsOnly
import java.util.*
import kotlin.collections.ArrayList

class Sintaxis {
    var pila: Stack<String> = Stack()
    private val simbolosFinales: ArrayList<String> = arrayListOf("F(x)", "=", "[0-9]*", "[1-9]*", "x", "x^2", "+", "-", "$", "!", "/")
    private var canNew: MutableList<String> = mutableListOf()
    private var tablaAnalysis = arrayOf(
        arrayOf(" ", "[0-9]*", "[1-9]*", "+", "-", "F(x)", "x", "x^2", "=", "$", "/", "!"),
        arrayOf("Funcion", "", "", "", "", "F(x)", "", "", "", "", "", ""),
        arrayOf("Igual", "", "", "", "", "", "", "", "=", "", "", ""),
        arrayOf("Constante", "Digito RFraccion", "Digito2 Digito", "", "", "", "", "", "", "$ Digito2", "", ""),
        arrayOf("Digito", "[0-9]*", "", "", "", "", "", "", "", "", ""),
        arrayOf("Digito2", "", "[1-9]*", "", "", "", "", "", "", "", "", ""),
        arrayOf("RFraccion", "", "", "", "", "", "32", "32", "", "32", "/ Digito2", "32"),
        arrayOf("Variable", "", "", "", "", "", "x", "", "", "", "", ""),
        arrayOf("Variable2", "", "", "", "", "", "", "x^2", "", "", "", ""),
        arrayOf("Signo", "32", "32", "+", "-", "", "", "", "", "32", "", ""),
        arrayOf("Derivada", "", "", "", "", "Funcion Igual Signo Constante Variable Signo Constante", "", "", "", "", ""),
        arrayOf("Derivada2", "", "", "", "", "Funcion Igual Signo Constante Variable2 Signo Constante Variable Signo Constante", "", "", "", "", "", ""),
    )

    fun methodPredictive(cadena: String): String? {
        var i = 0
        val tipoDerivada = pila.firstElement()
        val canNew = cadena.split("\\s+".toPattern()).toMutableList()
        if (canNew[canNew.indexOf("=") + 1] != "+" && canNew[canNew.indexOf("=") + 1] != "-") {
            canNew.add(canNew.indexOf("=") + 1, "+")
            canNew.add("!")
        }else canNew.add("!")

        do {
            if (simbolosFinales.contains(pila.firstElement())) {
                if (pila.firstElement().equals(canNew[i]) || Regex(pila.firstElement()).matches(canNew[i])) {
                    Log.d("Se elimina :", pila.firstElement())
                    pila.remove(pila.firstElement())
                    i++
                    if(pila.isEmpty())pila.add(0,"32")
                }else{
                    pila.add(0,"32")
                }
            } else {
                tablaPredictive(pila.firstElement(), canNew[i], i)
            }
        } while (pila.firstElement() != "32")
       if( i == (canNew.size-1) && pila.size == 1) return tipoDerivada
        return ""
    }

    private fun tablaPredictive(Xpila: String, caracter: String, size: Int) {
        var cadena = caracter
        var band = false
        Log.d("Interseccion entre", "$Xpila $caracter")
        for (i in 0..11) {
            if (tablaAnalysis[i][0] == Xpila)  {
                cadena = when (true) {
                    Xpila == "Digito2" -> tablaAnalysis[0][2]
                    Xpila == "Digito" -> tablaAnalysis[0][1]
                    (cadena.isDigitsOnly() && Xpila == "Constante") -> tablaAnalysis[0][1]
                    else -> cadena
                }
                for (j in 0..11) {
                    if (tablaAnalysis[0][j] == cadena) {
                        val produccion = tablaAnalysis[i][j].split(" ").toMutableList()
                        Log.d("=",produccion.toString())
                        if (produccion[0] != "32" || size == (canNew.size - 1)) {
                            pila.remove(Xpila)
                            pila.forEach { produccion += it }
                            pila.clear()
                            pila.addAll(produccion)
                            band = true
                            break
                        }else{
                            pila.remove(Xpila)
                            band = true
                            break
                        }
                    }else band = false
                }
                if(band) break
            }else band = false
        }
        if(!band || pila.size ==0 )pila.add(0,"32")
        Log.d("Pila actulizada",pila.toString())
    }
}
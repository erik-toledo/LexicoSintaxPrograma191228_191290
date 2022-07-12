package com.example.analizadorsintactico

import android.util.Log
import kotlin.math.sqrt

class Numeros {
    fun tipoDerivada(funcion: String): MutableList<String> {
        return if (funcion.contains("x^2"))
            funCuadratica(funcion)
        else funLineal(funcion)
    }

    private fun funCuadratica(funcion: String): MutableList<String> {
        val expresion =
            funcion.replace("F(x)", "").replace("=", "").replace("x^2", "").replace("x", "")
        val nuevaExpr = expresion.split("\\s+".toRegex()).toMutableList()
        nuevaExpr.removeAt(0)
        var num1 = 0.0f
        var num2 = 0.0f
        var num3 = 0.0f
        val signo:String
        val signo2:String
        val iteradores = busquedaSignos(nuevaExpr)
        val bloque1 = nuevaExpr.subList(0, iteradores[0].toInt())
        val bloque2 = nuevaExpr.subList(iteradores[0].toInt() + 1, iteradores[1].toInt())
        val bloque3 = nuevaExpr.subList(iteradores[1].toInt() + 1, nuevaExpr.size)
        Log.d("xd","$bloque1 $bloque2 $bloque3")
        //------------------------------------------------------------------------------------
        if (bloque1.contains("/")) num1 = fracciones(bloque1)
        else if (bloque1.contains("-")) num1 = numeroNegativo(bloque1)
        else if(bloque1.contains("+"))num1 = numeroPositivo(bloque1)
        else if (bloque1.contains("$")) num1 = numeroConRaiz(bloque1)
        else bloque1.forEach { num1 += it.toFloat() }
        /*Bloque 2*/
        if (bloque2.contains("/")) num2 = fracciones(bloque2)
        else if (bloque2.contains("$")) num2 = numeroConRaiz(bloque2)
        else bloque2.forEach { num2 += it.toFloat() }
        /*Bloque 3*/
        if (bloque3.contains("/")) num3 = fracciones(bloque3)
        else if (bloque1.contains("-")) num1 = numeroNegativo(bloque1)
        else if (bloque3.contains("$")) num3 = numeroConRaiz(bloque3)
        else bloque3.forEach { num3 += it.toFloat() }
        signo = nuevaExpr[iteradores[0].toInt()]
        signo2 = nuevaExpr[iteradores[1].toInt()]
        Log.d("Nuevos", "\n$num1$signo$num2$signo2$num3")
        return mutableListOf(num1.toString(), num2.toString(), num3.toString(), signo, signo2, "C")
    }

    private fun funLineal(funcion: String): MutableList<String> {

        val expresion = funcion.replace("F(x)", "").replace("x", "").replace("=", "")
        val nuevaExpr = expresion.split("\\s+".toRegex()).toMutableList()
        var num1 = 0.0f
        var num2 = 0.0f
        nuevaExpr.removeAt(0)
        var iterador = nuevaExpr.lastIndexOf("-")
        Log.d("ns", iterador.toString())
        if (iterador < 1) iterador = nuevaExpr.indexOf("+")
        Log.d("+", iterador.toString())
        val bloque1 = nuevaExpr.subList(0, iterador)
        val bloque2 = nuevaExpr.subList(iterador + 1, nuevaExpr.size)
        val signo = nuevaExpr[iterador]

        if (bloque1.contains("/")) num1 = fracciones(bloque1)
        else if (bloque1.contains("-")) num1 = numeroNegativo(bloque1)
        else if(bloque1.contains("+")) num1 = numeroPositivo(bloque1)
        else if (bloque1.contains("$")) num1 = numeroConRaiz(bloque1)
        else bloque1.forEach { num1 += it.toFloat() }
        /*Bloque 2*/
        if (bloque2.contains("/")) num2 = fracciones(bloque2)
        else if (bloque2.contains("$")) num2 = numeroConRaiz(bloque2)
        else if(bloque2.contains("+")) num2 = numeroPositivo(bloque2)
        else bloque2.forEach { num2 += it.toFloat() }
        return mutableListOf(num1.toString(), num2.toString(), signo, "L")
    }

    private fun fracciones(bloque1: MutableList<String>): Float {
        var x = ""
        val indexSignoDivisor = bloque1.indexOf("/")
        val list = bloque1.subList(0, indexSignoDivisor)
        val list2 = bloque1.subList(indexSignoDivisor + 1, bloque1.size)
        list.forEach { x += it }
        val numFraccionario = x.toFloat()
        x = ""
        list2.forEach { x += it }
        return numFraccionario / x.toFloat()

    }

    private fun numeroNegativo(bloque1: MutableList<String>): Float {
        var numAux = ""
        if (bloque1.contains("$")) numAux = numeroConRaiz(bloque1).toString()
        else bloque1.forEach { if (it != "-") numAux += it }
        return -numAux.toFloat()
    }

    private fun numeroConRaiz(bloque1: MutableList<String>): Float {
        var numero = ""
        bloque1.forEach { if (it != "$" && it != "-") numero += it }
        return sqrt(numero.toFloat())
    }

    private fun busquedaSignos(funcion: MutableList<String>): MutableList<String> {
        var it = ""
        for (i in 1 until funcion.size) {
            if (funcion[i] == "+" || funcion[i] == "-") it += i
        }
        val iterador1:Int = it[0].toString().toInt()
        val iterador2:Int = it[1].toString().toInt()
        return mutableListOf(iterador1.toString(), iterador2.toString())
    }
    private fun numeroPositivo(bloque1: MutableList<String>): Float
    {
        var numAux = ""
        if (bloque1.contains("$")) numAux = numeroConRaiz(bloque1).toString()
        else bloque1.forEach { if (it != "+") numAux += it }
        return +numAux.toFloat()
    }
    //F(x) = 2 x^2 + 3 x + 2
    //2+3+2
}
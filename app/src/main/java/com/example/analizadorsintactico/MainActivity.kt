package com.example.analizadorsintactico

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.analizadorsintactico.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bandera = false
    private var tipoDerivada = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAnalizar.setOnClickListener {
            var informacion = iniciarLexico()
            if(!informacion.contains("Error"))
            {
                informacion += "\n${iniciarSintaxis()}"
                binding.textViewNumeros.text = informacion
                if (bandera) graficaPython()
                else binding.textViewNumeros.text = informacion
            }else
            {
                binding.textViewNumeros.text = informacion
            }


        }
    }

    private fun iniciarSintaxis(): String {
        var derivada: String
        val sintax = Sintaxis()
        sintax.pila.push("Derivada2")
        for (i in (0..1)) {
            derivada = sintax.methodPredictive(binding.editTextDerivada.text.toString()).toString()
            if (derivada == "Derivada" || derivada == "Derivada2") {
                bandera = true
                tipoDerivada = derivada
                return if (derivada == "Derivada2") "Sin error sintactico\n Funcion cudratica"
                else "Sin error sintactico\nFuncion lineal"
            } else {
                sintax.pila.clear()
                sintax.pila.push("Derivada")
            }
        }
        bandera = false
        return "Error sintactico"
    }

    private fun iniciarLexico(): String {
        val init = Lexico()
        var information = ""
        binding.editTextDerivada.text.toString().forEach {
            if (it != ' ') information += it.toString()
        }
        val error = init.analyzer(information)["Error"]
        return if (error?.isEmpty() == true) "Sin error lexico"
        else "Error lexico: $error"
    }

    private fun graficaPython() {
        val py = Numeros()
        val parametros = py.tipoDerivada(binding.editTextDerivada.text.toString())
        try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this))
            }
            val pythom = Python.getInstance()
            val bytes: ByteArray
            val pyObjt :PyObject
            if (parametros[parametros.size - 1] == "L") {
                pyObjt = pythom.getModule("linealFun")
                bytes = pyObjt.callAttr("main",
                    parametros[0].toFloat(),
                    parametros[1].toFloat(),
                    parametros[2]).toJava(ByteArray::class.java)
            } else {
                pyObjt = pythom.getModule("cuadraticFun")
                bytes = pyObjt.callAttr("main",
                    parametros[0].toFloat(),
                    parametros[1].toFloat(),
                    parametros[2].toFloat(),
                    parametros[3],
                    parametros[4]).toJava(ByteArray::class.java)
            }
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imgVwGrafica.setImageBitmap(bitmap)
            currentFocus?.let {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(it.windowToken, 0)
            }

        } catch (e: PyException) {
            Log.d("Error", e.toString())
        }


    }

}



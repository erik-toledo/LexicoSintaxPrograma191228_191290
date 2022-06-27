package com.example.analizadorsintactico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.analizadorsintactico.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAnalizar.setOnClickListener {
            var informacion = iniciarLexico()
            informacion += "\n${iniciarSintaxis()}"
            binding.textViewNumeros.text = informacion
        }
    }

    private fun iniciarSintaxis() : String {
        var derivada: String
        val sintax = Sintaxis()
        sintax.pila.push("Derivada2")
        for(i in (0..1))
        {
            derivada = sintax.methodPredictive(binding.editTextDerivada.text.toString()).toString()
            if(derivada =="Derivada" || derivada =="Derivada2") {
                return if (derivada =="Derivada2") "Sin error sintactico\n Funcion cudratica"
                else "Sin error sintactico\nFuncion lineal"
            }else {
                sintax.pila.clear()
                sintax.pila.push("Derivada")
            }
        }
        return "Error sintactico"
    }
    private fun iniciarLexico(): String {
        val init = Lexico()
        var information = ""
        binding.editTextDerivada.text.toString().forEach {
            if (it != ' ') information += it.toString()
        }
        var error = init.analyzer(information)["Error"]
        return if (error?.isEmpty() == true) "Sin error lexico"
        else "Error lexico: $error"
    }
}



package com.example.a01_jsonrest_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException

/*
    Aplicacion Android basado en Kotlin que obtiene los repositorios de usuario de GitHub
 */
class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://api.github.com/users/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Obteniendo datos
        btn_obtenerRepos.setOnClickListener {
            val nombreRepo = et_usuarioGitHub.text.toString().trim()
            obtenerListaRepos(nombreRepo)
        }
    }

    private fun crearAdaptador(lista: ArrayList<String>){
        val adaptador = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            lista)
        lv_listaRepos.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }

    private fun obtenerListaRepos(usuario: String) {
        val cola = Volley.newRequestQueue(this)
        val listaRepos = arrayListOf<String>()
        val urlFinal = "$baseUrl$usuario/repos"
        Log.i("volley","URL: $urlFinal")

        val arrayRequest = JsonArrayRequest(
            Request.Method.GET, urlFinal,null,
            Response.Listener<JSONArray> { response ->
                if(response.length() > 0){
                    Log.i("volley","Repositorios: ${response.length()}")
                    for (i in 0 until response.length()) {
                        try {
                            val objetoJSON = response.getJSONObject(i)
                            //Log.i("volley",objetoJSON.toString())
                            val nombreRepo = objetoJSON.getString("name").toString()
                            Log.i("volley","Nonbre: $nombreRepo")
                            val lenguaje = objetoJSON.getString("language").toString()
                            listaRepos.add("$i - Repo: $nombreRepo - \nLenguaje Principal: $lenguaje")
                        } catch (e: JSONException) {
                            Log.e("Volley", "Objeto JSON no válido.")
                        }
                    }
                    crearAdaptador(listaRepos)

                }else{
                    Log.i("volley","Sin repos")
                    Toast.makeText(this, "No hay repositorios", Toast.LENGTH_LONG).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error!:\n$error", Toast.LENGTH_LONG).show()
            })
        cola.add(arrayRequest)
    }

}

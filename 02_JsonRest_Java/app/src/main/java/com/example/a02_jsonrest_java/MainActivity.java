package com.example.a02_jsonrest_java;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
    Aplicacion Android basado en Java que obtiene los repositorios de usuario de GitHub
 */

public class MainActivity extends AppCompatActivity {

    String urlBase = "https://api.github.com/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnObtener = findViewById(R.id.btn_obtenerRepos);
        final TextView txvData = findViewById(R.id.et_usuarioGitHub);
        btnObtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioRepo = txvData.getText().toString().trim();
                obtenerListaRepos(usuarioRepo);
            }
        });
    }

    private void crearAdaptador(ArrayList<String> lista){
        ArrayAdapter adaptador= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        ListView listView = findViewById(R.id.lv_listaRepos);
        listView.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
    }

    private void obtenerListaRepos(String usuario){
        RequestQueue cola = Volley.newRequestQueue(this);
        final ArrayList<String> listaRepos = new ArrayList<>();
        String url = urlBase +usuario+"/repos";
        Log.i("volley","URL: "+url);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject objetoJSON = response.getJSONObject(i);
                                    String nombreRepo = objetoJSON.get("name").toString();
                                    String lenguaje = objetoJSON.get("language").toString();
                                    Log.i("volley", "Data: " + nombreRepo);
                                    listaRepos.add(i + " - Repo: " + nombreRepo + "\nLenguaje Principal: " + lenguaje);
                                } catch (JSONException e) {
                                    Log.e("Volley", "Objeto JSON no válido.");
                                }
                            }
                            crearAdaptador(listaRepos);
                        } else {
                            Log.i("volley","Sin repos");
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "No tiene repos",
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Error: " + error,
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
        cola.add(arrayRequest);
    }

}

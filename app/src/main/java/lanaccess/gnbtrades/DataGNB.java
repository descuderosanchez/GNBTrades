package lanaccess.gnbtrades;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by David on 23/8/15.

 */
public class DataGNB {
    //URL DE DONDE NOS PROVIENE LA INFORMACIÓN.
    public static String URL_CONVERSIONS = "http://quiet-stone-2094.herokuapp.com/rates";
    public static String URL_TRANSACTIONS = "http://quiet-stone-2094.herokuapp.com/transactions";

    //ArrayList con información obtenida.
    private static ArrayList<ConversionDivisas> conversiones = new ArrayList<ConversionDivisas>();
    private static ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();

    //Adapter que vamos a utilizar para actualizar la información.
    private AdapterListProducts adapter;

    private Activity context;

    //Constructor
    public DataGNB(Activity context){
        this.context = context;
        //Creamos el nuevo adapter vinculado a nuestra actividad.
        adapter = new AdapterListProducts(context, transacciones);
    }

    /**
     * Metodo que inicia la descarga de los datos.
     */
    public void iniciarDescarga(){
        tareaDeDescarga(URL_CONVERSIONS);
        tareaDeDescarga(URL_TRANSACTIONS);
    }

    //GETTERS...
    public static ArrayList<ConversionDivisas> getConversiones() {
        return conversiones;
    }

    public static ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }

    public AdapterListProducts getAdapter() {
        return adapter;
    }
    //...FIN GETTERS


    /**
     * Dada una URL de un WebService descarga sus datos y los pasa a StringBuilder
     * @param myurl Url con webservice
     * @return StringBuilder con datos
     * @throws IOException
     */
    private StringBuilder descargarDatos(String myurl) throws IOException {
        InputStream is = null;
        StringBuilder builder;

        try {
            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Comenzamos la descarga.
            connection.connect();
            int response = connection.getResponseCode();
            Log.d("GBD", "Codigo de respuesta: " + response);
            is = connection.getInputStream();

            //Conversión de datos a StringBuilder
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder;
            //Cerramos conexión.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Metodo encargado de llamar a la descarga de las transacciones.
     */
    private void tareaDeDescarga(final String url) {

        AsyncTask<Void, Void, StringBuilder> task = new AsyncTask<Void, Void, StringBuilder>() {

            @Override
            protected StringBuilder doInBackground(Void... params) {
                try {
                    return descargarDatos(url);
                } catch (IOException e) {
                    Log.d("GBD","Error al recibir los datos de los productos");
                }
                return null;
            }

            @Override
            protected void onPostExecute(StringBuilder builder) {
                try {
                    if(builder!=null) {
                        //Comprobamos si vamos a parsear transacciones o conversiones.
                        if (url.equals(URL_TRANSACTIONS)) {
                            //Parseamos transacciones.
                            ArrayList<Transaccion> arrayAux = new ArrayList<Transaccion>();
                            JSONArray jsonArray = new JSONArray(builder.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                transacciones.add(new Transaccion(jsonObject.getString("sku"), jsonObject.getDouble("amount"),
                                        jsonObject.getString("currency")));
                                adapter.notifyDataSetChanged();
                            }
                            adapter.setNotifyOnChange(true);
                        } else {
                            //Parseamos Conversiones.
                            JSONArray jsonArray = new JSONArray(builder.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                conversiones.add(new ConversionDivisas(jsonObject
                                        .getString("from"), jsonObject.getString("to"),
                                        jsonObject.getDouble("rate")));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute((Void[]) null);
    }

}

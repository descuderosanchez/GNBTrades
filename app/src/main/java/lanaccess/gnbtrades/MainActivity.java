package lanaccess.gnbtrades;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public ListView listView;

    public DataGNB dataGNB = null;

    public Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaración de View's...
        listView = (ListView) findViewById(R.id.listView);
        //...Fin declación de View's

        //Inicializamos los datos para visualizarlos.
        inicializarDatos();
    }

    /**
     * Comprueba que hay red en el dispositivo.
     * @return [TRUE si hay Red.]
     *         [FALSE si no hay Red.]
     */
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }else{
            return false;
        }
    }

    /**
     * Si el dispositivo tiene red descarga los datos de las URL de los webservice y muestra los
     * datos.
     */
    public void inicializarDatos(){
        if(isConnected()){
            dataGNB = new DataGNB(MainActivity.this);
            listView.setAdapter(dataGNB.getAdapter());
            dataGNB.iniciarDescarga();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListDetailActivity.class);
                intent.putExtra("sku", dataGNB.getSkuOfPosition(position));
                startActivity(intent);
            }
            });
            dataGNB.getAdapter().notifyDataSetChanged();
        }else{
            showToast(this.getText(R.string.network_error).toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Inicializamos variable 'menu' para poderlo modificar dinamicamente.
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(dataGNB.existSku(query)){
                    return false;
                }
                showToast(query + " " + MainActivity.this.getText(R.string.not_found).toString());
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Item para actualizar los datos y descargarlos de nuevo.
        if (id == R.id.action_settings) {
            inicializarDatos();
            updateMenuTittles();
            return true;
        }

        /*Item para cambiar el estado de la vista a los SKU Unicos.
         *Así es más sencillo buscar un SKU especifico entre todas las transacciones.
         */
        if (id == R.id.mostrar_sku) {
            dataGNB.changeViewStatus();
            listView.setAdapter(dataGNB.getAdapter());
            updateMenuTittles();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Si estamos viendo solo los SKU's cambiamos el titulo a 'Mostrar _todo'.
     * Si estamos viendo la lista completa cambiamos a 'Mostrar SKUs'
     */
    private void updateMenuTittles() {
        MenuItem skuUnicos = menu.findItem(R.id.mostrar_sku);
        if (dataGNB.isSkuUnicos()) {
            skuUnicos.setTitle(getString(R.string.todos_las_transacciones));
        } else {
            skuUnicos.setTitle(getString(R.string.solo_sku));
        }
    }

    /**
     * Función que muestra un Toast con un texto dado.
     * @param s Texto a mostrar en el Toast.
     */
    private void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        System.exit(0);
    }





}
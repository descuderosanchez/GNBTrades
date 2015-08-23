package lanaccess.gnbtrades;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public ListView listView;

    public DataGNB dataGNB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaración de View's...
        listView = (ListView) findViewById(R.id.listView);
        //...Fin declación de View's

        //Chequeamos que haya red en el dispositivo.
        inicializarDatos();

    }

    /**
     * Comprueba en el dispositivo que hay red disponible.
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
     * Si el dispositivo tiene red descarga los datos de las URL de los webservice.
     */
    public void inicializarDatos(){
        if(isConnected()){
        dataGNB = new DataGNB(MainActivity.this);
        dataGNB.iniciarDescarga();
        listView.setAdapter(dataGNB.getAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListDetailActivity.class);
                intent.putExtra("sku", dataGNB.getTransacciones().get(position).getSkuTittle());
                startActivity(intent);
            }
        });
        }else{
            Toast.makeText(this, this.getText(R.string.network_error), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            inicializarDatos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
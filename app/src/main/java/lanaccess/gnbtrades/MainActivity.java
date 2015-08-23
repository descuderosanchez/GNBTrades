package lanaccess.gnbtrades;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String URL_CONVERSIONS = "http://quiet-stone-2094.herokuapp.com/rates";
    public static String URL_TRANSACTIONS = "http://quiet-stone-2094.herokuapp.com/transactions";

    public static ArrayList<ConversionDivisas> conversiones = new ArrayList<ConversionDivisas>();
    public static ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();

    public ListView listView;

    public DataGNB dataGNB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Chequeamos que haya red en el dispositivo.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //cargarConversionDivisas();
            //cargarTransacciones();
            //adapter = new AdapterListProducts(MainActivity.this, transacciones);
            dataGNB = new DataGNB(MainActivity.this);
            dataGNB.iniciarDescarga();
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(dataGNB.getAdapter());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, ListDetailActivity.class);
                    intent.putExtra("sku", dataGNB.getTransacciones().get(position).getSkuTittle());
                    //intent.putExtra("dataGNB", dataGNB);
                    startActivity(intent);
                }
            });
        } else {
            // display error
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
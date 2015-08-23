package lanaccess.gnbtrades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by David on 23/8/15.
 * Activity que muestra la lista de productos de un SKU determinado.
 */
public class ListDetailActivity extends AppCompatActivity implements ListDetailProduct.TotalEurChangedListener {

    private ListView listView;

    private TextView textViewTotalSuma;

    private ListDetailProduct listDetailProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);

        //Recibimos los datos del SKU que vamos a mostrar.
        String sku = getIntent().getStringExtra("sku");

        //Declaración de View's...
        textViewTotalSuma = (TextView) findViewById(R.id.amount);
        listView = (ListView) findViewById(R.id.listView);
        //...fin declaración de View's

        textViewTotalSuma.setText("Cargando...");
        listDetailProduct = new ListDetailProduct(this, DataGNB.getConversiones(), DataGNB.getTransacciones(), sku);
        listView.setAdapter(listDetailProduct.getAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_de_producto_seleccionado, menu);
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

    @Override
    public void valueChanged(final String value) {
        runOnUiThread(new Runnable() {
            public void run() {
                textViewTotalSuma.setText(value);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        listDetailProduct.cancelObtenerDatos();
        super.onBackPressed();
    }
}

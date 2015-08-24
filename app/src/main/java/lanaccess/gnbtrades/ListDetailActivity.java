package lanaccess.gnbtrades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        //Ponemos flecha hacia atras en ActionBar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Declaración de View's...
        textViewTotalSuma = (TextView) findViewById(R.id.amount);
        listView = (ListView) findViewById(R.id.listView);
        //...fin declaración de View's

        textViewTotalSuma.setText("Cargando...");
        listDetailProduct = new ListDetailProduct(this, DataGNB.getConversiones(), DataGNB.getTransacciones(), sku);
        listView.setAdapter(listDetailProduct.getAdapter());

        //Mostramos un Toast indicando el valor en EUR del item seleccionado.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListDetailActivity.this, ListDetailActivity.this.getText(R.string.total) + " " + listDetailProduct.getTotalEurOfItem(position).toString()+ " EUR", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
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
        Log.d("GNB", "onBackPressed Called");
        listDetailProduct.cancelObtenerDatos();
        super.onBackPressed();
    }

}

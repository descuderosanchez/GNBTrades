package lanaccess.gnbtrades;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 23/8/15.
 * Adaptador para los listview.
 * Para dos ROW's de diseño distinto:
 * -[ SKU ][ TITULO SKU ][ AMOUNT ][ CURRENCY ]
 *
 *                      y
 *
 * -[               TITULO SKU                ]
 */
public class AdapterListProducts  extends ArrayAdapter<Transaccion> {
    private Activity context;
    private ArrayList<Transaccion> data= new ArrayList<Transaccion>();
    private boolean listaDeSkuUnicos=false;

    //Contructores...
    /**
     * Contructor por defecto, crea un Adapter que muestra todas las transacciones.
     * @param context [Activity que lo crea.]
     * @param data [Array con las transacciones.]
     */
    public AdapterListProducts(Activity context, ArrayList<Transaccion> data) {
        super(context, R.layout.item_lista_productos, data);
        this.context = context;
        this.data = data;
        listaDeSkuUnicos=false;
    }

    /**
     * Contructor que muestra solo los SKU's disponibles y así poder mostrar mas resumidos los productos
     * y ver una vista detallada pulsando sobre él.
     * @param context [Activity que lo crea.]
     * @param data [Array con las transacciones.]
     */
    public AdapterListProducts(Activity context, ArrayList<Transaccion> data, boolean listaDeSkuUnicos) {
        super(context, R.layout.item_lista_productos, data);
        this.context = context;
        this.data = data;
        this.listaDeSkuUnicos = listaDeSkuUnicos;
    }
    //...Fin Constructores.


    static class ViewHolder {
        public TextView amount;
        public TextView currency;
        public TextView sku;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // Reuso de Views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            ViewHolder viewHolder = new ViewHolder();
            if(listaDeSkuUnicos){
                rowView = inflater.inflate(R.layout.item_lista_productos_sku, null);
                // Configuramos ViewHolder para mostrar los SKU's disponibles.
                viewHolder.sku = (TextView) rowView.findViewById(R.id.tittle_sku);
            }else {
                rowView = inflater.inflate(R.layout.item_lista_productos, null);
                // Configuramos ViewHolder para mostrar todos los datos.
                viewHolder.amount = (TextView) rowView.findViewById(R.id.amount);
                viewHolder.currency = (TextView) rowView.findViewById(R.id.currency);
                viewHolder.sku = (TextView) rowView.findViewById(R.id.tittle_sku);
            }
            rowView.setTag(viewHolder);
        }

        // Datos del item
        if(listaDeSkuUnicos) {
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.sku.setText(data.get(position).getSkuTittle());
            return rowView;
        }else{
            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.sku.setText(data.get(position).getSkuTittle());
            holder.amount.setText(data.get(position).getAmount().toString());
            holder.currency.setText(data.get(position).getCurrency());
            return rowView;
        }
    }
}

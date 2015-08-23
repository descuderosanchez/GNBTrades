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
 *
 */
public class AdapterListProducts  extends ArrayAdapter<Transaccion> {
    private Activity context;
    private ArrayList<Transaccion> data= new ArrayList<Transaccion>();

    static class ViewHolder {
        public TextView amount;
        public TextView currency;
        public TextView sku;
    }

    public AdapterListProducts(Activity context, ArrayList<Transaccion> data) {
        super(context, R.layout.item_lista_productos, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // Reuso de Views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_lista_productos, null);
            // Configuramos ViewHolder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.amount = (TextView) rowView.findViewById(R.id.amount);
            viewHolder.currency = (TextView) rowView.findViewById(R.id.currency);
            viewHolder.sku = (TextView) rowView.findViewById(R.id.tittle_sku);
            rowView.setTag(viewHolder);
        }

        // Datos del item
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.sku.setText(data.get(position).getSkuTittle());
        holder.amount.setText(data.get(position).getAmount().toString());
        holder.currency.setText(data.get(position).getCurrency());
        return rowView;
    }
}

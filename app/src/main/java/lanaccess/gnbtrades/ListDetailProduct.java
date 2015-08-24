package lanaccess.gnbtrades;

import android.app.Activity;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by David on 23/8/15.
 * Lista de productos que pertenecen al mismo SKU y total en EUR redondeado en BigDecimal
 */
public class ListDetailProduct {

    //Variable que utilizo para refrescar el listView de 30 EN 30 sin colapsar la memoria.
    private int COUNT_REFRESH_ITEM = 20;

    //Lista de productos que tienen el mismo SKU.
    private ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();
    //Auxiliar de transacciones.
    private ArrayList<Transaccion> transaccionesaux = new ArrayList<Transaccion>();
    //Todas las conversiones.
    private ArrayList<ConversionDivisas> conversiones = new ArrayList<ConversionDivisas>();
    //Adapter que vamos a utilizar para actualizar la información.
    private AdapterListProducts adapter;
    //Total en euros.
    private BigDecimal totalEur;
    //Total en double
    private Double sumaTotal;
    //Listener para cambiar views en activity.
    private TotalEurChangedListener totalEurChangedListener;
    //Activity Context that create this class.
    private Activity context;
    //Asynctask de recopilar los productos de un SKU especifico y calcular el total ganado.
    private AsyncTask obtenerDatos;


    public ListDetailProduct(Activity context, ArrayList<ConversionDivisas> conversiones,
                             final ArrayList<Transaccion> todasTransacciones, final String sku){
        this.context = context;
        this.conversiones = conversiones;
        sumaTotal=0.0;
        adapter = new AdapterListProducts(context, transacciones);
        totalEurChangedListener = (TotalEurChangedListener) context;
        AsyncTask obtenerDatos = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                int refreshItems = COUNT_REFRESH_ITEM;
                for (Transaccion t : todasTransacciones) {
                    if (t.getSkuTittle().equals(sku)) {
                        transacciones.add(t);
                        sumaTotal += sumaDivisas(t);
                        totalEur = new BigDecimal(sumaTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                }
                totalEurChangedListener.valueChanged(totalEur.toString());
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();

            }
        }.execute();

    }

    //GETTERS...
    public BigDecimal getTotalEurOfItem(int position) {
        return new BigDecimal(sumaDivisas(transacciones.get(position))).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public AdapterListProducts getAdapter() {
        return adapter;
    }

    public ArrayList<Transaccion> getTransacciones() {
        return transacciones;
    }
    //...FIN GETTERS

    private Double sumaDivisas(Transaccion t){
        if(t.getCurrency().equals("EUR")){
            return t.getAmount();
        }else{
            ArrayList<ConversionDivisas> listaDeDivisasASumar = new ArrayList<ConversionDivisas>();
            Double sumProduct=t.getAmount();
            listaDeDivisasASumar = buscaConversiones(listaDeDivisasASumar, t.getCurrency());
            for(ConversionDivisas c : listaDeDivisasASumar){
                sumProduct *= c.getConversionRate();
            }
            return sumProduct;
        }
    }

    public interface TotalEurChangedListener{
        public void valueChanged(String value);
    }

    /**
     * [buscaConversiones Este Script recorre el listado de conversiones disponibles para buscar la
     * combinación exacta que convierte a EURO para la moneda inicial dada.]
     * @param  listadoDivisasPrevio [Es el listado que almacena las conversiones por las que hay que
     * 								[ multiplicar para llegar a EURO.]
     * @param  MonedaInicial        [Moneda inicial que buscamos para el listado de conversiones.]
     * @return                      [Null si no ha encontrado la conversión que haga llegar a EURO]
     *                              [Listado de Conversiones por las que hay que multiplicar para
     *                              [transformar la moneda del producto a Euro.]
     */
    private ArrayList<ConversionDivisas> buscaConversiones(ArrayList<ConversionDivisas> listadoDivisasPrevio,
                                                           String MonedaInicial){
        //Array donde metemos las divisas por donde hemos de buscar el camino para la conversión a EUR.
        ArrayList<ConversionDivisas> listaDivisasABuscar = new ArrayList<ConversionDivisas>();

        //Buscamos la conversión cuyo FROM sea igual a MonedaInicial que damos en la función y el TO sea el EURO.
        for(ConversionDivisas divisa : conversiones){
            if(divisa.getCurrencyFROM().equals(MonedaInicial)){
                //Conversiones guardadas cuyo FROM es igual a MonedaInicial para sseguir después la búsqueda.
                listaDivisasABuscar.add(divisa);
                if(divisa.getCurrencyTO().equals("EUR")){
                    listadoDivisasPrevio.add(divisa);
                    //Encontrada conversión final
                    return listadoDivisasPrevio;
                }
            }
        }
        if(listaDivisasABuscar.size()!=0){
            ArrayList<ConversionDivisas> listadoConversionesFinales = new ArrayList<ConversionDivisas>();
            listadoConversionesFinales = (ArrayList<ConversionDivisas>) listadoDivisasPrevio.clone();
            for(ConversionDivisas divisa : listaDivisasABuscar){
                listadoConversionesFinales.add(divisa);
                listadoConversionesFinales = buscaConversiones(listadoConversionesFinales, divisa.getCurrencyTO());
                if(listadoConversionesFinales!=null){
                    return listadoConversionesFinales;
                }else{
                    //Al devolver NULL nos indica que por ese camino no puede llegar a EUR.
                    //Eliminamos la ultima conversión agregada y seguimos buscando en otras conversiones guardadas.
                    listadoConversionesFinales.remove(listadoConversionesFinales.size()-1);
                }
            }
        }
        return null;
    }

    public void cancelObtenerDatos(){
        if(obtenerDatos!=null) {
            obtenerDatos.cancel(true);
        }
    }

}

package lanaccess.gnbtrades;

/**
 * Created by David on 23/8/15.
 * Objeto que contiene el tipo de monedas de cambio y el valor del cambio.
 */
public class ConversionDivisas {

    public String currencyFROM;
    public String currencyTO;
    public Double conversionRate;


    ConversionDivisas(String currencyFROM, String currencyTO, Double conversionRate){
        this.currencyFROM = currencyFROM;
        this.currencyTO = currencyTO;
        this.conversionRate = conversionRate;
    }

    //GETTERS Y SETTERS

    public String getCurrencyFROM() {
        return currencyFROM;
    }

    public String getCurrencyTO() {
        return currencyTO;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setCurrencyFROM(String currencyFROM) {
        this.currencyFROM = currencyFROM;
    }

    public void setCurrencyTO(String currencyTO) {
        this.currencyTO = currencyTO;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    //FIN GETTERS Y SETTERS

}

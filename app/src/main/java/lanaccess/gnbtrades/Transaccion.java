package lanaccess.gnbtrades;

/**
 * Created by David on 23/8/15.
 * Objeto que contiene la información de una transacción.
 */
public class Transaccion {

    private String skuTittle;
    private Double amount;
    private String currency;

    public Transaccion(String skuTittle, Double amount, String currency) {
        this.skuTittle = skuTittle;
        this.amount = amount;
        this.currency = currency;
    }

    //GETTER Y SETTERS...

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSkuTittle() {
        return skuTittle;
    }

    public void setSkuTittle(String skuTittle) {
        this.skuTittle = skuTittle;
    }

    //...FIN GETTERS Y SETTERS

}

package org.conversion.AppleStores;

import org.conversion.NepExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainStore {
    public static final String IPHONE_16_PRO_256 = "IPHONE_16_PRO_256";
    public static final String IPHONE_16_PRO_MAX_256 = "IPHONE_16_PRO_MAX_256";
    public static final String AIRPODS_PRO_3RD_GEN = "Airpods Pro 3rd Gen";

    protected String name;
    protected double tax_rate;
    protected String code;
    protected String currency;
    protected NepExchange nepExchange;
    protected Map<String, Double> items;

    public MainStore(String name, double tax_rate, String currency, Map<String, Double> items) {
        this.name = name;
        this.tax_rate = tax_rate;
        this.currency = currency;
        this.nepExchange = new NepExchange();
        this.items = items;
    }

    public Double getPrice(String product, String targetCurrency) throws IOException {
        targetCurrency = targetCurrency.toUpperCase();
        if (!items.containsKey(product)) {
            return null;
        }
        double priceBeforeTax = items.get(product);
        double priceAfterTax = priceBeforeTax * (1 + tax_rate / 100);

        if (targetCurrency == currency) {
            return Math.round(priceAfterTax * 100.0) / 100.0;
        } else {
            double convertedPrice = nepExchange.convert(priceAfterTax, currency, targetCurrency);
            return Math.round(convertedPrice * 100.0) / 100.0;
        }

    }

    @Override
    public String toString() {
        return name;
    }
}

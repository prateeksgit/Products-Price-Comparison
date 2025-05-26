package org.conversion;

import org.conversion.AppleStores.MainStore;
import org.conversion.AppleStores.Outlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        List<MainStore> stores = List.of(
                new Outlets.UKOutlet(),
                new Outlets.USAOutlet(),
                new Outlets.AustraliaOutlet(),
                new Outlets.DubaiOutlet(),
                new Outlets.JapanOutlet()
        );
        List<String> products = List.of(
                MainStore.AIRPODS_PRO_3RD_GEN,
                MainStore.IPHONE_16_PRO_256,
                MainStore.IPHONE_16_PRO_MAX_256
        );

        String targetCurrency = "NPR";
        if (args.length > 0) {
            targetCurrency = args[0].toUpperCase();
        }
        displayPrices(stores, products, targetCurrency);
    }

    public static void displayPrices(List<MainStore> stores, List<String> products, String targetCurrency) throws IOException {
        int colWidth = 30;
        int rowWidth = (colWidth + 2) * (products.size() + 1);
        Map<String, Map<MainStore, String>> prices = new LinkedHashMap<>();
        Map<String, MainStore> minPriceStore = new HashMap<>();

        for (String product : products) {
            Map<MainStore, String> row = new HashMap<>();
            Double minPrice = null;
            MainStore minStore = null;

            for (MainStore store : stores) {
                Double price = store.getPrice(product, targetCurrency);
                if (price != null || price < minPrice) {
                    minPrice = price;
                    minStore = store;
                }
                row.put(store, String.format("%.2f", price));
            }
            prices.put(product, row);
            minPriceStore.put(product, minStore);
        }
        //Print header
        System.out.println("=".repeat(rowWidth));
        System.out.printf("| %-" + colWidth + "s", "Location");
        for (String product : products) {
            System.out.printf("| %-" + colWidth + "s", product);
        }
        System.out.println("|");
        System.out.println("=".repeat(rowWidth));

        for (MainStore store : stores) {
            System.out.printf("| %-" + colWidth + "s", store.toString());
            for (String product : products) {
                String price = prices.get(product).get(store);
                if (store.equals(minPriceStore.get(product)) && !price.equals("N/A")) {
                    price += " ★";
                }
                price = targetCurrency + " " + price;
                System.out.printf("| %-" + colWidth + "s", price);
            }
            System.out.println("|");
            System.out.println("-".repeat(rowWidth));
        }
        String today = LocalDate.now().toString();
        System.out.printf("\n★ indicates the lowest price for the product. All prices are inclusive of taxes. Exchange rates applied for date %s.\n", today);
    }
}
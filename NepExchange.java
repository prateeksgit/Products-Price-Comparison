package org.conversion;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.StandardOpenOption;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Scanner;

public class NepExchange {
    private static final String API_URL="https://api.apilayer.com/exchangerates_data/latest";
    private static final String API_KEY="AtFnr2vdqM7rLU4EpmSEiM8leLFZPVh4";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final Path dataDirectory;

    public NepExchange(){
        dataDirectory=Paths.get(System.getProperty("user.dir"),"forex_data");
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }
    //Look inside the current working directory (user.dir)
    //Then into a subfolder called "forex_data"

    public Map<String,Object> getRatesForBaseCurrency(String base_currency, boolean refresh) throws IOException {
        base_currency = base_currency.toUpperCase();
        Path dataFilePath = dataDirectory.resolve(base_currency + ".json");
        //.resolve appends the provided string to the path.
        //dataDirectory.resolve("USD.json")-->/data/currencies/USD.json

        if (!Files.exists(dataFilePath)) {
            refresh = true;
        }
        // Ensuring directory exists
        //Paths.get(System.getProperty("user.dir"),"forex_data"); this will only point toward the forex_data folder and not create it
        //so we need to create one. if its already there code skips this part.

        if(!refresh){
            byte[] jsonData=Files.readAllBytes(dataFilePath);
            //storing the data in raw bytes as binary numbers.
            JsonNode root= mapper.readTree(jsonData);
            long timeStamp=root.get("timestamp").asLong();

            LocalDate pulledDate = Instant.ofEpochSecond(timeStamp).atZone(ZoneId.systemDefault()).toLocalDate();
//            System.out.println("Pulled date: "+pulledDate);
            LocalDate today = LocalDate.now();
//            System.out.println("LocalDate today: "+today);

            if(pulledDate.equals(today)){
                //Returning as MAP
                return mapper.convertValue(root, new TypeReference<Map<String, Object>>() {});
            }
        }
        //if data needs to be refreshed.
        URL url=new URL(API_URL+"?base"+base_currency);
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setRequestProperty("apikey",API_KEY);
        connection.setRequestMethod("GET");

        if(connection.getResponseCode()!=200){
            throw new IOException("Failed to fetch the exchange rates: Status: "+ connection.getResponseCode());
        }

        Scanner scanner=new Scanner(connection.getInputStream());
        System.out.println(connection.getInputStream());
        StringBuilder jsonBuilder=new StringBuilder();
        while (scanner.hasNext()){
            jsonBuilder.append(scanner.nextLine());
        }
        scanner.close();
//        System.out.println("Data from String BUilder JSON builder: "+jsonBuilder);
        String jsonData=jsonBuilder.toString();
//        System.out.println("Data after being converted toString: "+jsonData);

        // Parse and cache
        JsonNode root=mapper.readTree(jsonData);
        Files.writeString(dataFilePath,jsonData,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);

        //Return as MAP

        return mapper.convertValue(root, new TypeReference<Map<String, Object>>() {});
    }

    public double convert(double amount,String from,String to) throws IOException{
        from=from.toUpperCase();
        to=to.toUpperCase();
        Map<String,Object> data= getRatesForBaseCurrency(from,false);
        Map<String,Double> rates=(Map<String, Double>) data.get("rates");

//        System.out.println("Rates of the base currency is : "+rates);
        if (!rates.containsKey(to)) {
            throw new IllegalArgumentException("Invalid currency code: " + to);
        }
        return amount*rates.get(to);
    }
}

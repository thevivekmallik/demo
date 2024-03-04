package com.techplement.currencyconverter;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ExchangeRateApiClient {

	private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/99ba35bac0162ba7739dcb46/latest/USD";
	//private static final String apiKey = "99ba35bac0162ba7739dcb46";
    public static String getExchangeRates() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL ))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static void main(String[] args) {
        try {
            String exchangeRates = getExchangeRates();
            System.out.println("Exchange Rates:\n" + exchangeRates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

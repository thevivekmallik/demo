package com.techplement.currencyconverter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CurrencyConverter {

    private final Scanner scanner = new Scanner(System.in);
    private Map<String, Double> exchangeRates;
    private Set<String> favoriteCurrencies;
    //private Set<String> favoriteCurrencies = new HashSet<>();


    public CurrencyConverter() {
        this.exchangeRates = new HashMap<>();
        this.favoriteCurrencies = new HashSet<>();
        // You may initialize exchange rates or fetch them from the API upon object creation.
    }

    public void setExchangeRates(Map<String, Double> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        System.out.println("fromCurrency: " + fromCurrency);
        System.out.println("toCurrency: " + toCurrency);

        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Invalid currency codes");
        }

        double fromRate = exchangeRates.get(fromCurrency);
        double toRate = exchangeRates.get(toCurrency);

        // Perform the currency conversion
        return amount * (toRate / fromRate);
    }

    public boolean isValidCurrencyCode(String fromCurrency) {
        return fromCurrency != null && fromCurrency.matches("[A-Z]{3}");
    }
	
//    public boolean isValidCurrencyCode(String currencyCode) {
//        return currencyCode != null && exchangeRates.containsKey(currencyCode);
//    }

    
    public boolean addFavoriteCurrency(String currencyCode) {
        if (isValidCurrencyCode(currencyCode)) {
            return favoriteCurrencies.add(currencyCode);
        }
        return false;
    }




    public Set<String> getFavoriteCurrencies() {
        return favoriteCurrencies;
    }

    
    public void deleteFavoriteCurrency(String currencyCode) {
        if (favoriteCurrencies.contains(currencyCode)) {
            favoriteCurrencies.remove(currencyCode);
        } else {
            throw new IllegalArgumentException("Invalid currency code. The currency is not in your favorite currencies.");
        }
    }


}

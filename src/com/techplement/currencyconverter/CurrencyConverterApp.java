package com.techplement.currencyconverter;

import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyConverterApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CurrencyConverter currencyConverter = new CurrencyConverter();
	private static Map<String, Double> favoriteCurrencies;

    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Currency Converter!");

        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    showExchangeRates();
                    break;
                case 2:
                    addFavoriteCurrency();
                    break;
                case 3:
                    showFavoriteCurrency();
                    break;
                case 4:
                	deleteFavoriteCurrency();// Add logic for updating favorite currencies
                    break;
                case 0:
                    System.out.println("Thank you for using the Currency Converter. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMENU:");
        System.out.println("1. Show Exchange Rates");
        System.out.println("2. Add Favorite Currency");
        System.out.println("3. Show Favorite Currency");
        System.out.println("4. Delete Favorite Currency");
        System.out.println("0. Exit");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }

    private static void showExchangeRates() {
        try {
            String exchangeRatesData = ExchangeRateApiClient.getExchangeRates();
            currencyConverter.setExchangeRates(parseExchangeRates(exchangeRatesData));

            // Get user input for conversion
            System.out.print("Enter the amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter the source currency code: ");
            String fromCurrency = scanner.next().toUpperCase();
            System.out.print("Enter the target currency code: ");
            String toCurrency = scanner.next().toUpperCase();

            // Perform the conversion
            double convertedAmount = currencyConverter.convertCurrency(amount, fromCurrency, toCurrency);

            // Display the result
            System.out.println(String.format("%.2f %s is equal to %.2f %s", amount, fromCurrency, convertedAmount, toCurrency));
        } catch (Exception e) {
            System.out.println("Error fetching exchange rates or performing conversion. Please try again later.");
        }
    }

    private static Map<String, Double> parseExchangeRates(String exchangeRatesData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the JSON data into a JsonNode
            JsonNode rootNode = objectMapper.readTree(exchangeRatesData);
            JsonNode conversionRatesNode = rootNode.get("conversion_rates");

            // Convert the rates into a map
            Map<String, Double> exchangeRates = new HashMap<>();
            conversionRatesNode.fields().forEachRemaining(entry -> {
                exchangeRates.put(entry.getKey(), entry.getValue().asDouble());
            });
            currencyConverter.setExchangeRates(exchangeRates);
            return exchangeRates;
        } catch (IOException e) {
            System.out.println("Error parsing exchange rates data: " + e.getMessage());
            return new HashMap<>(); // Return an empty map in case of an error
        }
    }

    private static void addFavoriteCurrency() {
        boolean isValidCurrencyCode = false;

        while (!isValidCurrencyCode) {
            System.out.print("Enter the currency code to add to favorites: ");
            String currencyCode = scanner.next().toUpperCase();

            if (currencyConverter.addFavoriteCurrency(currencyCode)) {
                System.out.println(currencyCode + " has been added to your favorite currencies.");
                isValidCurrencyCode = true;
            } else {
                System.out.println("Invalid currency code or currency is already in your favorite currencies. Please enter a valid 3-letter currency code.");
            }
        }
    }


    private static void showFavoriteCurrency() {
        Set<String> favoriteCurrencies = currencyConverter.getFavoriteCurrencies();
        if (favoriteCurrencies.isEmpty()) {
            System.out.println("You haven't added any favorite currencies yet.");
        } else {
            System.out.println("Favorite Currencies:");
            for (String currency : favoriteCurrencies) {
                System.out.println(currency);
            }
        }
    }
    
	/*
	 * private static void deleteFavoriteCurrency() {
	 * System.out.print("Enter the currency code to delete: "); String
	 * currencyCodeToDelete = scanner.next().toUpperCase();
	 * 
	 * if (favoriteCurrencies.containsKey(currencyCodeToDelete)) {
	 * favoriteCurrencies.remove(currencyCodeToDelete);
	 * System.out.println(currencyCodeToDelete +
	 * " has been deleted from your favorite currencies."); } else { System.out.
	 * println("Invalid currency code. The currency is not in your favorite currencies."
	 * ); } }
	 */
	/*
	 * public static void deleteFavoriteCurrency(String currencyCode) { if
	 * (isValidCurrencyCode(currencyCode)) { if
	 * (favoriteCurrencies.contains(currencyCode)) {
	 * favoriteCurrencies.remove(currencyCode); System.out.println(currencyCode +
	 * " has been removed from your favorite currencies."); } else {
	 * System.out.println(currencyCode + " is not in your favorite currencies."); }
	 * } else { System.out.
	 * println("Invalid currency code. The currency is not in your favorite currencies."
	 * ); } }
	 */
    private static void deleteFavoriteCurrency() {
        System.out.print("Enter the currency code to delete from favorites: ");
        String currencyCode = scanner.next().toUpperCase();

        try {
            currencyConverter.deleteFavoriteCurrency(currencyCode);
            System.out.println(currencyCode + " has been deleted from your favorite currencies.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

}

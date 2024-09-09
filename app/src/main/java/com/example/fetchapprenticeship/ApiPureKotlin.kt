package com.example.fetchapprenticeship

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URI

fun main() {
    val apiUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    try {
        val url = URI.create(apiUrl).toURL();
        val connection = url.openConnection() as HttpURLConnection;

        connection.requestMethod = "GET";

        val responseCode = connection.responseCode;
        println("Response Code: $responseCode");

        if(responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream));
            var line: String?;
            val response = StringBuilder();

            while(reader.readLine().also { line = it } != null) {
                response.append(line);
            }

            reader.close()
        } else {
            println("Error: $responseCode. Unable to fetch data from the API.");
        }

        connection.disconnect();
    } catch (error: Exception) {
        error.printStackTrace();
    }
}
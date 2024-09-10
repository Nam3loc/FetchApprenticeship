package com.example.fetchapprenticeship

import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URI

class ApiService {
    fun getFetchHiringDataPureKotlin(): List<FetchHiringData> {
        val apiUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

        return try {
            val url = URI.create(apiUrl).toURL()
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            val responseCode = connection.responseCode
            Log.d("ApiService", "Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                val jsonResponse = JSONArray(response.toString())
                val filteredDataList = mutableListOf<FetchHiringData>()

                // Iteration starting at 0 and going the entire length of the JSON
                for(i in 0 until jsonResponse.length()) {
                    val jsonObject = jsonResponse.getJSONObject(i)
                    if(jsonObject.has("listId") && !jsonObject.isNull("name")) {
                        val name = jsonObject.getString("name")
                        if(name.isNotBlank()) {
                            val filteredData = FetchHiringData(
                                id = jsonObject.getInt("id"),
                                name = name
                            )
                            filteredDataList.add(filteredData)
                        }
                    }
                }
                filteredDataList
            } else {
                Log.d("ApiService", "Error: $responseCode. Unable to fetch data from the API.")
                emptyList()
            }
        } catch (error: Exception) {
            error.printStackTrace()
            emptyList()
        }
    }
}

data class FetchHiringData (
    val id: Int,
    val name: String
)


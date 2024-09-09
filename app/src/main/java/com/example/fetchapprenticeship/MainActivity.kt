package com.example.fetchapprenticeship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fetchapprenticeship.ui.theme.FetchApprenticeshipTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FetchApprenticeshipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    apiCallPureKotlin();
                }
            }
        }
    }
}

private fun apiCallPureKotlin() {
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchApprenticeshipTheme {
        Greeting("Android")
    }
}
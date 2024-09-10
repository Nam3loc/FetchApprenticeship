package com.example.fetchapprenticeship

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchapprenticeship.ui.theme.FetchApprenticeshipTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

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
                    GetFetchHiringData()
                }
            }
        }
    }
    @Composable
    fun GetFetchHiringData() {
        // Remember handles states and storing of data
        // Similar to useState in React
        var apiData by remember { mutableStateOf<List<FetchHiringData>>(emptyList()) }
        var isAscending by remember { mutableStateOf(true) }

        // Coroutine to call the API and update the state
        LaunchedEffect(Unit) { // LaunchedEffect similar to BehaviorSubject
            val apiService = ApiService()
            // Dispatchers.IO is a background thread the api is being called on
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data = apiService.getFetchHiringDataPureKotlin()
                    // Dispatchers.Main takes the data and gives it to the main thread to display
                    withContext(Dispatchers.Main) {
                        apiData = data
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        apiData = emptyList()
                    }
                }
            }
        }

        // Sort API Data
        val sortedApiData = apiData.sortedBy { if (isAscending) it.id else -it.id }

        // Full Screen container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF290b2f))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Fetch Logo at the top center
                FetchLogo(
                    fetchedData = "", // No text needed here
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )

                // Button to toggle sorting order
                Button(
                    onClick = { isAscending = !isAscending },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfda71e)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isAscending) "Sort Descending" else "Sort Ascending",
                        color = Color.White
                    )
                }

                // Inner container for the fetched data with white background and rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            PaddingValues(
                                start = 32.dp,
                                end = 32.dp,
                                bottom = 32.dp
                            )
                        )
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    // Scrollable container for the fetched data
                    if (sortedApiData.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            items(sortedApiData) { item ->
                                val fetchedData = "ID: ${item.id}, Name: ${item.name}"
                                FetchHiringDataText(
                                    fetchedData = fetchedData,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        // Placeholder text when data is loading or empty
                        Text(
                            text = "Loading...",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FetchLogo(fetchedData: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.fetch_rewards_logo_no_background)
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Image(
                painter = image,
                contentDescription = "Fetch logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
                    .size(150.dp)
            )
            FetchHiringDataText(
                fetchedData = fetchedData,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun FetchHiringDataText(fetchedData: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
        ) {
            Text(
                text = fetchedData,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        bottom = 8.dp
                    )
            )
        }
    }
}
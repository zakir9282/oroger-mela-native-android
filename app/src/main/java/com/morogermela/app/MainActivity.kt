package com.morogermela.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF176B45)
private val LightGreen = Color(0xFFEAF5EE)
private val Background = Color(0xFFF5F8F5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Dashboard()
            }
        }
    }
}

@Composable
fun Dashboard() {
    var selectedPage by remember { mutableStateOf<String?>(null) }

    if (selectedPage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(20.dp)
        ) {
            Text(
                text = "← ফিরে যান",
                color = Green,
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    selectedPage = null
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = selectedPage!!,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "আপনি এখনো তথ্য যোগ করেননি",
                        color = Color.Gray
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(16.dp)
        ) {
            Text(
                "মোরগের মেলা",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )

            Text(
                "পোল্ট্রি হিসাব ব্যবস্থাপনা",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                "আজকের হিসাব",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryCard("মোট ক্রয়", "৳ ০", Modifier.weight(1f))
                SummaryCard("মোট বিক্রয়", "৳ ০", Modifier.weight(1f))
                SummaryCard("লাভ / ক্ষতি", "৳ ০", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "হিসাব ব্যবস্থাপনা",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            val sections = listOf(
                "ক্রয় হিসাব",
                "বিক্রয় হিসাব",
                "অন্যান্য হিসাব",
                "স্টক",
                "কর্মচারী",
                "রিপোর্ট"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sections) { section ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(105.dp)
                            .clickable {
                                selectedPage = section
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                section,
                                color = Green,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    amount: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(95.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                fontSize = 12.sp,
                color = Green
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                amount,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
        }
    }
}

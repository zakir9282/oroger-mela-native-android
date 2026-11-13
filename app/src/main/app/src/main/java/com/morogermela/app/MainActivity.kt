package com.morogermela.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Green = Color(0xFF176B45)
private val LightGreen = Color(0xFFEAF5EE)
private val Background = Color(0xFFF5F8F5)

private val breeds = mapOf(
    "মুরগি" to listOf("ব্রাউন/কক", "সোনালি", "ব্রয়লার", "লেয়ার", "দেশি"),
    "হাঁস" to listOf("খাকি ক্যাম্পবেল", "ইন্ডিয়ান রানার", "দেশি হাঁস"),
    "কোয়েল" to listOf("দেশি", "জাপানি কোয়েল", "ব্রাউন কোয়েল"),
    "কবুতর" to listOf("গিরিবাজ", "লাহোরি", "দেশি কবুতর")
)

data class PurchaseRecord(
    val id: Long,
    val date: String,
    val bird: String,
    val breed: String,
    val supplier: String,
    val quantity: Double,
    val weight: Double,
    val rate: Double,
    val amount: Double
)

private fun readPurchases(context: Context): List<PurchaseRecord> {
    return try {
        val file = File(context.filesDir, "purchases.json")
        if (!file.exists()) return emptyList()

        val array = JSONArray(file.readText())
        (0 until array.length()).map { i ->
            val item = array.getJSONObject(i)
            PurchaseRecord(
                id = item.getLong("id"),
                date = item.getString("date"),
                bird = item.getString("bird"),
                breed = item.getString("breed"),
                supplier = item.getString("supplier"),
                quantity = item.getDouble("quantity"),
                weight = item.getDouble("weight"),
                rate = item.getDouble("rate"),
                amount = item.getDouble("amount")
            )
        }
    } catch (_: Exception) {
        emptyList()
    }
}

private fun savePurchases(
    context: Context,
    records: List<PurchaseRecord>
) {
    val array = JSONArray()
    records.forEach { record ->
        array.put(
            JSONObject().apply {
                put("id", record.id)
                put("date", record.date)
                put("bird", record.bird)
                put("breed", record.breed)
                put("supplier", record.supplier)
                put("quantity", record.quantity)
                put("weight", record.weight)
                put("rate", record.rate)
                put("amount", record.amount)
            }
        )
    }

    File(context.filesDir, "purchases.json").writeText(array.toString())
}

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

    if (selectedPage == "ক্রয় হিসাব") {
        PurchaseScreen { selectedPage = null }
    } else if (selectedPage != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(20.dp)
        ) {
            Text(
                "← ফিরে যান",
                color = Green,
                modifier = Modifier.clickable { selectedPage = null }
            )
            Spacer(Modifier.height(24.dp))
            Text(
                selectedPage!!,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
            Spacer(Modifier.height(20.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("আপনি এখনো তথ্য যোগ করেননি", color = Color.Gray)
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
            Text("পোল্ট্রি হিসাব ব্যবস্থাপনা", color = Color.Gray)

            Spacer(Modifier.height(22.dp))
            Text("আজকের হিসাব", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SummaryCard("মোট ক্রয়", "৳ ০", Modifier.weight(1f))
                SummaryCard("মোট বিক্রয়", "৳ ০", Modifier.weight(1f))
                SummaryCard("লাভ / ক্ষতি", "৳ ০", Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))
            Text("হিসাব ব্যবস্থাপনা", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            val sections = listOf(
                "ক্রয় হিসাব", "বিক্রয় হিসাব",
                "অন্যান্য হিসাব", "স্টক",
                "কর্মচারী", "রিপোর্ট"
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                sections.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        pair.forEach { section ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(105.dp)
                                    .clickable { selectedPage = section },
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
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val records = remember {
        mutableStateListOf<PurchaseRecord>().apply {
            addAll(readPurchases(context))
        }
    }

    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    var date by remember { mutableStateOf(today) }
    var bird by remember { mutableStateOf("মুরগি") }
    var breed by remember { mutableStateOf(breeds["মুরগি"]!!.first()) }
    var supplier by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<Long?>(null) }
    var message by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    val isWeightBird = bird == "মুরগি" || bird == "হাঁস"
    val amount = (
        (if (isWeightBird) weight.toDoubleOrNull() ?: 0.0
         else quantity.toDoubleOrNull() ?: 0.0) *
        (rate.toDoubleOrNull() ?: 0.0)
    )

    fun clearForm() {
        date = today
        bird = "মুরগি"
        breed = breeds["মুরগি"]!!.first()
        supplier = ""
        quantity = ""
        weight = ""
        rate = ""
        editingId = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "← ফিরে যান",
            color = Green,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(16.dp))
        Text(
            "ক্রয় হিসাব",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("নতুন ক্রয়ের তথ্য", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("তারিখ (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("পাখির ধরন")
                SimpleDropdown(bird, breeds.keys.toList()) {
                    bird = it
                    breed = breeds[it]!!.first()
                }

                Text("জাত নির্বাচন")
                SimpleDropdown(breed, breeds[bird]!!) {
                    breed = it
                }

                OutlinedTextField(
                    value = supplier,
                    onValueChange = { supplier = it },
                    label = { Text("সরবরাহকারীর নাম") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("পরিমাণ (টি)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                if (isWeightBird) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("মোট ওজন (কেজি)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = rate,
                    onValueChange = { rate = it },
                    label = {
                        Text(if (isWeightBird) "প্রতি কেজির দর (৳)" else "প্রতি পিসের দর (৳)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                Text(
                    "মোট টাকা: ৳ ${"%.2f".format(Locale.US, amount)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green
                )

                Button(
                    onClick = {
                        val q = quantity.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()
                        val w = weight.toDoubleOrNull() ?: 0.0

                        if (
                            date.isBlank() || supplier.isBlank() ||
                            q == null || q <= 0 ||
                            r == null || r <= 0 ||
                            (isWeightBird && (w <= 0 || weight.isBlank()))
                        ) {
                            message = "সব তথ্য সঠিকভাবে পূরণ করুন"
                            showMessage = true
                        } else {
                            val record = PurchaseRecord(
                                id = editingId ?: System.currentTimeMillis(),
                                date = date.trim(),
                                bird = bird,
                                breed = breed,
                                supplier = supplier.trim(),
                                quantity = q,
                                weight = w,
                                rate = r,
                                amount = amount
                            )

                            val oldIndex = records.indexOfFirst { it.id == record.id }
                            if (oldIndex >= 0) records[oldIndex] = record
                            else records.add(0, record)

                            savePurchases(context, records)
                            clearForm()
                            message = "ক্রয়ের তথ্য সংরক্ষণ হয়েছে"
                            showMessage = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Green)
                ) {
                    Text(if (editingId == null) "ক্রয়ের তথ্য সংরক্ষণ করুন" else "তথ্য আপডেট করুন")
                }

                if (editingId != null) {
                    OutlinedButton(
                        onClick = { clearForm() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("সম্পাদনা বাতিল")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "ক্রয়ের তালিকা (${records.size})",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))

        if (records.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("আপনি এখনো তথ্য যোগ করেননি", color = Color.Gray)
                }
            }
        } else {
            records.forEach { record ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            "${record.bird} - ${record.breed}",
                            fontWeight = FontWeight.Bold,
                            color = Green
                        )
                        Text("তারিখ: ${record.date}")
                        Text("সরবরাহকারী: ${record.supplier}")
                        Text("পরিমাণ: ${record.quantity} টি")
                        if (record.weight > 0) Text("ওজন: ${record.weight} কেজি")
                        Text("দর: ৳ ${record.rate}")
                        Text(
                            "মোট: ৳ ${"%.2f".format(Locale.US, record.amount)}",
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(
                                onClick = {
                                    editingId = record.id
                                    date = record.date
                                    bird = record.bird
                                    breed = record.breed
                                    supplier = record.supplier
                                    quantity = record.quantity.toString()
                                    weight = if (record.weight > 0) record.weight.toString() else ""
                                    rate = record.rate.toString()
                                }
                            ) {
                                Text("সম্পাদনা", color = Green)
                            }

                            TextButton(
                                onClick = {
                                    records.removeAll { it.id == record.id }
                                    savePurchases(context, records)
                                    if (editingId == record.id) clearForm()
                                }
                            ) {
                                Text("মুছে ফেলুন", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        if (showMessage) {
            AlertDialog(
                onDismissRequest = { showMessage = false },
                confirmButton = {
                    TextButton(onClick = { showMessage = false }) {
                        Text("ঠিক আছে")
                    }
                },
                text = { Text(message) }
            )
        }
    }
}

@Composable
fun SimpleDropdown(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selected)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
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
        colors = CardDefaults.cardColors(containerColor = LightGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 12.sp, color = Green)
            Spacer(Modifier.height(8.dp))
            Text(
                amount,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Green
            )
        }
    }
}

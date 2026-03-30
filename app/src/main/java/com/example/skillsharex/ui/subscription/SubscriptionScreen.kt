package com.example.skillsharex.ui.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// 🔥 TEMP DATA MODEL (SAFE)
data class SubscriptionPlan(
    val id: Int,
    val name: String,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    navController: NavController
) {

    // 🔥 STATIC DATA (NO API → NO ERROR)
    val plans = listOf(
        SubscriptionPlan(1, "Basic Plan", "₹99/month"),
        SubscriptionPlan(2, "Pro Plan", "₹199/month"),
        SubscriptionPlan(3, "Premium Plan", "₹299/month")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Go Pro 💎") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(plans) { plan ->
                SubscriptionPlanCard(plan) {
                    // 🔥 TEMP CLICK ACTION
                    navController.popBackStack()
                }
            }
        }
    }
}

/* ---------- CARD UI ---------- */
@Composable
fun SubscriptionPlanCard(
    plan: SubscriptionPlan,
    onSubscribe: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = plan.name, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = plan.price)

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onSubscribe) {
                Text("Subscribe")
            }
        }
    }
}
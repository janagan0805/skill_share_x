package com.example.skillsharex.ui.subscription

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillsharex.model.SubscriptionPlan

@Composable
fun SubscriptionPlanCard(plan: SubscriptionPlan, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {

        Column(Modifier.padding(16.dp)) {

            Text(plan.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Text(plan.price, fontSize = 24.sp, color = Color(0xFF4CAF50))

            Spacer(Modifier.height(8.dp))

            plan.features.forEach {
                Text("• $it")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
            ) {
                Text("Subscribe", color = Color.Black)
            }
        }
    }
}
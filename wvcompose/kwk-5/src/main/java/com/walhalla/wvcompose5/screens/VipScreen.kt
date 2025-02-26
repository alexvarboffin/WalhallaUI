package com.walhalla.wvcompose5.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun VipScreen() {
    val vipOffers = listOf(
        VipOffer("💎 БРИЛЛИАНТОВЫЙ VIP", "1,000,000", "• Личный менеджер\n• Максимальные ставки\n• Эксклюзивные бонусы"),
        VipOffer("👑 ЗОЛОТОЙ VIP", "500,000", "• Повышенные лимиты\n• Особые привилегии\n• VIP-поддержка"),
        VipOffer("🌟 СЕРЕБРЯНЫЙ VIP", "100,000", "• Специальные бонусы\n• Увеличенный кэшбэк\n• Приоритетная поддержка")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            "VIP-ПРИВИЛЕГИИ",
            color = Color(0xFFFFD700),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vipOffers) { offer ->
                VipOfferCard(offer)
            }
        }
    }
}

@Composable
private fun VipOfferCard(offer: VipOffer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                offer.title,
                color = Color(0xFFFFD700),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Минимальный депозит: $${offer.minDeposit}",
                color = Color(0xFF4CAF50),
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                offer.benefits,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    "ПОЛУЧИТЬ VIP-СТАТУС",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class VipOffer(
    val title: String,
    val minDeposit: String,
    val benefits: String
) 
package com.walhalla.wvcompose6.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose6.ui.theme.*

@Composable
fun VipScreen() {
    val vipOffers = listOf(
        VipOffer(
            "💎 LUXURY VIP",
            "1,000,000",
            listOf(
                "• Персональный VIP-менеджер 24/7",
                "• Безлимитные ставки",
                "• Эксклюзивные VIP-турниры",
                "• Особые привилегии в нашем клубе"
            )
        ),
        VipOffer(
            "👑 PREMIUM VIP",
            "500,000",
            listOf(
                "• Персональный менеджер",
                "• Повышенные лимиты ставок",
                "• Доступ к VIP-турнирам",
                "• Особые привилегии"
            )
        ),
        VipOffer(
            "🌟 ELITE VIP",
            "100,000",
            listOf(
                "• Приоритетная поддержка",
                "• Увеличенные лимиты",
                "• Специальные бонусы",
                "• VIP-статус"
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        TableGreen.copy(alpha = 0.3f),
                        DarkBackground
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "VIP ПРИВИЛЕГИИ",
                    color = GoldColor,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Откройте для себя мир элитных привилегий",
                    color = TextSecondary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

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
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                offer.title,
                color = GoldColor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Минимальный депозит: $${offer.minDeposit}",
                color = WinningsGreen,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            offer.benefits.forEach { benefit ->
                Text(
                    benefit,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "ПОЛУЧИТЬ VIP СТАТУС",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

private data class VipOffer(
    val title: String,
    val minDeposit: String,
    val benefits: List<String>
) 
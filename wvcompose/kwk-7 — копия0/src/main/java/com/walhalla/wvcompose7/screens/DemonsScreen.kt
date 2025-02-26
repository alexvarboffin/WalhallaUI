package com.walhalla.wvcompose7.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walhalla.wvcompose7.game.viewmodels.DemonsViewModel
import com.walhalla.wvcompose7.game.viewmodels.Demon

@Composable
fun DemonsScreen(
    viewModel: DemonsViewModel,
    modifier: Modifier = Modifier
) {
    var showDealDialog by remember { mutableStateOf(false) }
    
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ДЕМОНЫ АЗАРТА",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Заключи сделку с дьяволом",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            DemonsList(onDemonSelected = { showDealDialog = true })
        }
        
        if (showDealDialog) {
            AlertDialog(
                onDismissRequest = { showDealDialog = false },
                title = { Text("Заключить сделку?") },
                text = { Text("Ваша душа будет принадлежать демону, но взамен вы получите невероятную силу.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.makeDemonPact()
                            showDealDialog = false
                        }
                    ) {
                        Text("Заключить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDealDialog = false }) {
                        Text("Отказаться")
                    }
                }
            )
        }
    }
}

@Composable
private fun DemonsList(onDemonSelected: () -> Unit) {
    Column {
        DemonCard(
            name = "Маммон",
            title = "Князь Алчности",
            power = "Утраивает все выигрыши",
            challenge = "Потеря души при банкротстве",
            onClick = onDemonSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DemonCard(
            name = "Асмодей",
            title = "Князь Азарта",
            power = "Гарантированный джекпот раз в день",
            challenge = "Невозможность выйти из игры при проигрыше",
            onClick = onDemonSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DemonCard(
            name = "Белиал",
            title = "Князь Обмана",
            power = "Возможность подменять карты",
            challenge = "Случайная потеря половины баланса",
            onClick = onDemonSelected
        )
    }
}

@Composable
private fun DemonCard(
    name: String,
    title: String,
    power: String,
    challenge: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = power,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = challenge,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
} 
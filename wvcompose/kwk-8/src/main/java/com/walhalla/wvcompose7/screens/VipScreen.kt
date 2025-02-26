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
import com.walhalla.wvcompose7.game.viewmodels.VipViewModel
import com.walhalla.wvcompose7.game.viewmodels.VipBenefit

@Composable
fun VipScreen(
    viewModel: VipViewModel,
    modifier: Modifier = Modifier
) {
    var showVipDialog by remember { mutableStateOf(false) }
    val vipLevel by viewModel.vipLevel.collectAsState()
    val vipPoints by viewModel.vipPoints.collectAsState()
    
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
                text = "VIP ПРИВИЛЕГИИ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Уровень: $vipLevel | Очки: $vipPoints",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            VipBenefitsList(viewModel = viewModel, onUpgradeClick = { showVipDialog = true })
        }
        
        if (showVipDialog) {
            AlertDialog(
                onDismissRequest = { showVipDialog = false },
                title = { Text("Повысить VIP уровень?") },
                text = { Text("Получите доступ к новым привилегиям и эксклюзивным предложениям.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.purchaseVip()
                            showVipDialog = false
                        }
                    ) {
                        Text("Повысить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showVipDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@Composable
private fun VipBenefitsList(
    viewModel: VipViewModel,
    onUpgradeClick: () -> Unit
) {
    Column {
        VipBenefit.values().forEach { benefit ->
            VipBenefitCard(
                title = benefit.title,
                description = benefit.description,
                requiredLevel = benefit.requiredLevel,
                isUnlocked = viewModel.hasBenefit(benefit),
                onClick = onUpgradeClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VipBenefitCard(
    title: String,
    description: String,
    requiredLevel: Int,
    isUnlocked: Boolean,
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
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = if (isUnlocked) "Разблокировано" else "Требуется VIP ${requiredLevel}",
                fontSize = 16.sp,
                color = if (isUnlocked) 
                    MaterialTheme.colorScheme.primary
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 
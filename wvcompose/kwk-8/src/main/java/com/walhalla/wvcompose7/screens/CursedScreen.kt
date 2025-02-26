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
import com.walhalla.wvcompose7.game.viewmodels.CursedViewModel

@Composable
fun CursedScreen(
    viewModel: CursedViewModel,
    modifier: Modifier = Modifier
) {
    var showCurseDialog by remember { mutableStateOf(false) }
    
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
                text = "ПРОКЛЯТЫЕ ДУШИ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Присоединись к проклятым",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CursedList(onCurseSelected = { showCurseDialog = true })
        }
        
        if (showCurseDialog) {
            AlertDialog(
                onDismissRequest = { showCurseDialog = false },
                title = { Text("Принять проклятие?") },
                text = { Text("Ваша душа будет проклята, но взамен вы получите силу тьмы.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.acceptCurse()
                            showCurseDialog = false
                        }
                    ) {
                        Text("Принять")
                    }
                },
                dismissButton = {
                    Button(onClick = { showCurseDialog = false }) {
                        Text("Отказаться")
                    }
                }
            )
        }
    }
}

@Composable
private fun CursedList(onCurseSelected: () -> Unit) {
    Column {
        CursedCard(
            name = "Проклятие Жадности",
            status = "Активно",
            power = "Увеличивает все выигрыши на 50%",
            curse = "Каждый проигрыш удваивается",
            onClick = onCurseSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CursedCard(
            name = "Проклятие Азарта",
            status = "Доступно",
            power = "Шанс выигрыша увеличен на 25%",
            curse = "Невозможно остановить игру при выигрышной серии",
            onClick = onCurseSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CursedCard(
            name = "Проклятие Власти",
            status = "Заблокировано",
            power = "Возможность управлять результатом раз в день",
            curse = "Потеря всех накоплений при проигрыше",
            onClick = onCurseSelected
        )
    }
}

@Composable
private fun CursedCard(
    name: String,
    status: String,
    power: String,
    curse: String,
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
                text = status,
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
                text = curse,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
} 
package com.walhalla.wvcompose7.game.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.walhalla.wvcompose7.game.models.LuckSystem

@Composable
fun LuckEffectsOverlay(
    luckSystem: LuckSystem,
    modifier: Modifier = Modifier
) {
    var showEffect by remember { mutableStateOf(false) }
    
    LaunchedEffect(luckSystem.isDemonPactActive()) {
        if (luckSystem.isDemonPactActive()) {
            showEffect = true
        }
    }
    
    AnimatedVisibility(
        visible = showEffect,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x88000000))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "👿 ДЕМОНИЧЕСКИЙ ПАКТ АКТИВЕН",
                    color = Color.Red,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Множитель удачи: x${String.format("%.1f", luckSystem.getStreakMultiplier())}",
                    color = Color.White,
                    fontSize = 18.sp
                )
                
                Text(
                    text = "Очки удачи: ${luckSystem.getLuckPoints()}",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
} 
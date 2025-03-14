package com.walhalla.wvcompose6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.walhalla.wvcompose6.game.BlackjackScreen
import com.walhalla.wvcompose6.screens.KingsScreen
import com.walhalla.wvcompose6.screens.ProfileScreen
import com.walhalla.wvcompose6.screens.VipScreen

import com.walhalla.wvcompose6.ui.theme.WVComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WVComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                Config.MENU_ITEMS.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        label = { Text(item.title) },
                        icon = { Text(item.title.split(" ")[0]) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedItem) {
                0 -> BlackjackScreen()
                1 -> VipScreen()
                2 -> KingsScreen()
                3 -> ProfileScreen()
            }
        }
    }
} 
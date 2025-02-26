package com.walhalla.wvcompose4

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.walhalla.wvcompose4.game.SlotMachineScreen
import com.walhalla.wvcompose4.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Config.GAME_NAME) },
                navigationIcon = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.Menu, contentDescription = "Меню")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                Config.MENU_ITEMS.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = item.title)
                                1 -> Icon(Icons.Default.Info, contentDescription = item.title)
                                2 -> Icon(Icons.Default.Star, contentDescription = item.title)
                                3 -> Icon(Icons.Default.Person, contentDescription = item.title)
                                else -> Icon(Icons.Default.Home, contentDescription = item.title)
                            }
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
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
                0 -> SlotMachineScreen()
                1 -> RulesScreen()
                2 -> LeaderboardScreen()
                3 -> ProfileScreen()
            }
            
            if (showMenu) {
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    Config.MENU_ITEMS.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.title) },
                            onClick = {
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
} 
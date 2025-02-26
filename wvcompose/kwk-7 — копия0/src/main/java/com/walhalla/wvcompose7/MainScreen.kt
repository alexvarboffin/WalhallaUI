package com.walhalla.wvcompose7

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.walhalla.wvcompose7.game.CasinoScreen
import com.walhalla.wvcompose7.game.screens.RouletteScreen
import com.walhalla.wvcompose7.screens.*
import com.walhalla.wvcompose7.game.viewmodels.*

@Composable
fun MainScreen() {
    var selectedScreen by remember { mutableStateOf(Screen.CASINO) }
    
    val casinoViewModel: CasinoViewModel = viewModel()
    val demonsViewModel: DemonsViewModel = viewModel()
    val cursedViewModel: CursedViewModel = viewModel()
    val vipViewModel: VipViewModel = viewModel()
    val rouletteViewModel: RouletteViewModel = viewModel()
    
    when (selectedScreen) {
        Screen.CASINO -> CasinoScreen(
            viewModel = casinoViewModel,
            onNavigateToDevilsDeal = { selectedScreen = Screen.DEMONS },
            onNavigateToAchievements = { selectedScreen = Screen.ACHIEVEMENTS }
        )
        Screen.DEMONS -> DemonsScreen(
            viewModel = demonsViewModel
        )
        Screen.CURSED -> CursedScreen(
            viewModel = cursedViewModel
        )
        Screen.VIP -> VipScreen(
            viewModel = vipViewModel
        )
        Screen.ROULETTE -> RouletteScreen(
            viewModel = rouletteViewModel
        )
        Screen.ACHIEVEMENTS -> AchievementsScreen()
    }
}

enum class Screen {
    CASINO,
    DEMONS,
    CURSED,
    VIP,
    ROULETTE,
    ACHIEVEMENTS
} 
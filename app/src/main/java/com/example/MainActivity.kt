package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.CinexBottomBar
import com.example.ui.components.CinexTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.CinexViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CinexViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CinexApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CinexApp(viewModel: CinexViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()

    // Handle Android system back button: return to Home if on secondary tab
    BackHandler(enabled = currentTab != AppNavTab.HOME) {
        viewModel.selectTab(AppNavTab.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CinexTopBar(viewModel = viewModel)
        },
        bottomBar = {
            CinexBottomBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                when (tab) {
                    AppNavTab.HOME -> HomeScreen(viewModel = viewModel)
                    AppNavTab.PC_BUILDER -> PcBuilderScreen(viewModel = viewModel)
                    AppNavTab.SERVICES -> ServicesScreen(viewModel = viewModel)
                    AppNavTab.CATALOG -> CatalogScreen(viewModel = viewModel)
                    AppNavTab.CONTACT -> ContactScreen(viewModel = viewModel)
                }
            }
        }
    }
}

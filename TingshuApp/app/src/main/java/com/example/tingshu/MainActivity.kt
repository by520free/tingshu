package com.example.tingshu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tingshu.data.preferences.ThemeMode
import com.example.tingshu.player.PlayerController
import com.example.tingshu.ui.components.MiniPlayer
import com.example.tingshu.ui.navigation.BottomNavGraph
import com.example.tingshu.ui.navigation.BottomNavigationBar
import com.example.tingshu.ui.navigation.Routes
import com.example.tingshu.ui.theme.TingshuAppTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val themeMode by mainViewModel.themeMode.collectAsState(initial = ThemeMode.FOLLOW_SYSTEM)
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.FOLLOW_SYSTEM -> systemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            TingshuAppTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(playerController: PlayerController = hiltViewModel<MainViewModel>().playerController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val playerState by playerController.playerState.collectAsStateWithLifecycle()

    val isPlayerScreen = currentDestination?.route == Routes.PLAYER
    val showMiniPlayer = playerState.currentBook != null && !isPlayerScreen
    val showBottomNav = !isPlayerScreen

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (showMiniPlayer) {
                        MiniPlayer(
                            playerState = playerState,
                            onClick = {
                                navController.navigate(Routes.player())
                            },
                            onPlayPauseClick = {
                                playerController.toggle()
                            }
                        )
                    }
                    BottomNavigationBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        }
    ) { innerPadding ->
        BottomNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    val playerController: PlayerController,
    private val settingsPreferences: com.example.tingshu.data.preferences.SettingsPreferences
) : androidx.lifecycle.ViewModel() {

    val themeMode = settingsPreferences.themeMode
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.AiToolId
import com.example.data.BharatPreferencesRepository
import com.example.data.BharatUiState
import com.example.ui.components.BharatEmblemMark
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.AiToolsScreen
import com.example.ui.screens.ComingSoonToolScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PhotoEditorScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.BharatAiTheme
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BharatViewModel

object BharatRoutes {
    const val HOME = "tab_home"
    const val AI_TOOLS = "tab_ai_tools"
    const val WALLET = "tab_wallet"
    const val PROFILE = "tab_profile"
    const val PREMIUM = "screen_premium"
}

private data class BharatNavTab(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val bharatTabs = listOf(
    BharatNavTab(
        route = BharatRoutes.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        testTag = "nav_home"
    ),
    BharatNavTab(
        route = BharatRoutes.AI_TOOLS,
        label = "AI Tools",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
        testTag = "nav_ai_tools"
    ),
    BharatNavTab(
        route = BharatRoutes.WALLET,
        label = "Wallet",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet,
        testTag = "nav_wallet"
    ),
    BharatNavTab(
        route = BharatRoutes.PROFILE,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        testTag = "nav_profile"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = BharatPreferencesRepository(applicationContext)

        setContent {
            BharatAiTheme {
                val viewModel: BharatViewModel = viewModel(
                    factory = BharatViewModel.Factory(repository)
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                BharatAppRoot(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun BharatAppRoot(
    uiState: BharatUiState,
    viewModel: BharatViewModel
) {
    if (!uiState.isLoaded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NavyDeep),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SaffronPrimary)
        }
        return
    }

    if (!uiState.isLoggedIn) {
        LoginScreen(
            initialName = uiState.profile.name,
            initialContact = uiState.profile.contact,
            onSignInSuccess = { name, contact ->
                viewModel.signInDemo(name, contact)
            },
            onContinueAsGuest = {
                viewModel.continueAsGuest()
            }
        )
    } else {
        MainTabNavigationHost(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

@Composable
private fun MainTabNavigationHost(
    uiState: BharatUiState,
    viewModel: BharatViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BharatRoutes.HOME
    val tabRoutes = remember { bharatTabs.map { it.route }.toSet() }
    val showTabChrome = currentRoute in tabRoutes

    var pendingChatPrompt by remember { mutableStateOf<String?>(null) }

    val navigateToTab: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val openTool: (AiToolId) -> Unit = { toolId ->
        navController.navigate(toolId.routeKey) {
            launchSingleTop = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (showTabChrome && !useNavRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        bharatTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateToTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = { Text(tab.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PureWhite,
                                    selectedTextColor = SaffronPrimary,
                                    indicatorColor = SaffronPrimary,
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag(tab.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showTabChrome && useNavRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        header = {
                            BharatEmblemMark(
                                size = 40.dp,
                                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                            )
                        },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        bharatTabs.forEach { tab ->
                            val selected = currentRoute == tab.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTab(tab.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = { Text(tab.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = PureWhite,
                                    selectedTextColor = SaffronPrimary,
                                    indicatorColor = SaffronPrimary
                                ),
                                modifier = Modifier.testTag("${tab.testTag}_rail")
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = BharatRoutes.HOME,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    composable(BharatRoutes.HOME) {
                        HomeScreen(
                            uiState = uiState,
                            onOpenTool = openTool,
                            onOpenChatWithPrompt = { prompt ->
                                pendingChatPrompt = prompt
                                openTool(AiToolId.CHAT)
                            },
                            onNavigateToToolsTab = { navigateToTab(BharatRoutes.AI_TOOLS) },
                            onNavigateToWalletTab = { navigateToTab(BharatRoutes.WALLET) },
                            onNavigateToProfileTab = { navigateToTab(BharatRoutes.PROFILE) },
                            onOpenPremiumScreen = {
                                navController.navigate(BharatRoutes.PREMIUM) { launchSingleTop = true }
                            }
                        )
                    }

                    composable(BharatRoutes.AI_TOOLS) {
                        AiToolsScreen(onOpenTool = openTool)
                    }

                    composable(BharatRoutes.WALLET) {
                        WalletScreen(
                            uiState = uiState,
                            onClaimDailyBonus = { viewModel.claimDailyBonus() },
                            onOpenPremiumScreen = {
                                navController.navigate(BharatRoutes.PREMIUM) { launchSingleTop = true }
                            }
                        )
                    }

                    composable(BharatRoutes.PROFILE) {
                        ProfileScreen(
                            uiState = uiState,
                            onUpdateProfile = { viewModel.updateProfile(it) },
                            onOpenPremiumScreen = {
                                navController.navigate(BharatRoutes.PREMIUM) { launchSingleTop = true }
                            },
                            onSignOutDemo = { viewModel.signOutDemo() }
                        )
                    }

                    composable(AiToolId.CHAT.routeKey) {
                        AiChatScreen(
                            messages = uiState.chatMessages,
                            isTyping = uiState.isAssistantTyping,
                            initialPrompt = pendingChatPrompt,
                            onConsumeInitialPrompt = { pendingChatPrompt = null },
                            onSendMessage = { viewModel.sendChatMessage(it) },
                            onClearChat = { viewModel.clearChat() },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(AiToolId.PHOTO_EDITOR.routeKey) {
                        PhotoEditorScreen(
                            onSavePreview = { viewModel.recordPhotoEditSaved() },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(AiToolId.IMAGE_GENERATOR.routeKey) {
                        ComingSoonToolScreen(
                            toolId = AiToolId.IMAGE_GENERATOR,
                            isNotified = AiToolId.IMAGE_GENERATOR.routeKey in uiState.notifiedToolIds,
                            onToggleNotify = {
                                viewModel.toggleToolNotification(AiToolId.IMAGE_GENERATOR.routeKey)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(AiToolId.VIDEO_GENERATOR.routeKey) {
                        ComingSoonToolScreen(
                            toolId = AiToolId.VIDEO_GENERATOR,
                            isNotified = AiToolId.VIDEO_GENERATOR.routeKey in uiState.notifiedToolIds,
                            onToggleNotify = {
                                viewModel.toggleToolNotification(AiToolId.VIDEO_GENERATOR.routeKey)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(AiToolId.VOICE_STUDIO.routeKey) {
                        ComingSoonToolScreen(
                            toolId = AiToolId.VOICE_STUDIO,
                            isNotified = AiToolId.VOICE_STUDIO.routeKey in uiState.notifiedToolIds,
                            onToggleNotify = {
                                viewModel.toggleToolNotification(AiToolId.VOICE_STUDIO.routeKey)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(BharatRoutes.PREMIUM) {
                        PremiumScreen(
                            selectedPlanId = uiState.selectedPlanId,
                            onSelectPreviewPlan = { viewModel.selectPreviewPlan(it) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

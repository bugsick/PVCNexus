package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.db.AppDatabase
import com.example.data.repository.PvcNexusRepository
import com.example.ui.screens.admin.AdminMainContainer
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.user.UserMainContainer
import com.example.ui.theme.NexusBackground
import com.example.ui.theme.PvcNexusTheme
import com.example.ui.viewmodel.PvcNexusViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = AppDatabase.getInstance(applicationContext)
    val repository = PvcNexusRepository(
      userDao = database.userDao(),
      transactionDao = database.transactionDao(),
      withdrawalDao = database.withdrawalDao(),
      adCampaignDao = database.adCampaignDao(),
      complaintDao = database.complaintDao(),
      broadcastDao = database.broadcastDao(),
      systemConfigDao = database.systemConfigDao()
    )

    setContent {
      PvcNexusTheme {
        val nexusViewModel: PvcNexusViewModel = viewModel(
          factory = PvcNexusViewModel.provideFactory(application, repository)
        )

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = NexusBackground
        ) {
          PvcNexusApp(viewModel = nexusViewModel)
        }
      }
    }
  }
}

@Composable
fun PvcNexusApp(viewModel: PvcNexusViewModel) {
  val currentUser by viewModel.currentUser.collectAsState()
  val currentDest by viewModel.currentDestination.collectAsState()

  Crossfade(targetState = currentUser != null, label = "auth_state") { isAuthenticated ->
    if (!isAuthenticated) {
      LoginScreen(viewModel = viewModel)
    } else {
      val isAdminDest = currentDest in listOf(
        ScreenDestination.AdminDashboard,
        ScreenDestination.AdminUserOps,
        ScreenDestination.AdminGlobalRates,
        ScreenDestination.AdminWithdrawalAuth,
        ScreenDestination.AdminAdManager,
        ScreenDestination.AdminBroadcasts
      )

      if (isAdminDest && currentUser?.role == "ADMIN") {
        AdminMainContainer(viewModel = viewModel)
      } else {
        UserMainContainer(viewModel = viewModel)
      }
    }
  }
}

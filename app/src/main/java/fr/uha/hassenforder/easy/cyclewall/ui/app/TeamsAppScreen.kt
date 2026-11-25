package fr.uha.hassenforder.easy.cyclewall.ui.app

import android.app.WallpaperManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Cyclone
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import fr.uha.hassenforder.easy.android.model.FlexibleIcon
import fr.uha.hassenforder.easy.android.model.IconRender
import fr.uha.hassenforder.easy.android.ui.app.AppNavigatorScreen
import fr.uha.hassenforder.easy.android.ui.app.PrimaryNavigation
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.navigation.CyclesDestination
import fr.uha.hassenforder.easy.cyclewall.navigation.GreetingDestination
import fr.uha.hassenforder.easy.cyclewall.navigation.EventDestination
import fr.uha.hassenforder.easy.cyclewall.navigation.SettingsDestination

@Composable
fun CycleWallAppScreen(
    vm: AppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    if (WallpaperManager.getInstance(context).isWallpaperSupported) {
        AppNavigatorScreen(vm.navigator, primaryNavigations)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.wallpaper_not_supported),
                fontSize = 32.sp,
                color = Color.Red
            )
        }
    }
}

private val primaryNavigations = listOf (
    PrimaryNavigation(
        destination = GreetingDestination.GreetingScreen,
        icon = IconRender(
            focused = FlexibleIcon(vector = Icons.Filled.Home),
            unfocused = FlexibleIcon(vector = Icons.Outlined.Home)
        ),
        labelId = R.string.app_name
    ),
    PrimaryNavigation(
        destination = CyclesDestination.ListScreen,
        icon = IconRender(focused = FlexibleIcon(vector= Icons.Filled.Cyclone), unfocused = FlexibleIcon(vector= Icons.Outlined.Cyclone)),
        labelId = R.string.action_cycles
    ),
    PrimaryNavigation(
        destination = EventDestination.ListScreen,
        icon = IconRender(focused = FlexibleIcon(vector= Icons.Filled.Schedule), unfocused = FlexibleIcon(vector= Icons.Outlined.Schedule)),
        labelId = R.string.action_next
    ),
    PrimaryNavigation(
        destination = SettingsDestination.SettingsScreen,
        icon = IconRender(focused = FlexibleIcon(vector= Icons.Filled.Settings), unfocused = FlexibleIcon(vector= Icons.Outlined.Settings)),
        labelId = R.string.action_settings
    ),
)

package fr.uha.hassenforder.easy.cyclewall.ui

import androidx.compose.runtime.Composable
import fr.uha.hassenforder.easy.android.ui.screen.greeting.EasyGreetingScreen
import fr.uha.hassenforder.easy.android.ui.screen.greeting.PortraitGreeting
import fr.uha.hassenforder.easy.cyclewall.R

@Composable
fun GreetingScreen() {
    EasyGreetingScreen (
        titleId = R.string.app_short,
        launch = null,
    ) {
        PortraitGreeting()
    }
}

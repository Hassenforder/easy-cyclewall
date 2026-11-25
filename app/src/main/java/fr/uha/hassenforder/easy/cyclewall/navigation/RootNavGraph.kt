package fr.uha.hassenforder.easy.cyclewall.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

fun EntryProviderScope<NavKey>.rootNavGraph () {
    greetingNavGraph()
    cyclesNavGraph()
    eventNavGraph()
    settingsNavGraph()
}
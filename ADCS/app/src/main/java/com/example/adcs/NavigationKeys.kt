package com.example.adcs

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Navigation destinations
@Serializable data object Splash : NavKey
@Serializable data object Login : NavKey
@Serializable data object Dashboard : NavKey
@Serializable data object PartiesScreen : NavKey
@Serializable data object PropertyScreen : NavKey
@Serializable data object RentScreen : NavKey
@Serializable data object DepositScreen : NavKey
@Serializable data object DocumentsScreen : NavKey
@Serializable data object SummaryScreen : NavKey
@Serializable data object PdfScreen : NavKey
@Serializable data object HistoryScreen : NavKey
@Serializable data object SettingsScreen : NavKey

// Keep Main for backward compat with template
@Serializable data object Main : NavKey

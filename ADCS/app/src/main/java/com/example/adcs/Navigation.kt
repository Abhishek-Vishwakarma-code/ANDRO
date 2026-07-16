package com.example.adcs

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.adcs.ui.main.*

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            // Splash
            entry<Splash> {
                SplashScreen(
                    onNavigateToDashboard = {
                        backStack.removeLastOrNull()
                        backStack.add(Login)
                    }
                )
            }

            // Login
            entry<Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.removeLastOrNull()
                        backStack.add(Dashboard)
                    }
                )
            }

            // Dashboard
            entry<Dashboard> {
                DashboardScreen(
                    onNewAgreement = { backStack.add(PartiesScreen) },
                    onHistory = { backStack.add(HistoryScreen) },
                    onSettings = { backStack.add(SettingsScreen) }
                )
            }

            // Agreement Flow — Step 1: Parties
            entry<PartiesScreen> {
                PartiesScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(PropertyScreen) }
                )
            }

            // Step 2: Property
            entry<PropertyScreen> {
                PropertyScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(RentScreen) }
                )
            }

            // Step 3: Rent
            entry<RentScreen> {
                RentScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(DepositScreen) }
                )
            }

            // Step 4: Deposit
            entry<DepositScreen> {
                DepositScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(DocumentsScreen) }
                )
            }

            // Step 5: Documents
            entry<DocumentsScreen> {
                DocumentsScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onNext = { backStack.add(SummaryScreen) }
                )
            }

            // Summary / Receipt
            entry<SummaryScreen> {
                SummaryScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onGeneratePdf = { backStack.add(PdfScreen) }
                )
            }

            // PDF Export
            entry<PdfScreen> {
                PdfScreen(
                    onBack = { backStack.removeLastOrNull() },
                    onDone = {
                        // Pop all the way back to dashboard
                        while (backStack.size > 1) backStack.removeLastOrNull()
                        backStack.removeLastOrNull()
                        backStack.add(Dashboard)
                    }
                )
            }

            // History
            entry<HistoryScreen> {
                HistoryScreen(onBack = { backStack.removeLastOrNull() })
            }

            // Settings
            entry<SettingsScreen> {
                SettingsScreen(onBack = { backStack.removeLastOrNull() })
            }

            // Backward compat
            entry<Main> {
                DashboardScreen(
                    onNewAgreement = { backStack.add(PartiesScreen) },
                    onHistory = { backStack.add(HistoryScreen) },
                    onSettings = { backStack.add(SettingsScreen) }
                )
            }
        }
    )
}

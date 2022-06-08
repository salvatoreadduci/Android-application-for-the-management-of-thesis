package com.whyskey.tesiunical

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThesisScreen(
    val icon: ImageVector
    ) {
        Profile(
            icon = Icons.Filled.Info,
        ),
        ThesisFullScreen(
            icon = Icons.Filled.AttachMoney,
        ),
        Bills(
            icon = Icons.Filled.MoneyOff,
        );

        companion object {
            fun fromRoute(route: String?): ThesisScreen =
                when (route?.substringBefore("/")) {
                    ThesisFullScreen.name -> ThesisFullScreen
                    Profile.name -> Profile
                    null -> Profile
                    else -> throw IllegalArgumentException("Route $route is not recognized.")
                }
        }
}
package com.arcade.checkers.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arcade.checkers.R
import com.arcade.checkers.ui.theme.*

data class NavItem(val label: String, val iconRes: Int, val route: String)

val bottomNavItems = listOf(
    NavItem("PLAY", R.drawable.ic_play_circle, "welcome"),
    NavItem("BATTLE", R.drawable.ic_swords, "game"),
    NavItem("STATS", R.drawable.ic_bar_chart, "stats"),
    NavItem("SHOP", R.drawable.ic_local_mall, "shop")
)

@Composable
fun ArcadeBottomNav(currentRoute: String, onSelect: (String) -> Unit) {
    NavigationBar(
        containerColor = SurfaceContainerLowest.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier.height(64.dp)
    ) {
        bottomNavItems.forEach { item ->
            val selected = item.route == currentRoute
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label,
                        tint = if (selected) PrimaryContainer else OnSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (selected) PrimaryContainer else OnSurfaceVariant
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = PrimaryContainer.copy(alpha = 0.12f)
                )
            )
        }
    }
}

@Composable
fun ArcadeChip(
    text: String,
    containerColor: Color = SurfaceContainerLowest,
    contentColor: Color = TertiaryContainer,
    borderColor: Color? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(containerColor)
            .then(
                if (borderColor != null) Modifier.border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
                else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = contentColor, style = MaterialTheme.typography.labelSmall)
    }
}

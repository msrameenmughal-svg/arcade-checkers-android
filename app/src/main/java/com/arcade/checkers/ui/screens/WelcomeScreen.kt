package com.arcade.checkers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arcade.checkers.R
import com.arcade.checkers.ui.components.ArcadeBottomNav
import com.arcade.checkers.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(onStartGame: () -> Unit, onNavigateStats: () -> Unit) {
    var selectedMode by remember { mutableStateOf("bot") }
    var difficulty by remember { mutableStateOf("TURBO") }

    Scaffold(
        containerColor = Background,
        bottomBar = { ArcadeBottomNav(currentRoute = "welcome", onSelect = {}) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Top HUD header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLowest.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sports_esports),
                        contentDescription = null,
                        tint = PrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("PLAY", color = PrimaryContainer, style = MaterialTheme.typography.headlineSmall)
                    Text("LVL 24", color = SecondaryContainer, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.weight(1f))
                // Coins
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(painterResource(R.drawable.ic_toll), null, tint = TertiaryContainer, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("1,250", color = Tertiary, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_volume_up), "Sound", tint = OnSurfaceVariant)
                }
                IconButton(onClick = {}) {
                    Icon(painterResource(R.drawable.ic_settings), "Settings", tint = OnSurfaceVariant)
                }
            }

            // INSERT COIN marquee
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_bolt), null, tint = TertiaryContainer, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "INSERT COIN  ·  CLASSIC 8x8 CYBER",
                    color = TertiaryContainer,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp
                )
            }

            // QUICK MATCH VS BOT card
            QuickMatchCard(
                difficulty = difficulty,
                onDifficultyChange = { difficulty = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Daily puzzle
            DailyPuzzleCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(12.dp))

            // Pass & Play card
            PassAndPlayCard(
                selected = selectedMode == "local",
                onClick = { selectedMode = "local" },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // START BATTLE button
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Icon(painterResource(R.drawable.ic_play_arrow), null, tint = OnPrimary)
                Spacer(Modifier.width(8.dp))
                Text(
                    "START BATTLE",
                    color = DeepVoid,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.width(8.dp))
                ArcadeChip(text = "1 COIN", containerColor = DeepVoid.copy(alpha = 0.25f), contentColor = DeepVoid)
            }

            Spacer(Modifier.height(16.dp))

            // Streak banner
            StreakBanner(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun QuickMatchCard(difficulty: String, onDifficultyChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SynthCharcoal),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryContainer.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(SurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_smart_toy), null, tint = PrimaryContainer, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("QUICK MATCH VS BOT", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Text("Instant cyber skirmish with adaptive CPU", color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                ArcadeChip("RANKED", containerColor = ErrorContainer, contentColor = OnErrorContainer)
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = PanelBorder)
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_tune), null, tint = OnSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Difficulty Level", color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.weight(1f))
                Text("TURBO PRO (x1.5 XP)", color = TertiaryContainer, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(10.dp))

            // Difficulty segments
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("ROOKIE", "8-BIT", "TURBO", "PRO AI", "BOSS").forEach { level ->
                    val active = level == difficulty
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (active) PrimaryContainer else SurfaceContainerLowest)
                            .clickable { onDifficultyChange(level) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            level,
                            color = if (active) DeepVoid else OnSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_stars), null, tint = TertiaryContainer, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("CYBER GOD", color = TertiaryContainer, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun DailyPuzzleCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CabinetViolet),
        border = androidx.compose.foundation.BorderStroke(1.dp, PanelBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.ic_bolt), null, tint = TertiaryContainer, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("DAILY PUZZLE #42", color = TertiaryContainer, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Icon(painterResource(R.drawable.ic_timer), null, tint = Error, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("04:12:18", color = Error, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(8.dp))
            Text("Triple Hop Blitz", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text("Force double jump & crown king in 3 moves.", color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ArcadeChip("+250 COINS", containerColor = TertiaryContainer.copy(alpha = 0.2f), contentColor = TertiaryContainer)
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("PLAY", color = DeepVoid, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Icon(painterResource(R.drawable.ic_arrow_forward), null, tint = DeepVoid, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun PassAndPlayCard(selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SynthCharcoal),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (selected) SecondaryContainer else PanelBorder
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(SurfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_swords), null, tint = SecondaryContainer, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("PASS & PLAY", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                Text("Single screen 1v1 tabletop duel with a friend.", color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_phonelink_ring), null, tint = SecondaryContainer, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("SETUP", color = SecondaryContainer, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun StreakBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow)
    ) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.ic_local_fire_department), null, tint = TertiaryContainer, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text("Day 5 Streak Active", color = TertiaryContainer, style = MaterialTheme.typography.labelMedium)
                Text("3x COIN MULTIPLIER", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Crown Blitz: King 10 Cyber", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                Text("Checkers  7/10", color = PrimaryContainer, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

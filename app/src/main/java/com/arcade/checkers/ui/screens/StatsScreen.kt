package com.arcade.checkers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arcade.checkers.R
import com.arcade.checkers.ui.components.ArcadeBottomNav
import com.arcade.checkers.ui.components.ArcadeChip
import com.arcade.checkers.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = Background,
        bottomBar = { ArcadeBottomNav(currentRoute = "stats", onSelect = {}) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLowest)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(SurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_analytics), null, tint = PrimaryContainer, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("STATS", color = PrimaryContainer, style = MaterialTheme.typography.headlineSmall)
                    Text("LVL 24", color = SecondaryContainer, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SurfaceContainerLow).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Icon(painterResource(R.drawable.ic_toll), null, tint = TertiaryContainer, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("1,250", color = Tertiary, style = MaterialTheme.typography.labelSmall)
                }
                IconButton(onClick = onBack) { Icon(painterResource(R.drawable.ic_chevron_right), "Back", tint = OnSurfaceVariant) }
            }

            // Hall of Fame banner
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CabinetViolet),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PanelBorder)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_military_tech), null, tint = TertiaryContainer, modifier = Modifier.size(32.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("HALL OF FAME", color = TertiaryContainer, style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
                        Text("NEON KNIGHT #1042", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Icon(painterResource(R.drawable.ic_local_fire_department), null, tint = Error, modifier = Modifier.size(18.dp))
                        Text("9 STREAK", color = Error, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // Win rate + streak row
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Win rate
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("WIN RATE", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                            Spacer(Modifier.weight(1f))
                            Icon(painterResource(R.drawable.ic_percent), null, tint = PrimaryContainer, modifier = Modifier.size(14.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("68.4%", color = PrimaryContainer, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        // Progress bar
                        Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(SurfaceContainerHighest)) {
                            Box(Modifier.fillMaxWidth(0.684f).fillMaxHeight().clip(CircleShape).background(PrimaryContainer))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("97 Wins / 45 Losses", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                    }
                }
                // Streak
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("STREAK", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                            Spacer(Modifier.weight(1f))
                            Icon(painterResource(R.drawable.ic_stars), null, tint = TertiaryContainer, modifier = Modifier.size(14.dp))
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("9", color = TertiaryContainer, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold)
                        Text("Wins", color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        Text("Current streak", color = Outline, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Stat tiles
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTile("BATTLES", "142", R.drawable.ic_sports_esports, PrimaryContainer, Modifier.weight(1f))
                StatTile("CROWNED", "318", R.drawable.ic_crown, TertiaryContainer, Modifier.weight(1f))
                StatTile("ARCADE ELO", "1,894", R.drawable.ic_bolt, SecondaryContainer, Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            // Arcade Ledger
            Text(
                "ARCADE LEDGER",
                color = OnSurface,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "SEASON 04",
                color = OnSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            // Match cards
            LedgerMatchCard(
                isWin = true, elo = "+24 ELO", time = "12m ago", opponent = "vs MECHA-MIND",
                detail = "Captured 12/12 pieces · 2 Kings promoted", pts = "+450 PTS",
                accent = PrimaryContainer, containerColor = CabinetViolet
            )
            LedgerMatchCard(
                isWin = false, elo = "-12 ELO", time = "2h ago", opponent = "vs CYBER-BOT 9000",
                detail = "Bot corner trap on turn 24 · 8 captures", pts = "+110 PTS",
                accent = Error, containerColor = SurfaceContainer
            )
            LedgerMatchCard(
                isWin = true, elo = "+18 ELO", time = "Yesterday", opponent = "vs PIXEL-ROOKIE",
                detail = "Triple jump chain victory · Flawless midgame", pts = "+300 PTS",
                accent = PrimaryContainer, containerColor = SurfaceContainer
            )

            Spacer(Modifier.height(16.dp))

            // Tactical Matrix
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SynthCharcoal),
                border = androidx.compose.foundation.BorderStroke(1.dp, PanelBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_analytics), null, tint = PrimaryContainer, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("TACTICAL MATRIX", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.weight(1f))
                        ArcadeChipLabel("TOP 5% IN REGION")
                    }
                    Spacer(Modifier.height(14.dp))
                    TacticalBar("KING CONVERSION RATE", 0.84f, "84%")
                    TacticalBar("DOUBLE JUMP ACCURACY", 0.91f, "91%")
                    TacticalBar("TRAP RESISTANCE", 0.76f, "76%")
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("AVG MATCH TIME", color = Outline, style = MaterialTheme.typography.labelSmall)
                            Text("5m 20s", color = OnSurface, style = MaterialTheme.typography.labelMedium)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("MAX CHAIN JUMP", color = Outline, style = MaterialTheme.typography.labelSmall)
                            Text("5 PIECES", color = TertiaryContainer, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, iconRes: Int, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painterResource(iconRes), null, tint = accent, modifier = Modifier.size(18.dp))
            Spacer(Modifier.height(6.dp))
            Text(value, color = accent, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun LedgerMatchCard(
    isWin: Boolean, elo: String, time: String, opponent: String,
    detail: String, pts: String, accent: Color, containerColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(if (isWin) R.drawable.ic_check_circle else R.drawable.ic_cancel),
                    null, tint = accent, modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    if (isWin) "WIN ($elo)" else "LOSS ($elo)",
                    color = accent, style = MaterialTheme.typography.labelMedium, letterSpacing = 1.sp
                )
                Spacer(Modifier.weight(1f))
                Text(time, color = Outline, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(6.dp))
            Text(opponent, color = OnSurface, style = MaterialTheme.typography.headlineSmall)
            Text(detail, color = OnSurfaceVariant, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                ArcadeChipLabel(pts)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_replay), null, tint = OnSurfaceVariant, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("REPLAY", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun TacticalBar(label: String, fraction: Float, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(label, color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.weight(1f))
            Text(value, color = TertiaryContainer, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(4.dp))
        Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(SurfaceContainerHighest)) {
            Box(Modifier.fillMaxWidth(fraction).fillMaxHeight().clip(CircleShape).background(PrimaryContainer))
        }
    }
}

@Composable
private fun ArcadeChipLabel(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(TertiaryContainer.copy(alpha = 0.2f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = TertiaryContainer, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
    }
}

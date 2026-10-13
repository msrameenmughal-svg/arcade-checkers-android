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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arcade.checkers.R
import com.arcade.checkers.game.Cell
import com.arcade.checkers.game.CheckersGame
import com.arcade.checkers.ui.components.ArcadeChip
import com.arcade.checkers.game.PieceColor
import com.arcade.checkers.game.PieceType
import com.arcade.checkers.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(game: CheckersGame, onBack: () -> Unit, onStats: () -> Unit) {
    // Force a recomposition when game state changes
    var tick by remember { mutableStateOf(0) }
    var showHint by remember { mutableStateOf(false) }

    // Auto AI move for RED player
    LaunchedEffect(game.currentPlayer, tick) {
        if (game.currentPlayer == PieceColor.RED && game.winner == null) {
            delay(700)
            val moves = game.allLegalMoves(PieceColor.RED)
            if (moves.isNotEmpty()) {
                val captures = moves.filter { it.isCapture }
                val chosen = (captures.ifEmpty { moves }).random()
                game.select(chosen.from.row, chosen.from.col)
                game.makeMove(chosen)
                tick++
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            // Top arcade header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLowest)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(painterResource(R.drawable.ic_chevron_right), "Back", tint = OnSurfaceVariant,
                        modifier = Modifier.rotate(180f))
                }
                Box(
                    modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(SurfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.ic_sports_esports), null, tint = PrimaryContainer, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text("BATTLE", color = Primary, style = MaterialTheme.typography.headlineSmall, letterSpacing = 1.sp)
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SurfaceContainerLow).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Icon(painterResource(R.drawable.ic_toll), null, tint = TertiaryContainer, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("1,250", color = Tertiary, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.width(4.dp))
                IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_volume_up), null, tint = OnSurfaceVariant, modifier = Modifier.size(18.dp)) }
                IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_settings), null, tint = OnSurfaceVariant, modifier = Modifier.size(18.dp)) }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Opponent HUD (AI)
            OpponentHud(
                activePieces = game.piecesOf(PieceColor.RED),
                captured = game.capturedByCyan,
                thinking = game.currentPlayer == PieceColor.RED && game.winner == null
            )

            Spacer(Modifier.height(8.dp))

            // Match banner
            MatchBanner(
                turnLabel = when {
                    game.winner != null -> "GAME OVER"
                    game.currentPlayer == PieceColor.CYAN -> "YOUR TURN! (P1)"
                    else -> "CPU TURN"
                },
                isPlayerTurn = game.currentPlayer == PieceColor.CYAN
            )

            Spacer(Modifier.height(10.dp))

            // THE BOARD
            CheckersBoard(game = game, onCellClick = {
                game.select(it.row, it.col)
                if (game.validMoves.any { m -> m.to == it }) {
                    game.tryMove(it)
                }
                tick++
            })

            Spacer(Modifier.height(10.dp))

            // Player HUD
            PlayerHud(
                activePieces = game.piecesOf(PieceColor.CYAN),
                captured = game.capturedByRed
            )

            Spacer(Modifier.height(12.dp))

            // Action deck
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ArcadeActionButton("UNDO", R.drawable.ic_undo, SurfaceContainerHigh, OnSurface, PrimaryContainer, Modifier.weight(1f)) {
                    game.undo(); tick++
                }
                ArcadeActionButton("HINT", R.drawable.ic_lightbulb, SurfaceContainerHigh, TertiaryContainer, TertiaryContainer, Modifier.weight(1f)) {
                    showHint = !showHint
                }
                ArcadeActionButton("RESIGN", R.drawable.ic_flag, ErrorContainer, OnErrorContainer, ErrorContainer, Modifier.weight(1f)) {
                    game.reset(); tick++
                }
                ArcadeActionButton("PAUSE", R.drawable.ic_pause, PrimaryContainer, OnPrimaryContainer, PrimaryContainer, Modifier.weight(1f)) {
                    onBack()
                }
            }

            // Winner overlay
            if (game.winner != null) {
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SynthCharcoal),
                    border = androidx.compose.foundation.BorderStroke(2.dp, PrimaryContainer)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(painterResource(R.drawable.ic_military_tech), null, tint = TertiaryContainer, modifier = Modifier.size(40.dp))
                        Spacer(Modifier.height(8.dp))
                        Text(
                            if (game.winner == PieceColor.CYAN) "VICTORY!" else "DEFEAT",
                            color = if (game.winner == PieceColor.CYAN) PrimaryContainer else Error,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { game.reset(); tick++ },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer)
                        ) {
                            Text("PLAY AGAIN", color = DeepVoid, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun OpponentHud(activePieces: Int, captured: Int, thinking: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(SurfaceContainerLowest),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_smart_toy), null, tint = SecondaryContainer, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ROBO-KING v2.4", color = OnSurface, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.width(6.dp))
                    ArcadeChip("HARD", containerColor = ErrorContainer, contentColor = OnErrorContainer)
                }
                if (thinking) {
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_sync), null, tint = Secondary, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("THINKING...", color = Secondary, style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(SurfaceContainerLowest).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("CAPTURED: ", color = Outline, style = MaterialTheme.typography.labelSmall)
                    repeat(captured.coerceAtMost(6)) {
                        Spacer(Modifier.width(2.dp))
                        Box(Modifier.size(8.dp).clip(CircleShape).background(PrimaryContainer))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "ACTIVE PIECES: $activePieces",
                    color = Secondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun MatchBanner(turnLabel: String, isPlayerTurn: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SCORE", color = Outline, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
                Text("1,480", color = TertiaryContainer, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isPlayerTurn) PrimaryContainer.copy(alpha = 0.15f) else SurfaceContainerHighest)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(if (isPlayerTurn) PrimaryContainer else Outline))
                Spacer(Modifier.width(6.dp))
                Text(
                    turnLabel,
                    color = if (isPlayerTurn) Primary else OnSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 2.sp
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SurfaceContainerHighest).padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(painterResource(R.drawable.ic_timer), null, tint = Outline, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("03:45", color = OnSurface, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun CheckersBoard(game: CheckersGame, onCellClick: (Cell) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLowest)
            .padding(8.dp)
    ) {
        // Neon frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerHighest)
                .padding(4.dp)
        ) {
            Column(Modifier.fillMaxSize()) {
                for (row in 0 until 8) {
                    Row(Modifier.weight(1f)) {
                        for (col in 0 until 8) {
                            val isDark = (row + col) % 2 == 1
                            val piece = game.board[row][col]
                            val isSelected = game.selected?.row == row && game.selected?.col == col
                            val isTarget = game.validMoves.any { it.to.row == row && it.to.col == col }
                            val isCaptureTarget = game.validMoves.any { m -> m.to.row == row && m.to.col == col && m.isCapture }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(if (isDark) BoardDarkTile else BoardLightTile)
                                    .clickable { onCellClick(Cell(row, col)) },
                                contentAlignment = Alignment.Center
                            ) {
                                // Valid move indicator
                                if (isTarget && piece == null) {
                                    if (isCaptureTarget) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(TertiaryContainer.copy(alpha = 0.3f))
                                                .border(2.dp, TertiaryContainer, CircleShape)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(TertiaryContainer)
                                            )
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(PrimaryContainer)
                                        )
                                    }
                                }

                                // Selected glow
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(2.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, TertiaryContainer, CircleShape)
                                    )
                                }

                                // Piece
                                if (piece != null) {
                                    val baseColor = if (piece.color == PieceColor.CYAN) PrimaryContainer else SecondaryContainer
                                    val lipColor = if (piece.color == PieceColor.CYAN) OnPrimaryContainer else OnSecondaryContainer
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(3.dp)
                                            .shadow(6.dp, CircleShape)
                                            .clip(CircleShape)
                                            .background(baseColor)
                                            .border(1.dp, lipColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Inner ring
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp)
                                                .clip(CircleShape)
                                                .background(baseColor.copy(alpha = 0.6f))
                                                .border(1.dp, lipColor.copy(alpha = 0.4f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (piece.type == PieceType.KING) {
                                                Icon(
                                                    painterResource(R.drawable.ic_crown),
                                                    contentDescription = "King",
                                                    tint = TertiaryContainer,
                                                    modifier = Modifier.fillMaxSize(0.5f)
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(if (piece.color == PieceColor.CYAN) Primary else Secondary)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerHud(activePieces: Int, captured: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainer)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLowest)
                    .border(1.5.dp, PrimaryContainer.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_person), null, tint = PrimaryContainer, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("NeonKnight", color = Primary, style = MaterialTheme.typography.headlineSmall)
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArcadeChip("x2 COMBO!", containerColor = TertiaryContainer, contentColor = OnTertiary)
                    Spacer(Modifier.width(6.dp))
                    Text("(+250 PTS)", color = OnSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(SurfaceContainerLowest).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("CAPTURED: ", color = Outline, style = MaterialTheme.typography.labelSmall)
                    repeat(captured.coerceAtMost(6)) {
                        Spacer(Modifier.width(2.dp))
                        Box(Modifier.size(8.dp).clip(CircleShape).background(SecondaryContainer))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "ACTIVE PIECES: $activePieces",
                    color = PrimaryContainer,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun ArcadeActionButton(
    label: String,
    iconRes: Int,
    bgColor: Color,
    textColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(iconRes), label, tint = accentColor, modifier = Modifier.size(18.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, color = textColor, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.sp)
    }
}

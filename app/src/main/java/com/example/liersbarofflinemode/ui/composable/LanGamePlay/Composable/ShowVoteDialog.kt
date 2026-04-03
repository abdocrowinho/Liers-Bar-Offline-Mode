package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf

@Composable
fun ShowBotVoteDialog(
    players: List<LanUserEntity?>,
    yesCount: Int,
    totalPlayers: Int,
    onYes: () -> Unit,
    onNo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF1A1A1A),
                disabledContentColor = Color.White
            ),
            modifier = Modifier
                .width(GetWidthConf() * 0.55f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF333333), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val slots = (0 until 4).map { index ->
                    players.getOrNull(index)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(GetHeightConf() * 0.25f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(slots) { player ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    Color(0xFF2A2A2A),
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    0.5.dp,
                                    Color(0xFF444444),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (player != null) {
                                AsyncImage(
                                    model = player.image,
                                    contentDescription = player.name,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            } else {
                                Text(
                                    text = "...",
                                    color = Color(0xFF666666),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                if (yesCount > 0) {
                    Text(
                        text = "$yesCount/$totalPlayers agreed",
                        color = Color(0xFF639922),
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Start the game with bots?",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onYes,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✓  Yes", color = Color.White)
                    }

                    Button(
                        onClick = onNo,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC62828)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✕  No", color = Color.White)
                    }
                }
            }
        }
    }
}
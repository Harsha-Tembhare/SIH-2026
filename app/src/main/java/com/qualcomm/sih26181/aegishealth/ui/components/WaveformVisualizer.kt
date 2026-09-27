package com.qualcomm.sih26181.aegishealth.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun WaveformVisualizer(
    points: List<Float>,
    signalQualityIndex: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(AegisSurface)
            .border(1.dp, AegisBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE PPG / ECG SIGNAL CANVASES",
                    style = Typography.labelSmall,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "SQI: ${String.format("%.1f", signalQualityIndex)}%",
                    style = Typography.labelSmall,
                    color = if (signalQualityIndex > 80) AegisEmerald else AegisAmber
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val width = size.width
                val height = size.height
                val midY = height / 2f

                // Draw Grid Lines (Medical Monitor Aesthetic)
                val gridSpacing = 20.dp.toPx()
                var xGrid = 0f
                while (xGrid < width) {
                    drawLine(
                        color = AegisSurfaceVariant,
                        start = Offset(xGrid, 0f),
                        end = Offset(xGrid, height),
                        strokeWidth = 1f
                    )
                    xGrid += gridSpacing
                }
                var yGrid = 0f
                while (yGrid < height) {
                    drawLine(
                        color = AegisSurfaceVariant,
                        start = Offset(0f, yGrid),
                        end = Offset(width, yGrid),
                        strokeWidth = 1f
                    )
                    yGrid += gridSpacing
                }

                // Draw Waveform Trace
                if (points.isNotEmpty()) {
                    val path = Path()
                    val dx = width / (points.size - 1).coerceAtLeast(1)
                    
                    points.forEachIndexed { index, sample ->
                        val x = index * dx
                        val y = midY - (sample * (height * 0.4f))
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }

                    drawPath(
                        path = path,
                        color = AegisCyan,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                }
            }
        }
    }
}

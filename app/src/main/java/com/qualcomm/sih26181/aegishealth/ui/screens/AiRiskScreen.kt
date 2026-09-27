package com.qualcomm.sih26181.aegishealth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qualcomm.sih26181.aegishealth.domain.model.AiRiskAssessment
import com.qualcomm.sih26181.aegishealth.domain.model.PersonalBaseline
import com.qualcomm.sih26181.aegishealth.ui.components.ExplainableFactorRow
import com.qualcomm.sih26181.aegishealth.ui.components.RiskScoreGauge
import com.qualcomm.sih26181.aegishealth.ui.theme.*

@Composable
fun AiRiskScreen(
    assessment: AiRiskAssessment,
    baseline: PersonalBaseline,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AegisBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "EXPLAINABLE AI ENGINE (XAI)",
                style = Typography.labelSmall,
                color = AegisCyan,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Qualcomm Edge Risk Fusion",
                style = Typography.titleLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.Bold
            )
        }

        item {
            RiskScoreGauge(assessment = assessment)
        }

        item {
            Text(
                text = "FEATURE IMPORTANCE & RISK FACTORS",
                style = Typography.labelSmall,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        if (assessment.explainableFactors.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AegisSurface, shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "All physiological vectors are within safe baseline bounds.",
                        style = Typography.bodyLarge,
                        color = AegisEmerald
                    )
                }
            }
        } else {
            items(assessment.explainableFactors) { factor ->
                ExplainableFactorRow(factor = factor)
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AegisSurface, shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Zero Cloud & Privacy Guarantee",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Inference is computed entirely on-device using quantized deterministic decision matrices. No biometric telemetry is ever uploaded to external cloud LLM services.",
                        style = Typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

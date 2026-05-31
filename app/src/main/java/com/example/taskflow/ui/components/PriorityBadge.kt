package com.example.taskflow.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.domain.model.Priority

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (priority) {
        Priority.ALTA  -> Triple(Color(0xFFFCEBEB), Color(0xFFA32D2D), "Alta")
        Priority.MEDIA -> Triple(Color(0xFFFAEEDA), Color(0xFF854F0B), "Media")
        Priority.BAJA  -> Triple(Color(0xFFEAF3DE), Color(0xFF3B6D11), "Baja")
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(99.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}


package com.example.taskflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Task

@Composable
fun TaskList(
    tasks: List<Task>,
    onToggle: (Int) -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tasks.isEmpty()) {
        Text(
            text = "No hay tareas aquí todavía 🎉",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(
                items = tasks,
                key = { task -> task.id }
            ) { task ->
                TaskCard(
                    task = task,
                    onToggle = onToggle,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }
}
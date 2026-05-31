package com.example.taskflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskflow.domain.model.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TaskCard(
    task: Task,
    onToggle: (Int) -> Unit,
    onEdit: (Task) -> Unit,
    onDelete: (Int) -> Unit,
    onClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverdue = task.dueDate != null && !task.isDone && task.dueDate.isBefore(LocalDate.now())

    Card(
        onClick = { onClick(task) },
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox — toggle de completado
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle(task.id) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Info de la tarea
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
                    color = if (task.isDone)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    // Badge de categoría
                    Text(
                        text = task.category.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Badge de prioridad — SRP: delega al PriorityBadge
                    PriorityBadge(priority = task.priority)

                    // Fecha de vencimiento
                    task.dueDate?.let { date ->
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            fontSize = 11.sp,
                            color = if (isOverdue)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            IconButton(
                onClick = { onEdit(task) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tarea",
                    modifier = Modifier.size(18.dp)
                )
            }

            IconButton(
                onClick = { onDelete(task.id) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
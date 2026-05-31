package com.example.taskflow.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    taskToEdit: Task? = null,
    onConfirm: (title: String, category: Category, priority: Priority, dueDate: LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    // Estado del formulario — SRP: solo maneja estado, delega UI a TaskForm
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(taskToEdit?.category ?: Category.TRABAJO) }
    var selectedPriority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIA) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(taskToEdit?.dueDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (taskToEdit != null) "Editar tarea" else "Nueva tarea",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            // OCP: si mañana quieres un formulario diferente, solo cambias TaskForm
            TaskForm(
                title = title,
                onTitleChange = { title = it },
                selectedCategory = selectedCategory,
                onCategoryChange = { selectedCategory = it },
                selectedPriority = selectedPriority,
                onPriorityChange = { selectedPriority = it },
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedCategory, selectedPriority, selectedDate)
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
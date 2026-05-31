package com.example.taskflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    taskToEdit: Task? = null,
    onConfirm: (title: String, category: Category, priority: Priority, dueDate: LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(taskToEdit?.category ?: Category.TRABAJO) }
    var selectedPriority by remember { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIA) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(taskToEdit?.dueDate) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    // Estado del DatePicker con la fecha actual como inicial
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            ?.atStartOfDay(ZoneId.of("UTC"))
            ?.toInstant()
            ?.toEpochMilli()
    )

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Convertimos milisegundos a LocalDate
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (taskToEdit != null) "Editar tarea" else "Nueva tarea",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // Campo título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Dropdown Categoría
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedCategory.name
                                .lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            Category.entries.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            category.name
                                                .lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = priorityExpanded,
                        onExpandedChange = { priorityExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = selectedPriority.name
                                .lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Prioridad") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = priorityExpanded,
                            onDismissRequest = { priorityExpanded = false }
                        ) {
                            Priority.entries.forEach { priority ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            priority.name
                                                .lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedPriority = priority
                                        priorityExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = selectedDate?.format(formatter) ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de vencimiento") },
                    placeholder = { Text("Selecciona una fecha") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedDate != null) {
                    TextButton(
                        onClick = { selectedDate = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Quitar fecha",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, selectedCategory, selectedPriority, selectedDate)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
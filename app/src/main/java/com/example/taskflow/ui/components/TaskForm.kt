package com.example.taskflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedCategory: Category,
    onCategoryChange: (Category) -> Unit,
    selectedPriority: Priority,
    onPriorityChange: (Priority) -> Unit,
    selectedDate: LocalDate?,
    onDateChange: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Campo título — SRP: delega a OutlinedTextField
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Título") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Descripción") },
            placeholder = { Text("Opcional...") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // SRP: delega a CategoryDropdown
            CategoryDropdown(
                selected = selectedCategory,
                onSelected = onCategoryChange,
                modifier = Modifier.weight(1f)
            )

            // SRP: delega a PriorityDropdown
            PriorityDropdown(
                selected = selectedPriority,
                onSelected = onPriorityChange,
                modifier = Modifier.weight(1f)
            )
        }

        // SRP: delega a DatePickerField
        DatePickerField(
            selectedDate = selectedDate,
            onDateSelected = onDateChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
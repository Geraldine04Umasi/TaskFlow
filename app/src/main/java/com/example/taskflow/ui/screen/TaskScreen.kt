package com.example.taskflow.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.domain.model.Task
import com.example.taskflow.ui.components.AddTaskDialog
import com.example.taskflow.ui.components.CategoryFilter
import com.example.taskflow.ui.components.TaskList
import com.example.taskflow.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TaskFlow",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    taskToEdit = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar tarea"
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Barra de búsqueda — SRP: delega al ViewModel el filtrado
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Buscar tareas...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filtro de categorías — ISP: solo recibe lo que necesita
            CategoryFilter(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.onCategorySelected(it) }
            )

            // Lista de tareas — LSP: TaskCard es intercambiable
            TaskList(
                tasks = uiState.tasks,
                onToggle = { viewModel.toggleDone(it) },
                onEdit = { task ->
                    taskToEdit = task
                    showDialog = true
                },
                onDelete = { viewModel.deleteTask(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }

        // Dialog de agregar/editar — SRP: el dialog es responsable solo del formulario
        if (showDialog) {
            AddTaskDialog(
                taskToEdit = taskToEdit,
                onConfirm = { title, description, category, priority, dueDate ->
                    if (taskToEdit != null) {
                        viewModel.updateTask(
                            taskToEdit!!.copy(
                                title = title,
                                description = description,
                                category = category,
                                priority = priority,
                                dueDate = dueDate
                            )
                        )
                    } else {
                        viewModel.addTask(title, description,category, priority, dueDate)
                    }
                    showDialog = false
                    taskToEdit = null
                },
                onDismiss = {
                    showDialog = false
                    taskToEdit = null
                }
            )
        }
    }
}


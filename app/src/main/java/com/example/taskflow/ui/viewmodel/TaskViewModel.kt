package com.example.taskflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.di.AppModule
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task
import com.example.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedPriority: Priority? = null,
    val searchQuery: String = ""
)

class TaskViewModel(
    private val repository: TaskRepository = AppModule.taskRepository
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    private val _selectedPriority = MutableStateFlow<Priority?>(null)
    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<TaskUiState> = combine(
        repository.getTasks(),
        _selectedCategory,
        _selectedPriority,
        _searchQuery
    ) { tasks, category, priority, query ->
        TaskUiState(
            tasks = tasks
                .filter { task ->
                    category == null || task.category == category
                }
                .filter { task ->
                    priority == null || task.priority == priority
                }
                .filter { task ->
                    query.isBlank() || task.title.contains(query, ignoreCase = true)
                },
            selectedCategory = category,
            selectedPriority = priority,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskUiState()
    )

    // — Eventos de filtros —

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
    }

    fun onPrioritySelected(priority: Priority?) {
        _selectedPriority.value = priority
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // — Eventos de tareas —

    fun addTask(
        title: String,
        description: String,
        category: Category,
        priority: Priority,
        dueDate: LocalDate?
    ) {
        viewModelScope.launch {
            repository.addTask(
                Task(
                    id = 0, // el repositorio asigna el id real
                    title = title,
                    description = description,
                    category = category,
                    priority = priority,
                    dueDate = dueDate
                )
            )
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun toggleDone(taskId: Int) {
        viewModelScope.launch {
            repository.toggleDone(taskId)
        }
    }
}


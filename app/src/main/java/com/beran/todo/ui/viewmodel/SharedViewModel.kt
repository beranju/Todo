package com.beran.todo.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.todo.data.models.Priority
import com.beran.todo.data.models.TodoTask
import com.beran.todo.data.repository.DataStoreRepository
import com.beran.todo.data.repository.TodoRepository
import com.beran.todo.utils.Action
import com.beran.todo.utils.Constant.MAX_TITLE_LENGTH
import com.beran.todo.utils.RequestState
import com.beran.todo.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    var id by mutableStateOf(0)
        private set

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var priority by mutableStateOf(Priority.Low)
        private set

    val lowPriorityTasks: StateFlow<List<TodoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val highPriorityTasks: StateFlow<List<TodoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set
    var searchTextState by mutableStateOf("")
        private set

    private val _sortState: MutableStateFlow<RequestState<Priority>> =
        MutableStateFlow(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> get() = _sortState

    private val _searchTask = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val searchTask: StateFlow<RequestState<List<TodoTask>>> = _searchTask

    private val _allTasks = MutableStateFlow<RequestState<List<TodoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<TodoTask>>> = _allTasks

    private val _selectedTask = MutableStateFlow<TodoTask?>(null)
    val selectedTask: StateFlow<TodoTask?> = _selectedTask

    init {
        getAllTasks()
        readSortState()
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTask.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e.message ?: "Unknown error")
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase("%$searchQuery%").collect { searchTasks ->
                    _searchTask.value = RequestState.Success(searchTasks)
                }
            }
        } catch (e: Exception) {
            _searchTask.value = RequestState.Error(e.message ?: "Unknown error")
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(todoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(todoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val todoTask = TodoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(todoTask)
        }
    }

    private fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun handleDatabaseAction(action: Action) {
        when (action) {
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTask()
            Action.UNDO -> addTask()
            else -> {}
        }
    }

    fun updateTask(todoTask: TodoTask?) {
        if (todoTask != null) {
            id = todoTask.id
            title = todoTask.title
            description = todoTask.description
            priority = todoTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.Low
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }
    }

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState.map { Priority.valueOf(it) }.collect {
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e.message ?: "Unknown error")
        }
    }

    fun updateAction(action: Action){
        this.action = action
    }

    fun updateId(id: Int){
        this.id = id
    }

    fun updateDesc(desc: String){
        this.description = desc
    }

    fun updatePriority(priority: Priority){
        this.priority = priority
    }

    fun updateSearchAppBarState(state: SearchAppBarState){
        this.searchAppBarState = state
    }

    fun updateSearchTextState(state: String){
        this.searchTextState = state
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
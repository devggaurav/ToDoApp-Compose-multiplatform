package presentation.addEditTodo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.Todo
import data.TodoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ui.UiEvent


//
// Created by Code For Android on 24/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class AddEditTodoViewModel(
    private val todoRepository: TodoRepository,
) : ScreenModel {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun loadTodo(todo: Todo?) {
        screenModelScope.launch {
            todo?.let {
                this@AddEditTodoViewModel.todo = todoRepository.getTodoById(todo._id!!)
                title = todo?.title ?: ""
                description = todo?.description ?: ""
            }


        }

    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {

            is AddEditTodoEvent.OnTitleChange -> {

                title = event.title

            }

            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddEditTodoEvent.OnSaveTodoClick -> {

                screenModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackBar(
                                message = "The title can't be empty"
                            )
                        )
                        return@launch
                    }
                    todoRepository.insertTodo(
                        Todo().apply {
                            title = title
                            description = description
                            isDone = todo?.isDone ?: false
                            _id = todo?._id

                        }
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }

            }


        }


    }


    private fun sendUiEvent(event: UiEvent) {
        screenModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
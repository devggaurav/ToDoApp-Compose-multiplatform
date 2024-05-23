package presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.Todo
import data.TodoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import presentation.todoList.TodoListEvent
import ui.Routes
import ui.UiEvent


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class HomeScreenViewModel(
    private val todoRepository: TodoRepository
) : ScreenModel {


    val todos = todoRepository.getTodos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private var deleteTodo: Todo? = null


    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))

            }

            is TodoListEvent.OnDeleteTodoClick -> {
                screenModelScope.launch {
                    deleteTodo = event.todo
                    todoRepository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = "Todo deleted",
                            action = "Undo"
                        )
                    )


                }
            }

            is TodoListEvent.OnDoneChange -> {
                screenModelScope.launch {
                    todoRepository.insertTodo(
                        Todo().apply {
                            title = event.todo.title
                            description = event.todo.description
                            isDone = event.isDone
                        }


                    )
                }

            }

            is TodoListEvent.OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo._id}"))
            }

            TodoListEvent.OnUndoDeleteClick -> {
                deleteTodo?.let { todo ->
                    screenModelScope.launch {
                        todoRepository.insertTodo(todo)
                    }
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
package presentation.todoList

import data.Todo


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

sealed class TodoListEvent {

    data class OnDeleteTodoClick(val todo: Todo) : TodoListEvent()

    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : TodoListEvent()
    data object OnUndoDeleteClick : TodoListEvent()

    data class OnTodoClick(val todo: Todo) : TodoListEvent()

    data object OnAddTodoClick : TodoListEvent()


}
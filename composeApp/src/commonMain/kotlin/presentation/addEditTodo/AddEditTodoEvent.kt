package presentation.addEditTodo


//
// Created by Code For Android on 24/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

sealed class AddEditTodoEvent {
    data class OnTitleChange(val title: String) : AddEditTodoEvent()
    data class OnDescriptionChange(val description: String) : AddEditTodoEvent()
    object OnSaveTodoClick : AddEditTodoEvent()

}
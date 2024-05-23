package presentation

import cafe.adriel.voyager.core.model.ScreenModel
import data.Todo
import data.TodoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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





}
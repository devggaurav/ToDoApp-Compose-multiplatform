package ui

import data.Todo


//
// Created by Code For Android on 23/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

sealed class UiEvent {

    data object PopBackStack : UiEvent()
    data class Navigate(val id: Todo?) : UiEvent()

    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()


}
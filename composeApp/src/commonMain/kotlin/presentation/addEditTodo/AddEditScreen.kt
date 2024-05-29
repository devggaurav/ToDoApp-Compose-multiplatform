package presentation.addEditTodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Todo
import ui.UiEvent
import ui.theme.onPrimaryContainerLight
import ui.theme.primaryContainerLight


//
// Created by Code For Android on 24/05/24.
// Copyright (c) 2024 CFA. All rights reserved.
//

class AddEditScreen(private val todo: Todo? = null) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val addEditViewModel = getScreenModel<AddEditTodoViewModel>()
        addEditViewModel.loadTodo(todo)

        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {

            addEditViewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.PopBackStack -> {
                        navigator.pop()
                    }

                    is UiEvent.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action
                        )


                    }

                    else -> Unit
                }


            }


        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { addEditViewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = { Text(text = if (todo == null) "Add Todo" else "Edit Todo") },
                    colors = TopAppBarColors(
                        containerColor = onPrimaryContainerLight,
                        titleContentColor = primaryContainerLight,
                        actionIconContentColor = primaryContainerLight,
                        navigationIconContentColor = primaryContainerLight,
                        scrolledContainerColor = onPrimaryContainerLight
                    )
                )
            },
            containerColor = onPrimaryContainerLight
        ) {


            Column(
                modifier = Modifier.fillMaxSize().padding(it).imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val firstFocusRequester = remember { FocusRequester() }
                val secondFocusRequester = remember { FocusRequester() }

                TextField(
                    value = addEditViewModel.title,
                    onValueChange = { addEditViewModel.onEvent(AddEditTodoEvent.OnTitleChange(it)) },
                    placeholder = {
                        Text(text = "Title")
                    },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp).focusRequester(firstFocusRequester),
                    keyboardOptions =  KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            secondFocusRequester.requestFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = addEditViewModel.description,
                    onValueChange = {
                        addEditViewModel.onEvent(AddEditTodoEvent.OnDescriptionChange(it))
                    },
                    placeholder = {
                        Text(text = "Description")
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp).focusRequester(secondFocusRequester),
                    singleLine = false,
                    maxLines = 5,
                    keyboardOptions =  KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            addEditViewModel.onEvent(AddEditTodoEvent.OnSaveTodoClick)
                        }
                    )
                )


            }


        }


    }
}
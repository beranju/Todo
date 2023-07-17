package com.beran.todo.ui.screen.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.beran.todo.R
import com.beran.todo.component.DisplayAlertDialog
import com.beran.todo.component.PriorityItem
import com.beran.todo.data.models.Priority
import com.beran.todo.ui.theme.LARGE_PADDING
import com.beran.todo.ui.theme.SMALL_PADDING
import com.beran.todo.ui.theme.TOP_APP_BAR_HIGH
import com.beran.todo.ui.theme.TodoTheme
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Action
import com.beran.todo.utils.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.updateSearchAppBarState(SearchAppBarState.OPENED)
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(it)
                },
                onDeleteAllConfirmed = {
                    sharedViewModel.updateAction(Action.DELETE_ALL)
                }
            )
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { sharedViewModel.updateSearchTextState(it) },
                onCloseClicked = {
                    sharedViewModel.updateSearchAppBarState(SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchTextState("")
                },
                onSearchClicked = {
                    sharedViewModel.searchDatabase(it)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.task_txt),
                color = MaterialTheme.colorScheme.background
            )
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
//        scrolledContainerColor = MaterialTheme.colorScheme.applyTonalElevation(
//            backgroundColor = containerColor,
//        elevation = TopAppBarSmallTokens.OnScrollContainerElevation
//        )
        )
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(R.string.remove_all_tasks),
        message = stringResource(R.string.delete_all_task_confirmation),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = {
        openDialog = true
    })

}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_task_hint),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.Sort,
            contentDescription = stringResource(R.string.search_task_hint),
            tint = MaterialTheme.colorScheme.background
        )
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        Priority.values().slice(setOf(0, 2, 3)).forEach { priority ->
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = priority)
                },
                onClick = {
                    onSortClicked(priority)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.delete_all_task_hint),
            tint = MaterialTheme.colorScheme.background
        )
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.delete_all_txt),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(LARGE_PADDING)
                )
            },
            onClick = {
                onDeleteAllConfirmed()
                expanded = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = SMALL_PADDING,
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HIGH)
    ) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.search_txt),
                    color = MaterialTheme.colorScheme.background
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.background,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}, modifier = Modifier.alpha(1.0f)) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_task_hint),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.search_task_hint),
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchClicked(text)
            }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultListAppBarPrev() {
    TodoTheme {
        DefaultListAppBar(onSearchClicked = {}, onSortClicked = {}, onDeleteAllConfirmed = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchAppBarPrev() {
    TodoTheme {
        SearchAppBar(
            text = "",
            onTextChange = {},
            onSearchClicked = {},
            onCloseClicked = {}
        )
    }
}
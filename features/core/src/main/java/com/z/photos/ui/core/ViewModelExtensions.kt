package com.z.photos.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.launchOnIO(block: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) { block() }
}

fun ViewModel.launchOnMain(block: suspend () -> Unit) {
    viewModelScope.launch { block() }
}

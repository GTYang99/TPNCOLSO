package com.example.tp_ncolso_android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner

@Composable
inline fun <reified VM : ViewModel> rememberDebugViewModel(
    key: String,
    noinline createViewModel: (SavedStateHandle) -> VM,
): VM {
    val storeOwner = LocalViewModelStoreOwner.current as? ViewModelStoreOwner
        ?: error("此畫面需要 ViewModelStoreOwner")
    val stateOwner = storeOwner as? SavedStateRegistryOwner
        ?: error("此畫面需要 SavedStateRegistryOwner")
    return remember(storeOwner, key) {
        ViewModelProvider(
            storeOwner,
            object : AbstractSavedStateViewModelFactory(stateOwner, null) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle,
                ): T = createViewModel(handle) as T
            },
        ).get(key, VM::class.java)
    }
}

package com.example.tp_ncolso_android.feature.notifications

import com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStore
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationsViewModelTest {
    @Test
    fun loadsReturnedRowsAndRoutesSelectionByStableParcelKey() = runBlocking {
        val viewModel = NotificationsViewModel(FakeReturnFlowStore())
        val keyNo = viewModel.state.value.items.last().keyNo
        val effect = async { viewModel.effect.first() }

        assertEquals(6, viewModel.state.value.items.size)
        viewModel.onEvent(NotificationsEvent.SelectParcel(keyNo))

        assertEquals(NotificationsEffect.ParcelSelected(keyNo), effect.await())
    }
}

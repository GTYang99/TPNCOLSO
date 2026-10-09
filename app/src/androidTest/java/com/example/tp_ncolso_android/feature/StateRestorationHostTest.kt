package com.example.tp_ncolso_android.feature

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tp_ncolso_android.Ui009StateRestoreActivity
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormField
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StateRestorationHostTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<Ui009StateRestoreActivity>()

    @Test
    fun surveyUiFlowKeepsDraftAndRemainsUsableAfterActivityRecreation() {
        composeRule.activity.surveyViewModel.onEvent(
            SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "Activity 重建後保留"),
        )
        composeRule.waitForIdle()
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        assertEquals("Activity 重建後保留", composeRule.activity.surveyViewModel.state.value.values.note)
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-form-dirty").assertExists()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
        composeRule.onNodeWithText("繼續編輯").performClick()
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
    }
}

package com.example.tp_ncolso_android.feature

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tp_ncolso_android.Ui009StatefulFlowTestActivity
import org.junit.Rule
import org.junit.Test

class PhotoCameraUiFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<Ui009StatefulFlowTestActivity>()

    @Test
    fun surveyCameraFlowSurvivesActivityRecreationAndReturnsToSurvey() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.uiAutomation.grantRuntimePermission(
            instrumentation.targetContext.packageName,
            Manifest.permission.CAMERA,
        )

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-002")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-open-detail").performScrollTo().performClick()
        composeRule.onNodeWithText("編輯調查").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()

        composeRule.onNodeWithTag("survey-camera").performScrollTo().performClick()
        composeRule.onNodeWithTag("photo-camera-screen").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("取消拍攝").assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("photo-camera-screen").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("取消拍攝").performClick()
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
    }
}

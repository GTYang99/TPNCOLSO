package com.example.tp_ncolso_android.feature.notifications.data

import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormValues
import com.example.tp_ncolso_android.feature.surveyform.SurveyPhotoAspect
import com.example.tp_ncolso_android.feature.surveyform.data.LocalSurveySubmitResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeReturnFlowStoreTest {
    @Test
    fun exposesSixReturnedFixturesWithMatchingDetailAndFormContext() {
        val store = FakeReturnFlowStore()
        val notifications = store.returnedNotifications.value

        assertEquals(6, notifications.size)
        notifications.forEach { notification ->
            val detail = store.findDetail(notification.keyNo)
            val form = store.load(notification.keyNo)

            assertNotNull(detail)
            assertEquals(notification.keyNo, detail?.keyNo)
            assertEquals("退回", detail?.status)
            assertEquals(notification.reasonCategory, detail?.returnReason?.category)
            assertEquals(notification.reasonPreview, detail?.returnReason?.explanation)
            assertNotNull(form?.returnContext)
            assertEquals(notification.reasonCategory, form?.returnContext?.category)
            assertEquals(notification.reasonPreview, form?.returnContext?.explanation)
        }
    }

    @Test
    fun savedCorrectionKeepsReturnedStatusUntilExplicitConfirmation() {
        val store = FakeReturnFlowStore()
        val keyNo = store.returnedNotifications.value.first().keyNo
        val seed = requireNotNull(store.load(keyNo))
        val correctedValues = seed.values.copy(note = "已補充的現場說明")
        val correctedPhotos = seed.photos + SurveyFormPhoto(
            id = "correction-photo",
            uri = "local://correction-photo",
            capturedAt = "now",
            aspect = SurveyPhotoAspect.FOUR_BY_THREE,
        )

        assertEquals(LocalSurveySubmitResult.Success, store.submit(keyNo, correctedValues, correctedPhotos))

        val savedDetail = requireNotNull(store.findDetail(keyNo))
        val savedForm = requireNotNull(store.load(keyNo))
        assertEquals("退回", savedDetail.status)
        assertTrue(savedDetail.correctionSaved)
        assertEquals("已補充的現場說明", savedDetail.surveyRecords.first().note)
        assertEquals(correctedValues, savedForm.values)
        assertEquals(correctedPhotos, savedForm.photos)
        assertEquals(6, store.returnedNotifications.value.size)
    }

    @Test
    fun confirmationChangesStatusRemovesReturnContextAndHidesNotification() {
        val store = FakeReturnFlowStore()
        val keyNo = store.returnedNotifications.value.first().keyNo
        val seed = requireNotNull(store.load(keyNo))
        val photos = seed.photos + SurveyFormPhoto("correction-photo", "local://correction-photo", "now")

        assertFalse(store.confirmCorrection(keyNo))
        assertEquals(LocalSurveySubmitResult.Success, store.submit(keyNo, seed.values, photos))
        assertTrue(store.confirmCorrection(keyNo))

        val correctedDetail = requireNotNull(store.findDetail(keyNo))
        assertEquals("已修正", correctedDetail.status)
        assertNull(correctedDetail.returnReason)
        assertNull(correctedDetail.editActionLabel)
        assertFalse(correctedDetail.correctionSaved)
        assertEquals(5, store.returnedNotifications.value.size)
        assertTrue(store.returnedNotifications.value.none { it.keyNo == keyNo })
    }
}

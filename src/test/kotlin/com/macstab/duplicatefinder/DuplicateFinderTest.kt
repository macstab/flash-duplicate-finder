package com.macstab.duplicatefinder

import com.macstab.duplicatefinder.options.DuplicateOptions
import com.macstab.duplicatefinder.service.DeletionProcessState
import com.macstab.duplicatefinder.service.FileDuplicateFinder
import com.macstab.duplicatefinder.service.FileDuplicateHandler
import com.macstab.duplicatefinder.service.FileDuplicateReporter
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DuplicateFinderTest {
    private lateinit var duplicateFinder: DuplicateFinder
    private lateinit var options: DuplicateOptions
    private lateinit var fileDuplicateFinder: FileDuplicateFinder
    private lateinit var fileDuplicateReporter: FileDuplicateReporter
    private lateinit var fileDuplicateHandler: FileDuplicateHandler

    @Before
    fun setup() {
        options = mockk(relaxed = true)
        fileDuplicateFinder = mockk(relaxed = true)
        fileDuplicateReporter = mockk(relaxed = true)
        fileDuplicateHandler = mockk(relaxed = true)
        duplicateFinder = DuplicateFinder(options, fileDuplicateFinder, fileDuplicateReporter, fileDuplicateHandler)
    }

    @Test
    fun `should start application successfully`() {
        val args = arrayOf("arg1", "arg2")
        val options = mockk<DuplicateOptions>()
        val deletionProcessState = mockk<DeletionProcessState>()

        every { fileDuplicateFinder.lookupForDuplicates(any<DuplicateOptions>()) } returns deletionProcessState

        duplicateFinder.startApplication(args)

        verify { options.wasNot(Called) }
        verify { fileDuplicateFinder.lookupForDuplicates(any<DuplicateOptions>()) }
        verify { fileDuplicateReporter.reportDuplicates(deletionProcessState, any<DuplicateOptions>()) }
        verify { fileDuplicateHandler.processDuplicates(deletionProcessState, any<DuplicateOptions>()) }
    }

    @Test
    fun `should handle no arguments`() {
        val args = arrayOf<String>()
        val options = mockk<DuplicateOptions>()
        val deletionProcessState = mockk<DeletionProcessState>()

        every { fileDuplicateFinder.lookupForDuplicates(any<DuplicateOptions>()) } returns deletionProcessState

        duplicateFinder.startApplication(args)

        verify { options.wasNot(Called) }
        verify { fileDuplicateFinder.lookupForDuplicates(any<DuplicateOptions>()) }
        verify { fileDuplicateReporter.reportDuplicates(deletionProcessState, any<DuplicateOptions>()) }
        verify { fileDuplicateHandler.processDuplicates(deletionProcessState, any<DuplicateOptions>()) }
    }
}

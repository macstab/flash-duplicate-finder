package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.options.DuplicateOptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class FileDuplicateFinderTest {
    internal val options = mockk<DuplicateOptions>()
    internal val deletionProcessState = DeletionProcessState(options)
    internal val fileDuplicateFinder = FileDuplicateFinder()

    @Test
    fun `should lookup for duplicates when directory contains duplicate files`() {
        every { options.path } returns "src/test/resources/duplicates"
        every { options.suppressVerbose } returns false
        every { options.recursive } returns true

        fileDuplicateFinder.lookupForDuplicates(options, deletionProcessState)

        assertEquals(1, deletionProcessState.sizeFileMap.size)
        assertEquals(2, deletionProcessState.filenameFileMap.size)
        assertEquals(1, deletionProcessState.checksumFileMap.size)
    }

    @Test
    fun `should not lookup for duplicates when directory does not contain duplicate files`() {
        every { options.path } returns "src/test/resources/no-duplicates"
        every { options.suppressVerbose } returns false

        fileDuplicateFinder.lookupForDuplicates(options, deletionProcessState)

        assertEquals(2, deletionProcessState.sizeFileMap.size)
        assertEquals(2, deletionProcessState.filenameFileMap.size)
        assertEquals(2, deletionProcessState.checksumFileMap.size)
    }

    @Test
    fun `should not lookup for duplicates when directory is empty`() {
        every { options.path } returns "src/test/resources/empty"
        every { options.suppressVerbose } returns false
        val file = File("src/test/resources/empty")
        if (!file.exists()) file.mkdir()

        fileDuplicateFinder.lookupForDuplicates(options, deletionProcessState)

        assertEquals(0, deletionProcessState.sizeFileMap.size)
        assertEquals(0, deletionProcessState.filenameFileMap.size)
        assertEquals(0, deletionProcessState.checksumFileMap.size)
    }

    @Test
    fun `should not lookup for duplicates when directory does not exist`() {
        every { options.path } returns "src/test/resources/non_existent"
        every { options.suppressVerbose } returns false

        fileDuplicateFinder.lookupForDuplicates(options, deletionProcessState)

        assertEquals(0, deletionProcessState.sizeFileMap.size)
        assertEquals(0, deletionProcessState.filenameFileMap.size)
        assertEquals(0, deletionProcessState.checksumFileMap.size)
    }
}
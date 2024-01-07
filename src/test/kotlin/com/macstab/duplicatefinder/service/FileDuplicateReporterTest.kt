package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.config.FileData
import com.macstab.duplicatefinder.options.DuplicateOptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class FileDuplicateReporterTest {

    private val deletionProcessState = mockk<DeletionProcessState>()
    private val options = mockk<DuplicateOptions>()
    private val fileDuplicateReporter = FileDuplicateReporter()

    @Test
    fun `should report duplicates when duplicate files exist`() {
        val fileData = FileData("testFile", 0, "checksum")
        every { options.suppressVerbose } returns false
        every { deletionProcessState.filenameFileMap } returns mutableMapOf("testFile" to fileData)
        fileData.duplicateFiles.add(FileData("testFile2", 0, "checksum"))
        try {
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            fileDuplicateReporter.reportDuplicates(deletionProcessState, options)

            assertEquals(outContent.toString().lines().size, 2)
        } finally {
            System.setOut(System.out)
        }
    }

    @Test
    fun `should not report duplicates when no duplicate files exist`() {
        val fileData = FileData("testFile", 0, "checksum")
        every { deletionProcessState.filenameFileMap } returns mutableMapOf("testFile" to fileData)
        every { options.suppressVerbose } returns false

        try {
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            fileDuplicateReporter.reportDuplicates(deletionProcessState, options)

            assertTrue(outContent.toString().isEmpty())
        } finally {
            System.setOut(System.out)
        }
    }

    @Test
    fun `should report duplicates for each file in filenameFileMap`() {
        val fileData1 = FileData("testFile1", 0, "checksum")
        val fileData2 = FileData("testFile2", 0, "checksum")
        every { deletionProcessState.filenameFileMap } returns mutableMapOf("testFile1" to fileData1, "testFile2" to fileData2)
        every { options.suppressVerbose } returns false
        fileData1.duplicateFiles.add(FileData("testFile1-1", 0, "checksum"))
        fileData1.duplicateFiles.add(FileData("testFile1-2", 0, "checksum"))
        fileData2.duplicateFiles.add(FileData("testFile2-2", 0, "checksum"))
        fileData2.duplicateFiles.add(FileData("testFile2-2", 0, "checksum"))

        try {
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            fileDuplicateReporter.reportDuplicates(deletionProcessState, options)

            assertEquals(outContent.toString().lines().size, 5)
        } finally {
            System.setOut(System.out)
        }
    }

    @Test
    fun `should not report duplicates when filenameFileMap is empty`() {
        every { deletionProcessState.filenameFileMap } returns mutableMapOf()
        every { options.suppressVerbose } returns false

        try {
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            fileDuplicateReporter.reportDuplicates(deletionProcessState, options)

            assertTrue(outContent.toString().isBlank())
        } finally {
            System.setOut(System.out)
        }
    }
}
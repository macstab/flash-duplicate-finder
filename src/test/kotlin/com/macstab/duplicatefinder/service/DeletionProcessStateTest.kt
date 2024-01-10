package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.options.DuplicateOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class DeletionProcessStateTest {
    @Test
    fun `should process unprocessed file`() {
        val options = DuplicateOptions()
        val deletionProcessState = DeletionProcessState(options)
        val file = File("testFile")

        assertTrue(deletionProcessState.fileNotProcessed(file))
    }

    @Test
    fun `should not process processed file`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("testFile")
            file.createNewFile()
            deletionProcessState.addNewFileToStates(file)
            assertFalse(deletionProcessState.fileNotProcessed(file))
        } finally {
            File("testFile").delete()
        }
    }

    @Test
    fun `should add file to states`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("testFile")
            file.createNewFile()
            deletionProcessState.addNewFileToStates(file)

            assertTrue(deletionProcessState.filenameFileMap.containsKey(file.absolutePath))
        } finally {
            File("testFile").delete()
        }
    }

    @Test
    fun `should not add duplicate file to states`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("testFile")
            file.createNewFile()

            deletionProcessState.addNewFileToStates(file)
            deletionProcessState.addNewFileToStates(file)

            assertEquals(1, deletionProcessState.filenameFileMap.count { it.key == file.absolutePath })
        } finally {
            File("testFile").delete()
        }
    }

    @Test
    fun `should not process non-existing file`() {
        val options = DuplicateOptions()
        val deletionProcessState = DeletionProcessState(options)
        val file = File("nonExistingFile")

        assertTrue(deletionProcessState.fileNotProcessed(file))
    }

    @Test
    fun `should process existing file`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("existingFile")
            file.createNewFile()
            deletionProcessState.addNewFileToStates(file)

            assertFalse(deletionProcessState.fileNotProcessed(file))
        } finally {
            File("existingFile").delete()
        }
    }

    @Test
    fun `should add unique files to states`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file1 = File("uniqueFile1")
            val file2 = File("uniqueFile2")
            file1.createNewFile()
            file2.createNewFile()

            deletionProcessState.addNewFileToStates(file1)
            deletionProcessState.addNewFileToStates(file2)

            assertTrue(deletionProcessState.filenameFileMap.containsKey(file1.absolutePath))
            assertTrue(deletionProcessState.filenameFileMap.containsKey(file2.absolutePath))
        } finally {
            File("uniqueFile1").delete()
            File("uniqueFile2").delete()
        }
    }

    @Test
    fun `should not add file to states if it is already processed`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("testFile")
            file.createNewFile()

            deletionProcessState.addNewFileToStates(file)
            deletionProcessState.addNewFileToStates(file)

            assertEquals(1, deletionProcessState.filenameFileMap.count { it.key == file.absolutePath })
        } finally {
            File("testFile").delete()
        }
    }

    @Test
    fun `should add file to states if it is not processed`() {
        try {
            val options = DuplicateOptions()
            val deletionProcessState = DeletionProcessState(options)
            val file = File("testFile")
            file.createNewFile()

            deletionProcessState.addNewFileToStates(file)

            assertTrue(deletionProcessState.filenameFileMap.containsKey(file.absolutePath))
        } finally {
            File("testFile").delete()
        }
    }
}

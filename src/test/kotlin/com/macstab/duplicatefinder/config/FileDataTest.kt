package com.macstab.duplicatefinder.config

import org.junit.Assert.*
import org.junit.Before
import kotlin.test.Test

class FileDataTest {

    private lateinit var fileData: FileData

    @Before
    fun setup() {
        fileData = FileData("fileName", 100L, "checkSum")
    }

    @Test
    fun `should add duplicate successfully`() {
        val duplicateFileData = FileData("duplicateFileName", 100L, "checkSum")
        fileData.addDuplicate(duplicateFileData)

        assertEquals(1, fileData.duplicateFiles.size)
        assertEquals(duplicateFileData, fileData.duplicateFiles[0])
    }

    @Test
    fun `should not add self as duplicate`() {
        fileData.addDuplicate(fileData)

        assertEquals(0, fileData.duplicateFiles.size)
    }

    @Test
    fun `should return true when files are equal`() {
        val equalFileData = FileData("fileName", 100L, "checkSum")

        assertEquals(fileData, equalFileData)
    }

    @Test
    fun `should return false when files are not equal`() {
        val notEqualFileData = FileData("notEqualFileName", 200L, "differentCheckSum")

        assertNotEquals(fileData, notEqualFileData)
    }

    @Test
    fun `should mark file as deleted`() {
        fileData.deleted = true

        assertTrue(fileData.deleted)
    }

    @Test
    fun `should not add duplicate if file is marked as deleted`() {
        fileData.deleted = true
        val duplicateFileData = FileData("duplicateFileName", 100L, "checkSum")
        fileData.addDuplicate(duplicateFileData)

        assertEquals(0, fileData.duplicateFiles.size)
    }
}
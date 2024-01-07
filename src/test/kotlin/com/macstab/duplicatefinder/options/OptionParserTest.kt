package com.macstab.duplicatefinder.options

import io.mockk.every
import io.mockk.mockk
import org.apache.commons.cli.CommandLine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class OptionParserTest {

    private lateinit var optionParser: OptionParser
    private lateinit var commandLine: CommandLine

    @Before
    fun setup() {
        optionParser = OptionParser()
        commandLine = mockk(relaxed = true)
    }

    @Test
    fun `should parse delete option`() {
        every { commandLine.hasOption("delete") } returns true

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertTrue(options.delete)
        assertFalse(options.recursive)
        assertFalse(options.suppressBackup)
        assertFalse(options.suppressVerbose)
        assertEquals("", options.backupPath)
        assertEquals("", options.path)
    }

    @Test
    fun `should parse recursive option`() {
        every { commandLine.hasOption("recursive") } returns true

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertTrue(options.recursive)
    }

    @Test
    fun `should parse suppressBackup option`() {
        every { commandLine.hasOption("suppressBackup") } returns true

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertTrue(options.suppressBackup)
        assertFalse(options.delete)
        assertFalse(options.suppressVerbose)
        assertEquals("", options.backupPath)
        assertEquals("", options.path)
    }

    @Test
    fun `should parse backupPath option`() {
        every { commandLine.getOptionValue("backupPath", "backupOfDeleted") } returns "customBackupPath"

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertEquals("customBackupPath", options.backupPath)
        assertFalse(options.delete)
        assertFalse(options.suppressBackup)
        assertFalse(options.suppressVerbose)
        assertEquals("", options.path)
    }

    @Test
    fun `should parse suppressVerbose option`() {
        every { commandLine.hasOption("suppressVerbose") } returns true

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertTrue(options.suppressVerbose)
        assertFalse(options.delete)
        assertFalse(options.suppressBackup)
        assertEquals("", options.backupPath)
        assertEquals("", options.path)
    }

    @Test
    fun `should parse path option`() {
        every { commandLine.getOptionValue("path", System.getProperty("user.dir")) } returns "customPath"

        val options = optionParser.generateDuplicateOptions(commandLine)

        assertEquals("customPath", options.path)
        assertFalse(options.delete)
        assertFalse(options.suppressBackup)
        assertFalse(options.suppressVerbose)
        assertEquals("", options.backupPath)
    }
}
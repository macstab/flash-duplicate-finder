package com.macstab.duplicatefinder.options

import com.macstab.duplicatefinder.service.VerboseHandler
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import kotlin.system.exitProcess

class OptionParser {
    fun parseArguments(args: Array<String>): DuplicateOptions {
        return parseCommandline(args)
    }

    @Throws(ParseException::class)
    fun parseCommandline(args: Array<String>?): DuplicateOptions {
        val options =
            Options().apply {
                addOption("d", "delete", false, "delete all duplicate files")
                addOption("r", "recursive", false, "scan all directories recursive")
                addOption("b", "suppressBackup", false, "suppress any backup's of the deleted files")
                addOption(
                    "o",
                    "backupPath",
                    true,
                    "the path, where the backups of the deletes files should be stored, string, default is backupOfDeleted",
                )
                addOption("v", "suppressVerbose", true, "do not output the current state of work to the console")
                addOption(
                    "p",
                    "path",
                    true,
                    "The parent entry path of the directory, that should be checked for duplicates. string, default is pwd",
                )
                addOption("h", "help", false, "print this help page")
            }

        val commandLine: CommandLine = DefaultParser().parse(options, args, true)

        if (commandLine.hasOption("help")) {
            HelpFormatter().printHelp("duplicateFinder", options)
            exitProcess(0)
        }

        if (commandLine.argList.isEmpty()) {
            VerboseHandler.printToConsole(
                "duplicateFinder: " +
                    "No arguments supplied. For a list of options provide --help. Will execute duplicate search only.",
                DuplicateOptions(false, false, false, "", false, ""),
            )
        }

        return generateDuplicateOptions(commandLine)
    }

    internal fun generateDuplicateOptions(commandLine: CommandLine): DuplicateOptions {
        return DuplicateOptions(
            commandLine.hasOption("delete").also { if (it) println("deletion of duplicates enabled") },
            commandLine.hasOption("recursive").also { if (it) println("recursive lookup of duplicates enabled") },
            commandLine.hasOption("suppressBackup").also { if (it) println("will not backup the deleted duplicates") },
            commandLine.getOptionValue("backupPath", "backupOfDeleted")
                .also { println("the duplicate backup path is set to $it") },
            commandLine.hasOption("suppressVerbose").also { if (it) println("verbose output is enabled") },
            commandLine.getOptionValue("path", System.getProperty("user.dir"))
                .also { println("the duplicate search path is set to $it") },
        )
    }
}

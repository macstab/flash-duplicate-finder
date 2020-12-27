package de.macstab.duplicatefinder.options

import de.macstab.duplicatefinder.service.VerboseHandler
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException

class OptionParser {

    fun parseArguments(args: Array<String>) : DuplicateOptions {
        return parseCommandline(args)
    }

    @Throws(ParseException::class)
    fun parseCommandline(args: Array<String>?): DuplicateOptions {
        val options = Options()
        options.addOption("d", "delete", false, "delete all duplicate files")
        options.addOption("r", "recursive", false, "scan all directories recursive")
        options.addOption("b", "suppressBackup", false, "suppress any backup's of the deleted files")
        options.addOption("o", "backupPath", true, "the path, where the backups of the deletes files should be stored, string, default is backupOfDeleted")
        options.addOption("v", "suppressVerbose", true, "do not output the current state of work to the console")
        options.addOption("p", "path", true, "The parent entry path of the directory, that should be checked for duplicates. string, default is pwd")
        options.addOption("h", "help", false, "print this help page")

        val commandLineParser: CommandLineParser = DefaultParser()
        val commandLine: CommandLine = commandLineParser.parse(options, args, true)
        if (commandLine.hasOption("help")) {
            val formatter = HelpFormatter()
            formatter.printHelp("duplicateFinder", options)
            System.exit(0)
        }


        if (commandLine.argList.isEmpty()) {
            VerboseHandler.printToConsole("duplicateFinder: " +
                    "No arguments supplied. For a list of options provide --help. Will execute duplicate search only.",
            DuplicateOptions(false, false, false, "", false, ""))

        }

        return generateDuplicateOptions(commandLine)
    }

    private fun generateDuplicateOptions(cmd: CommandLine): DuplicateOptions {
        var delete = false
        var recursive = false
        var suppressBackup = false
        var backupPath = "backupOfDeleted"
        var suppressVerbose = false
        var path = System.getProperty("user.dir")

        if (cmd.hasOption("delete")) {
            delete = true
            println("deletion of duplicates enabled")
        }
        if (cmd.hasOption("recursive")) {
            recursive = true
            println("recursive lookup of duplicates enabled")
        }
        if (cmd.hasOption("suppressBackup")) {
            suppressBackup = true
            println("will not backup the deleted duplicates")
        }
        if (cmd.hasOption("backupPath")) {
            backupPath = cmd.getOptionValue("backupPath")
            println("the duplicate backup path is set to ${backupPath}")
        }
        if (cmd.hasOption("suppressVerbose")) {
            suppressVerbose = true
            println("verbose output is enabled")
        }
        if (cmd.hasOption("path")) {
            path = cmd.getOptionValue("path")
            println("the duplicate search path is set to ${path}")
        }
        return DuplicateOptions(delete, recursive, suppressBackup, backupPath, suppressVerbose, path)
    }
}
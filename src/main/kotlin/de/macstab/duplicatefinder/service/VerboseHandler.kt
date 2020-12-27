package de.macstab.duplicatefinder.service

import de.macstab.duplicatefinder.options.DuplicateOptions

class VerboseHandler {
    companion object {
        @JvmStatic
        fun printToConsole(output: String, options: DuplicateOptions) {
            if (!options.suppressVerbose) println(output)
        }
    }
}
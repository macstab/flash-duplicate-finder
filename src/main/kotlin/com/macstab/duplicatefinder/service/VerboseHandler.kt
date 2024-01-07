package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.options.DuplicateOptions

class VerboseHandler {
    companion object {
        @JvmStatic
        fun printToConsole(output: String, options: DuplicateOptions) {
            if (!options.suppressVerbose) println(output)
        }
    }
}
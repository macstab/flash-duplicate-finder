package de.macstab.duplicatefinder

import de.macstab.duplicatefinder.options.OptionParser
import de.macstab.duplicatefinder.service.FileDuplicateFinder
import de.macstab.duplicatefinder.service.FileDuplicateHandler
import de.macstab.duplicatefinder.service.FileDuplicateReporter


class DuplicateFinder {
    fun startApplication(args: Array<String>) {
        val options = OptionParser().parseArguments(args)
        val deletionProcessState = FileDuplicateFinder().lookupForDuplicates(options)
        FileDuplicateReporter().reportDuplicates(deletionProcessState, options)
        FileDuplicateHandler().processDuplicates(deletionProcessState, options)
    }
}

fun main(args: Array<String>) {
    DuplicateFinder().startApplication(args)
}



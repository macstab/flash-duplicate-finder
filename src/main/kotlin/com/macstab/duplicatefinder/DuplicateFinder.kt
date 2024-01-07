package com.macstab.duplicatefinder

import com.macstab.duplicatefinder.options.DuplicateOptions
import com.macstab.duplicatefinder.options.OptionParser
import com.macstab.duplicatefinder.service.FileDuplicateFinder
import com.macstab.duplicatefinder.service.FileDuplicateHandler
import com.macstab.duplicatefinder.service.FileDuplicateReporter


class DuplicateFinder(var options: DuplicateOptions, var fileDuplicateFinder: FileDuplicateFinder, var fileDuplicateReporter: FileDuplicateReporter, var fileDuplicateHandler: FileDuplicateHandler) {
    fun startApplication(args: Array<String>) {
        val deletionProcessState = fileDuplicateFinder.lookupForDuplicates(options)
        fileDuplicateReporter.reportDuplicates(deletionProcessState, options)
        fileDuplicateHandler.processDuplicates(deletionProcessState, options)
    }
}

fun main(args: Array<String>) {
    val options = OptionParser().parseArguments(args)
    val duplicateFinder = DuplicateFinder(options, FileDuplicateFinder(), FileDuplicateReporter(), FileDuplicateHandler())
    duplicateFinder.startApplication(args)
}



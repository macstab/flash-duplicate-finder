package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.config.FileData
import com.macstab.duplicatefinder.options.DuplicateOptions

class FileDuplicateReporter {
    fun reportDuplicates(
        deletionProcessState: DeletionProcessState,
        options: DuplicateOptions,
    ) {
        deletionProcessState.filenameFileMap
            .forEach { (fileName, fileData) -> reportDuplicates(fileName, fileData, options) }
    }

    private fun reportDuplicates(
        path: String,
        fileData: FileData,
        options: DuplicateOptions,
    ) {
        if (fileData.duplicateFiles.isNotEmpty()) {
            fileData.duplicateFiles
                .forEach { VerboseHandler.printToConsole("$path has duplicate under ${it.fileName}", options) }
        }
    }
}

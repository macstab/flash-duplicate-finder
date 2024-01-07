package com.macstab.duplicatefinder.service

import com.macstab.duplicatefinder.options.DuplicateOptions
import java.io.File


class FileDuplicateFinder {
    fun lookupForDuplicates(options: DuplicateOptions): DeletionProcessState {
        val deletionProcessState = DeletionProcessState(options);
        return lookupForDuplicates(options, deletionProcessState)
    }

    internal fun lookupForDuplicates(
        options: DuplicateOptions,
        deletionProcessState: DeletionProcessState
    ): DeletionProcessState {
        checkDirectoryRecursive(options.path, deletionProcessState);
        return deletionProcessState
    }

    private fun checkDirectoryRecursive(currentWorkDirectory: String, deletionProcessState: DeletionProcessState) {
        val cwdFile = File(currentWorkDirectory)

        if (cwdFile.isDirectory) {

            for (directoryItem in cwdFile.listFiles()) {
                if (!directoryItem.isDirectory || (directoryItem.isDirectory && deletionProcessState.options.recursive))
                    checkDirectoryRecursive(directoryItem.absolutePath, deletionProcessState)
            }
        } else if (cwdFile.isFile){

            if (deletionProcessState.fileNotProcessed(cwdFile)) {
                VerboseHandler.printToConsole(
                    "Processing #${deletionProcessState.filenameFileMap.size} ${cwdFile.absolutePath}",
                    deletionProcessState.options
                )

                deletionProcessState.addNewFileToStates(cwdFile)
            }
        }
    }
}
package de.macstab.duplicatefinder.service

import de.macstab.duplicatefinder.config.FileData
import de.macstab.duplicatefinder.options.DuplicateOptions
import org.apache.commons.io.FileUtils
import java.io.File

class FileDuplicateHandler {
    fun processDuplicates(deletionProcessState: DeletionProcessState, options: DuplicateOptions) {
        deletionProcessState.filenameFileMap
                .forEach{(fileName, fileData) -> processDuplicates(fileName, fileData, options) }
    }

    private fun processDuplicates(path: String, fileData: FileData, options: DuplicateOptions) {
        fileData.duplicateFiles.filter { !it.deleted }
                .forEach { removeFile(it, options) }
    }

    private fun removeFile(file: FileData, options: DuplicateOptions) {
        if (file.deleted) {
            return  // already deleted
        }

        if (options.delete) {
            if (options.suppressBackup) {
                FileUtils.forceDelete(File(file.fileName))
                VerboseHandler.printToConsole("File ${file.fileName} deleted", options)
            } else {

                var destName = file.fileName.substring(options.path.length)
                if (destName.startsWith("/") || destName.startsWith("\\")) destName = destName.substring(1)
                var dest = File(options.backupPath, destName)
                FileUtils.forceMkdirParent(dest)
                FileUtils.moveFile(File(file.fileName), dest)
                VerboseHandler.printToConsole("backup for duplicate ${file.fileName} to backup file ${dest.absolutePath}", options)
            }

            file.deleted = true
        }
    }
}
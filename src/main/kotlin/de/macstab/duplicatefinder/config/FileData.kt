package de.macstab.duplicatefinder.config

import org.apache.commons.io.file.PathUtils
import java.nio.file.Paths


data class FileData (var fileName: String, var fileLength: Long, var checkSum: String) {
    var deleted = false
    val duplicateFiles: MutableList<FileData> = mutableListOf()

    fun equals(toCompare: FileData): Boolean {
        if (toCompare.checkSum.equals(checkSum)
                && toCompare.fileLength.equals(fileLength)) {
                    return PathUtils.fileContentEquals(Paths.get(toCompare.fileName), Paths.get(fileName))
        }
        return false
    }

    fun addDuplicate(value: FileData) {
        if (this !== value) {
            value.duplicateFiles.add(this)
            duplicateFiles. add(value)
        }
    }
}
package com.macstab.duplicatefinder.config

import org.apache.commons.io.file.PathUtils
import java.nio.file.Paths


data class FileData (var fileName: String, var fileLength: Long, var checkSum: String) {
    var deleted = false
    val duplicateFiles: MutableList<FileData> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (other is FileData
                &&
            other.checkSum.equals(checkSum)
                && other.fileLength.equals(fileLength)) {
                    return PathUtils.fileContentEquals(Paths.get(other.fileName), Paths.get(fileName))
        }
        return false
    }

    fun addDuplicate(value: FileData) {
        if (this !== value && deleted.not()) {
            value.duplicateFiles.add(this)
            duplicateFiles.add(value)
        }
    }

    override fun hashCode(): Int {
        return fileName.hashCode() * 31 + fileLength.hashCode() * 31 + checkSum.hashCode() * 31 + deleted.hashCode() * 31 + duplicateFiles.hashCode() * 31
    }
}
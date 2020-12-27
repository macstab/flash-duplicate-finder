package de.macstab.duplicatefinder.service

import de.macstab.duplicatefinder.config.FileData
import de.macstab.duplicatefinder.options.DuplicateOptions
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileInputStream

class DeletionProcessState(val options: DuplicateOptions) {

    val sizeFileMap = mutableMapOf<Long, Array<FileData>>()
    val checksumFileMap = mutableMapOf<String, Array<FileData>>()
    val filenameFileMap = mutableMapOf<String, FileData>()

    fun fileNotProcessed(currentFile: File): Boolean {
        return !filenameFileMap.containsKey(currentFile.absolutePath)
    }

    fun addNewFileToStates(cwdFile: File) {
        val fileName = cwdFile.absolutePath
        val fileSize = cwdFile.length()
        val checkSum = getSha256(cwdFile);
        val fileData = FileData(fileName, fileSize, checkSum);

        val sizeFileArray = sizeFileMap.getOrPut(fileSize) { emptyArray() }
        val checksumFileArray = checksumFileMap.getOrPut(checkSum) { emptyArray() }

        checksumFileArray
                .filter { sizeFileArray.contains(it) }
                .filter { it.equals(fileData) }
                .forEach { addLink(fileData, it) }

        sizeFileMap[fileSize] = sizeFileArray.plus(fileData)
        checksumFileMap[checkSum] = checksumFileArray.plus(fileData)
        filenameFileMap[fileName] = fileData
    }

    private fun addLink(lhs: FileData, rhs: FileData) {
        VerboseHandler.printToConsole(
                "Adding duplicate for ${lhs.fileName} to ${rhs.fileName}",
                options)

        if (!lhs.duplicateFiles.contains(rhs)) lhs.duplicateFiles.add(rhs)
        if (!rhs.duplicateFiles.contains(lhs)) rhs.duplicateFiles.add(lhs)
    }

    private fun getSha256(cwdFile: File): String {
        return FileInputStream(cwdFile).use { inputStream -> DigestUtils.sha256Hex(inputStream) }
    }
}
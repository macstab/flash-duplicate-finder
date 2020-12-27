package de.macstab.duplicatefinder.service

import de.macstab.duplicatefinder.options.DuplicateOptions
import java.io.File


class FileDuplicateFinder {
    fun lookupForDuplicates(options: DuplicateOptions): DeletionProcessState {
        val currentWorkDirectory = options.path
        val deletionProcessState = DeletionProcessState(options);

        checkDirectoryRecursive(currentWorkDirectory, deletionProcessState);
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
                        deletionProcessState.options)

                deletionProcessState.addNewFileToStates(cwdFile)
            }
        }
    }


    /*
    private fun start(delete: Boolean) {
        val dir = System.getProperty("user.dir")
        val result: HashMap<String, FileData> = handleDirectory(dir, HashMap<K, V>())
        for ((key, value) in result) {
            if (!value.getDuplicateFile().isEmpty()) {
                var duplicates = ""
                for ((fileName) in value.getDuplicateFile()) {
                    duplicates += if (duplicates.length != 0) "," else ""
                    duplicates += fileName
                }
                println("Duplicates for <$key>:$duplicates")
                if (delete) {
                    for (dup in value.getDuplicateFile()) {
                        if (!dup.isDeleted()) {
                            var destName = dup.fileName.substring(dir.length)
                            if (destName.startsWith("/") || destName.startsWith("\\")) destName = destName.substring(1)
                            var dest = File(dir, "deleted")
                            dest = File(dest, destName)
                            println("rename  <" + key + "> to :" + dest.absolutePath)
                            dest.parentFile.mkdirs()
                            File(dup.fileName).renameTo(dest)
                            dup.deleted = true
                        }
                    }
                }
            }
        }
    }

    // control directory
    private fun handleDirectory(dir: String, objects: HashMap<String, FileData?>): HashMap<String, FileData?>? {
        var dir = dir
        val directory = File(dir)
        if (directory.isDirectory) {
            for (directoryItem in directory.listFiles()) {
                handleDirectory(directoryItem.absolutePath, objects)
            }
            return objects
        }
        dir = directory.absolutePath
        if (objects.containsKey(dir)) return objects
        println("Processing[" + objects.size + "]:" + dir)
        val fileData = getFileData(directory, dir)
        for ((_, value) in objects) {
            if (value!!.fileName.equals(fileData.fileName, ignoreCase = true)) {
                return objects // the same file
            }
        }
        objects[dir] = fileData
        for ((_, value) in objects) {
            if (value!!.equals(fileData)) {
                fileData.addDuplicate(value)
            }
        }
        return objects
    }


    private fun getFileData(directory: File, dir: String): FileData {
        val data = FileData()
        data.fileName = directory.absolutePath
        data.fileLength = directory.length()
        data.checkSum = getCrc(directory)
        return data
    }


    // getCrc
    private fun getCrc(directory: File): Long {
        return try {
            val checksum: Checksum = CRC32()
            val f1 = Files.readAllBytes(directory.toPath())
            checksum.update(f1, 0, f1.size)
            checksum.getValue()
        } catch (e: IOException) {
            e.printStackTrace()
            0
        }
    }

     */
}
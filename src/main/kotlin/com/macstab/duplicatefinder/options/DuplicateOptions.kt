package com.macstab.duplicatefinder.options

data class DuplicateOptions(val delete: Boolean = false, val recursive: Boolean = false, val suppressBackup: Boolean = false,
                            val backupPath: String = "", val suppressVerbose: Boolean = false, val path: String = "")
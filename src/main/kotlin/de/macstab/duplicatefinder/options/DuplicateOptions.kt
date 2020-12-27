package de.macstab.duplicatefinder.options

data class DuplicateOptions(val delete: Boolean, val recursive: Boolean, val suppressBackup: Boolean,
                            val backupPath: String, val suppressVerbose: Boolean, val path: String) {
}

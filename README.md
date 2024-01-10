# User Documentation for File Duplicate Finder Application

## Overview of the Application

The File Duplicate Finder is a versatile command-line tool designed to help users identify and manage duplicate files within a specified directory structure. This application scans directories and their subdirectories, compares file content, and lists duplicate files based on user-defined criteria. The tool is particularly useful for organizing large data sets, cleaning up storage space, and maintaining an efficient file management system.

The application is built using Kotlin, a modern programming language that ensures high performance and compatibility across various systems. The tool's design focuses on ease of use, flexibility, and accuracy, making it suitable for both casual users and experienced professionals.

In the following sections, we will cover the installation process, starting the application, a detailed description of all available command-line switches and arguments, practical usage examples, and troubleshooting tips.

The application is released as:
- gradle project
- Docker Image
  - based on Java 19
  - Multi-Architecture Native Docker Image (amd64, arm64)
  - Arm64 Native Docker Image
  - Amd64 Native Docker Image

---

## Use of docker container

### Java Image



### Multi-Architecture Native Docker Image

### Arm64 Native Docker Image

### Amd64 Native Docker Image

---

## Installation and Starting the Application


### Installation

To install the File Duplicate Finder application, you will need to have a compatible environment that supports Kotlin applications. Ensure that you have the Java Runtime Environment (JRE) installed on your system, as Kotlin applications typically run on the Java Virtual Machine (JVM).

#### Steps for Installation:

1. **Download the Application**: Obtain the latest version of the File Duplicate Finder from the official repository or website. The application is typically distributed in the form of a `.jar` file.

2. **Place the File**: After downloading, place the `.jar` file in a desired directory. This location will be where you run the application from.

3. **Verify Java Installation**: Ensure that Java is installed on your system. You can verify this by running `java -version` in your command-line interface. If Java is not installed, download and install it from the official Oracle website or your operating system's package manager.

### Starting the Application

Run the distributed application using the following command as the distribution is creating a tar / zip file with all dependencies and a script to easly start the application:

```bash
```bash
./FlashDuplicateFinder
```

Example output of the released zip / tar file:

```text
x FlashDuplicateFinder-1.0.0/
x FlashDuplicateFinder-1.0.0/lib/
x FlashDuplicateFinder-1.0.0/lib/FlashDuplicateFinder-1.0.0.jar
x FlashDuplicateFinder-1.0.0/lib/commons-io-2.8.0.jar
x FlashDuplicateFinder-1.0.0/lib/commons-cli-1.4.jar
x FlashDuplicateFinder-1.0.0/lib/commons-codec-1.15.jar
x FlashDuplicateFinder-1.0.0/lib/kotlin-stdlib-jdk8-1.8.10.jar
x FlashDuplicateFinder-1.0.0/lib/kotlin-stdlib-jdk7-1.8.10.jar
x FlashDuplicateFinder-1.0.0/lib/kotlin-stdlib-1.8.10.jar
x FlashDuplicateFinder-1.0.0/lib/kotlin-stdlib-common-1.8.10.jar
x FlashDuplicateFinder-1.0.0/lib/annotations-13.0.jar
x FlashDuplicateFinder-1.0.0/bin/
x FlashDuplicateFinder-1.0.0/bin/FlashDuplicateFinder
x FlashDuplicateFinder-1.0.0/bin/FlashDuplicateFinder.bat
```

If you want to use gradle project you can directly start the application with gradle:

```bash
./gradlew run --args='--help'
```

In addition, we have an easy docker image to start the application

```bash
docker run -it --rm -v $(pwd):/data macstab/flash-duplicate-finder:latest --help
```

---

## Command-Line Arguments and Switches

The File Duplicate Finder offers various command-line arguments and switches to customize its behavior. Below is a table listing these options, along with a brief description, default values, and valid values where applicable.

| Argument / Switch         | Description                                                       | Default Value              | Valid Values    |
|---------------------------|-------------------------------------------------------------------|----------------------------|-----------------|
| `-d`, `--delete`          | Delete all duplicate files found during the scan.                 | `false`                    | `true`, `false` |
| `-r`, `--recursive`       | Scan all directories recursively.                                 | `false`                    | `true`, `false` |
| `-b`, `--suppressBackup`  | Suppress any backups of the deleted files.                        | `false`                    | `true`, `false` |
| `-o`, `--backupPath`      | Specify the path where backups of deleted files should be stored. | `backupOfDeleted`          | Any valid path  |
| `-v`, `--suppressVerbose` | Do not output the current state of work to the console.           | N/A                        | N/A             |
| `-p`, `--path`            | The parent entry path of the directory to check for duplicates.   | Current Working Dir (pwd)  | Any valid path  |
| `-h`, `--help`            | Print help information showing all command-line options.          | N/A                        | N/A             |

*Note: This table is based on the specific options provided for the File Duplicate Finder application.*

---

## In-Depth Explanation of Each Argument and Switch

## In-Depth Explanation of Each Argument and Switch

### `-d`, `--delete`
#### Description
The `-d` or `--delete` switch in the File Duplicate Finder application is a powerful feature designed for users who seek an automated approach to managing duplicate files. When enabled, this switch instructs the application to delete duplicate files that are identified during the scanning process. This functionality is particularly useful for users dealing with extensive file systems where manual deletion of duplicates would be time-consuming and impractical. However, the power of this feature comes with significant responsibility, as it involves permanent deletion of data.

The delete functionality is built with efficiency in mind. It operates by first scanning the specified directory (or directories) for duplicate files. Once duplicates are identified, the application cross-references these files against a set of criteria to determine which files are safe to delete. This process is designed to retain one instance of each duplicate file while removing the rest, thereby freeing up storage space without completely eradicating the data.

#### Parameters and Usage
The `-d` or `--delete` switch is a boolean flag and does not require any additional parameters. Its presence in the command line signifies that the deletion feature is active.

```bash
java -jar FileDuplicateFinder.jar --delete
```
In the command above, the application will proceed to automatically delete duplicate files as they are found. It is crucial for users to ensure that they have verified the accuracy and reliability of the duplicate detection mechanism of the application before employing this feature. This can be done by running the application in a non-deletion mode to review the duplicates that would be affected.

#### Considerations and Best Practices

- **Data Backup**: Before using the delete option, it is highly recommended to back up critical data. This precaution minimizes the risk of accidental loss of important files.
- **Test Runs**: Conduct test scans without the delete option to familiarize yourself with the application's duplicate detection patterns and to review the files that would be deleted.
- **Selective Deletion**: Be cautious when using the delete option in directories containing sensitive or critical files. It may be safer to manually review duplicates in such cases.
- **Responsibility**: Users should be aware that they bear full responsibility for any data loss that occurs as a result of using the delete option. It is advisable to use this feature only when you are certain of the implications.
- **Confirmation Prompt**: Consider implementing a confirmation prompt in the application before performing the deletion, especially when the delete option is used for the first time or in critical directories.

### `-r`, `--recursive`
#### Description
The `-r` or `--recursive` switch is a fundamental feature of the File Duplicate Finder that greatly expands its scanning capabilities. This option enables the application to perform a comprehensive search for duplicate files not only in the specified directory but also throughout all its subdirectories. The recursive nature of this option makes it an indispensable tool for users managing complex directory structures with multiple levels of nesting.

When activated, the recursive scan delves into each subdirectory, examining every file it contains. This thorough approach is particularly beneficial for users who organize their files in a hierarchical structure and need to ensure that duplicates are not overlooked in any part of the system. The utility of the `-r` switch becomes evident in scenarios where duplicates might reside in different folders within the same directory tree, a common occurrence in large data repositories.

#### Parameters and Usage
The `-r` switch does not require any additional parameters. Its inclusion in the command line is sufficient to initiate a recursive scan.

```bash
java -jar FileDuplicateFinder.jar --recursive
```

The above command instructs the application to scan the specified directory and all its subdirectories for duplicate files. This feature is particularly useful for users who need a detailed and exhaustive search across their entire file system or within specific, complex directory structures.

#### Considerations and Best Practices

- **Scanning Depth**: Users should be aware of the depth and complexity of their directory structure. Deeply nested directories or large volumes of data can significantly increase the duration of the scan.
- **System Resources**: Recursive scanning can be demanding on system resources. It is advisable to monitor system performance during the scan, especially when running other applications concurrently.
- **Selective Scanning**: In some cases, it may be more efficient to target specific subdirectories for scanning rather than initiating a full recursive scan, especially if certain areas of the directory structure are known to be free of duplicates or contain sensitive data that should not be altered.
- **Interrupt Handling**: Consider how the application handles interruptions or pauses in scanning, particularly for long-running recursive scans. Implementing features such as scan resumption can be beneficial in managing large-scale duplicate searches.

### `-b`, `--suppressBackup`
#### Description
The `-b` or `--suppressBackup` switch is a significant option in the File Duplicate Finder, giving users control over the backup mechanism during the file deletion process. By default, the application creates backups of files before deletion as a safety measure. This switch, when activated, overrides this default behavior, instructing the application not to create backups of the duplicate files it deletes. This option is particularly useful for users who are confident in the accuracy of the duplicate detection and want to streamline the deletion process by eliminating the additional step of creating backups.

#### Parameters and Usage
The `-b` switch is a boolean flag and does not require any additional parameters. When included in the command, it disables the backup creation.

```bash
java -jar FileDuplicateFinder.jar --suppressBackup
```

In this command, the application will delete duplicate files without creating backups, making the deletion process more direct and faster.

#### Considerations and Best Practices

- **Risk of Data Loss**: Users should be aware that disabling backups increases the risk of irreversible data loss. It's crucial to be certain about the files being deleted.
- **Storage Space**: Not creating backups can save significant storage space, especially when dealing with large files or a large number of duplicates.
- **Data Recovery Plan**: Ensure you have a data recovery plan or a separate backup system in place before using this option, particularly for critical data.


### `-o`, `--backupPath`
#### Description

The `-o` or `--backupPath` option in the File Duplicate Finder allows users to specify a custom directory for storing backups of deleted files. This feature is important for organizing backup files in a user-defined location, making it easier to locate and restore them if necessary. By default, the application stores backups in a directory named backupOfDeleted. However, with this option, users have the flexibility to choose a different location, perhaps one that aligns with their existing backup strategies or storage arrangements.

#### Parameters and Usage

The `-o` switch requires a path parameter that specifies the directory where backups should be stored.

```bash
java -jar FileDuplicateFinder.jar --backupPath /path/to/backupFolder
```

In the above command, backups of deleted duplicate files will be stored in /path/to/backupFolder.

#### Considerations and Best Practices

- **Backup Location**: Choose a backup location that is secure and has sufficient storage space. Consider using external drives or network locations for important backups.
- **Accessibility**: Ensure the specified path is accessible and writable by the application. Permissions issues could prevent the creation of backups.
- **Organization**: Organizing backups in a dedicated location helps in quick recovery and maintains the cleanliness of your primary storage.


### `-v`, `--suppressVerbose`
#### Description

The `-v` or `--suppressVerbose` switch is designed to control the verbosity of the application's output. By default, the File Duplicate Finder provides detailed output during its operation, which includes information about the current state of work, files being scanned, and duplicates found. However, in certain scenarios, users might prefer a quieter operation, especially when integrating the application into automated scripts or when the detailed output is not required. This switch, when activated, suppresses such verbose output, leading to a more streamlined and less cluttered command-line experience.

#### Parameters and Usage

This switch is a toggle and does not require additional parameters. Including it in the command line suppresses verbose output.

```bash
java -jar FileDuplicateFinder.jar --suppressVerbose
```

With this command, the application runs with minimal output, providing only essential information.

#### Considerations and Best Practices

- **Debugging and Monitoring**: While suppressing verbose output can be useful for certain use cases, it may hinder debugging and monitoring the application’s progress. Use this option judiciously.
- **Automated Tasks**: This option is ideal for automated tasks or scripts where output verbosity is not needed, as it can help in reducing the log size and clutter.

### `-p`, `--path`
#### Description

The `-p` or `--path` option is a crucial feature of the File Duplicate Finder, allowing users to specify the starting point of the duplicate search. By default, the application scans the current working directory, but this option provides the flexibility to target any specific directory. This is particularly useful when users need to scan directories other than the one from which they are running the application, such as network drives, external storage, or specific folders in their file system.

#### Parameters and Usage

The -p option requires a directory path parameter.

```bash
java -jar FileDuplicateFinder.jar --path /path/to/targetDirectory
```


In the command above, the application will scan /path/to/targetDirectory for duplicates.

#### Considerations and Best Practices

- **Path Validity**: Ensure that the path provided is valid and accessible. Invalid paths will result in errors or no files being scanned.
- **Relative vs Absolute Paths**: Both relative and absolute paths can be used, but absolute paths are recommended for clarity and to avoid errors related to the current working directory.
- **Permission Considerations**: Verify that the application has the necessary permissions to read the files in the specified path.


### `-h`, `--help`
#### Description

The `-h` or `--help` switch is an essential feature for any command-line application, including the File Duplicate Finder. This switch provides users with immediate access to helpful information about the application, including a list of all available command-line options and a brief description of each.

#### Parameters and Usage

The -h switch does not require any additional parameters. It is used standalone to display the help information.

```bash
java -jar FileDuplicateFinder.jar --help
```

Executing the above command will display the help information, outlining all the available command-line options with their descriptions.

---

## Usage Examples

### Example 1: Deleting Duplicates Recursively with Backup Suppression
- Command: `-d -r -b -p filesToScan`
- Directory Structure:
    - filesToScan/
        - folderA/
            - file1.txt (Duplicate)
        - folderB/
            - file2.txt (Duplicate)
            - file3.doc

#### Process
- Scans all files in `filesToScan` and its subdirectories.
- Identifies and deletes duplicates, `file1.txt` and `file2.txt`, without creating backups.

#### Expected Output
- `folderA/file1.txt` and `folderB/file2.txt` are deleted.
- No backups are created.

### Example 2: Recursive Scan with Custom Backup Path
- Command: `-r -o backupDirectory -p filesToScan`
- Directory Structure:
    - filesToScan/
        - folderC/
            - image1.jpg (Duplicate)
        - folderD/
            - image2.jpg (Duplicate)
            - report.pdf

#### Process
- Performs a recursive scan in `filesToScan`.
- Identifies duplicates and creates backups in `backupDirectory`.

#### Expected Output
- Identified duplicates: `folderC/image1.jpg` and `folderD/image2.jpg`.
- Backups of `image1.jpg` and `image2.jpg` are stored in `backupDirectory`.

### Example 3: Deleting Duplicates Without Backup
- Command: `-d -b -p "filesToScan"`
- Directory Structure:
    - filesToScan/
        - folderE/
            - doc1.pdf (Duplicate)
        - folderF/
            - doc2.pdf (Duplicate)
            - chart.xls

#### Process
- Scans `filesToScan` but not subdirectories.
- Deletes `doc1.pdf` and `doc2.pdf` without creating backups.

#### Expected Output
- `folderE/doc1.pdf` and `folderF/doc2.pdf` are deleted.
- No backups are created.

### Example 4: Specifying Backup Directory
- Command: `-o backupDir -p filestoScan`
- Directory Structure:
    - filestoScan/
        - folderG/
            - photo1.png (Duplicate)
        - folderH/
            - photo2.png (Duplicate)
            - notes.txt

#### Process
- Scans `filestoScan` but not subdirectories.
- Identifies duplicates and stores backups in `backupDir`.

#### Expected Output
- Identified duplicates: `folderG/photo1.png` and `folderH/photo2.png`.
- Backups of `photo1.png` and `photo2.png` are stored in `backupDir`.


---

## Troubleshooting and FAQs

In this section, we address some common issues and questions users might encounter while using the File Duplicate Finder application. This guide aims to provide quick solutions and clarifications to enhance user experience.

### Q1: The application is not starting. What should I do?
**A:** Ensure that you have Java installed on your system and that the `.jar` file is correctly placed in a directory accessible by Java. Also, check if the command used to start the application is correctly formatted.

### Q2: How can I ensure that the application does not delete important files?
**A:** Use the application without the `-d` (delete) option for an initial scan. This will list the duplicates without deleting them, allowing you to review the files that would be affected.

### Q3: The application is taking too long to scan. How can I speed it up?
**A:** Large directories or directories with a significant number of files can increase scan times. Consider using the `-p` option to target specific directories or excluding large, non-critical directories from the scan.

### Q4: I accidentally deleted files using the application. Can I recover them?
**A:** If you did not use the `-b` (suppressBackup) option, the application should have created backups of the deleted files in the specified backup directory or the default backup location. Check there for any backups.

### Q5: How do I handle permission issues when scanning certain directories?
**A:** Make sure that the user running the application has the necessary read (and write, if using delete options) permissions for the directories being scanned. Running the application with administrative privileges might resolve permission issues.

### Q6: Can I use this application on networked or external drives?
**A:** Yes, as long as the drive is mounted and accessible from your system, and the application has the necessary permissions to read (and write) to the drive.

### Q7: The verbose output is overwhelming. Can I reduce it?
**A:** Yes, use the `-v` (suppressVerbose) option to minimize the output during the scan. This will result in a less detailed output, showing only essential information.

### Q8: How accurate is the duplicate detection?
**A:** The application uses content-based comparison to identify duplicates, which is generally very accurate. However, always review the list of duplicates before proceeding with deletion.



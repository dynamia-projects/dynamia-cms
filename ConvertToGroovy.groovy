#!/usr/bin/env groovy

def renameFolder(String folderName, String target, String newname) {
    def folder = new File(folderName)
    if (folder.directory) {
        if (folder.name == target) {
            def newfolder = new File(folder.parent, newname)

            println "Renaming Folder $folder.path  ---> $newfolder.path"
            folder.renameTo(newfolder)
        } else {
            folder.eachFile { renameFolder(it.path, target, newname) }
        }
    }
}

def renameFile(String folderName, String ext, String newext) {
    def folder = new File(folderName)
    if (folder.directory) {
        folder.eachFile { file ->
            if (file.directory) {
                renameFile(file.path, ext, newext)
            } else {
                if (file.name.endsWith(".$ext")) {
                    def newfile = new File(file.parent, file.name.replace(".$ext", ".$newext"))

                    println "Renaming File $file.path  ---> $newfile.path"
                    file.renameTo(newfile)
                }
            }
        }
    }
}

def folder = args[0]
if (folder != null) {
    renameFolder(folder, "java", "groovy")
    println "--------------------------------------"
    renameFile(folder, "java", "groovy")
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.ui.ui

import tools.dynamia.cms.core.DynamiaCMS
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.io.IOUtils
import tools.dynamia.zk.BindingComponentIndex
import tools.dynamia.zk.ComponentAliasIndex
import tools.dynamia.zk.ui.Uploadlink

import java.nio.file.Path
import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author mario
 */
class ResourceUploadLink extends Uploadlink {

    static {
        BindingComponentIndex.instance.put("value", ResourceUploadLink.class)
        ComponentAliasIndex.instance.put("resourceUploadlink", ResourceUploadLink.class)
    }

    private String value
    private String subfolder
    private boolean generateFileName

    ResourceUploadLink() {
        generateUploadDirectory()
    }

    boolean isGenerateFileName() {
        return generateFileName
    }

    void setGenerateFileName(boolean generateFileName) {
        this.generateFileName = generateFileName
    }

    String getSubfolder() {
        return subfolder
    }

    void setSubfolder(String subfolder) {
        this.subfolder = subfolder
        generateUploadDirectory()
    }

    String getValue() {
        return value
    }

    void setValue(String value) {
        this.value = value
        if (value != null) {
            label = value
        }
    }

    @Override
    protected void onFileUpload() {
        String name = uploadedFile.name
        if (generateFileName) {
            try {

                name = System.nanoTime() + "." + uploadedFile.extension
                File output = new File(uploadedFile.file.parentFile, name)
                IOUtils.copy(uploadedFile.file, output)
                uploadedFile.delete()
            } catch (IOException ex) {
                Logger.getLogger(ResourceUploadLink.class.name).log(Level.SEVERE, null, ex)
            }
        }
        value = name
    }

    private void generateUploadDirectory() {
        Path base = DynamiaCMS.getSitesResourceLocation(SiteContext.get().current)
        if (subfolder != null) {
            Path subfolderPath = base.resolve(subfolder)
            DynamiaCMS.createDirectoryIfNotExists(subfolderPath)
            uploadDirectory = subfolderPath.toFile().absolutePath
        } else {
            uploadDirectory = base.toFile().absolutePath
        }

    }

}

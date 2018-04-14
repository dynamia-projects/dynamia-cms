/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.core.zk.ui

import tools.dynamia.cms.site.core.DynamiaCMS
import tools.dynamia.cms.site.core.SiteContext
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
        BindingComponentIndex.getInstance().put("value", ResourceUploadLink.class)
        ComponentAliasIndex.getInstance().put("resourceUploadlink", ResourceUploadLink.class)
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
            setLabel(value)
        }
    }

    @Override
    protected void onFileUpload() {
        String name = getUploadedFile().getName()
        if (isGenerateFileName()) {
            try {

                name = System.nanoTime() + "." + getUploadedFile().getExtension()
                File output = new File(getUploadedFile().getFile().getParentFile(), name)
                IOUtils.copy(getUploadedFile().getFile(), output)
                getUploadedFile().delete()
            } catch (IOException ex) {
                Logger.getLogger(ResourceUploadLink.class.getName()).log(Level.SEVERE, null, ex)
            }
        }
        setValue(name)
    }

    private void generateUploadDirectory() {
        Path base = DynamiaCMS.getSitesResourceLocation(SiteContext.get().getCurrent())
        if (subfolder != null) {
            Path subfolderPath = base.resolve(subfolder)
            DynamiaCMS.createDirectoryIfNotExists(subfolderPath)
            setUploadDirectory(subfolderPath.toFile().getAbsolutePath())
        } else {
            setUploadDirectory(base.toFile().getAbsolutePath())
        }

    }

}
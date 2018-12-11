/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.admin.ui

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
 * @author Mario Serrano Leones
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

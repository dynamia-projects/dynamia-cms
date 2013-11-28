/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.templates;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mario
 */
public class Template implements Serializable {

    private String name;
    private String directoryName;
    private String description;
    private String author;
    private String date;
    private String version;
    private List<String> positions;

    private Template() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getPositions() {
        return positions;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public static Template build(Properties prop, File directory) {
        Template t = new Template();
        t.name = prop.getProperty("name");
        t.author = prop.getProperty("author");
        t.date = prop.getProperty("date");
        t.version = prop.getProperty("version");
        t.description = prop.getProperty("description");
        t.directoryName = directory.getName();

        String pos = prop.getProperty("positions");
        if (pos != null && !pos.isEmpty()) {
            String[] positions = pos.split(",");
            if (positions != null) {
                List<String> positionsList = new ArrayList<>();
                for (String position : positions) {
                    positionsList.add(position.trim());
                }
                t.positions = Collections.unmodifiableList(positionsList);
            }
        }

        return t;
    }

    @Override
    public String toString() {
        return name;
    }

}


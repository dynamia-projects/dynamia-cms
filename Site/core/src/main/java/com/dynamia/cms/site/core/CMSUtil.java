/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.text.DecimalFormat;

/**
 *
 * @author mario
 */
public class CMSUtil {

      
    public String formatNumber(Number number, String pattern) {
        if(pattern==null){
            return String.valueOf(number);
        }
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(number);
    }
}

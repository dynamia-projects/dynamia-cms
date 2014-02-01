/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.tools.commons.CollectionsUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mario
 */
public class CMSUtil {

    public String formatNumber(Number number) {
        if (number == null) {
            return "";
        }

        return NumberFormat.getIntegerInstance().format(number);
    }

    public String formatNumber(Number number, String pattern) {
        if (number == null) {
            return "";
        }

        if (pattern == null) {
            return String.valueOf(number);
        }
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(number);
    }

    public Collection groupCollection(Collection collection, int groupSize) {
        return CollectionsUtils.group(collection, groupSize);
    }

    public String absoluteURL(String url) {
        if (url == null || url.isEmpty()) {
            return "#";
        }

        if (!url.startsWith("http")) {
            return "http://" + url;
        } else {
            return url;
        }
    }

    public String cropText(String text, int size) {
        if (text == null) {
            return "";
        }
        text = text.replaceAll(",", " ");

        if (text.length() <= size) {
            return text;
        } else {
            return text.substring(0, size) + "...";
        }
    }
    
    public static Cookie getCookie(HttpServletRequest request, String cookieName){
        Cookie cookies[] =  request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)){
                    return cookie;
                }
            }
        }
        return null;
    }
}

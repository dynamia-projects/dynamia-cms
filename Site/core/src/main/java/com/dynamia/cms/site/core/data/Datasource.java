/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.data;

import java.util.List;
import java.util.Map;

/**
 *
 * @author mario
 */
public interface Datasource<T> {

    public String getName();

    public T get(Class<T> type, Map<String, Object> params);

    public T get(Class<T> type, Object id);

    public void add(T t);

    public void remove(T t);

    public List<T> find(Class<T> type, Map<String, Object> params);
}

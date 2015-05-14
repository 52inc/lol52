package com.ftinc.lolserv.data.model;

import java.util.Map;

/**
 * Interface for modelling objects to a database
 *
 * Created by drew.heavner on 5/13/15.
 */
public interface ModelMap<T> {

    /**
     * Implement this method to map a generic mapping to the model
     * object
     *
     * @param map       the map to convert from
     * @return          the deserialized object
     */
    T fromMap(Map<String, Object> map);


}

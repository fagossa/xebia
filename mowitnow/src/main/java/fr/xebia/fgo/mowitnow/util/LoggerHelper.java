/*
 * 
 */

package fr.xebia.fgo.mowitnow.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Façades pour les loggers
 * 
 * @author fagossa
 */
public class LoggerHelper {
    
    public static Logger getTechnicalLogger(Class clazz) {
        return LoggerFactory.getLogger("TECHNICAL - " + clazz.getSimpleName());
    }
    
    public static Logger getBusinessLogger(Class clazz) {
        return LoggerFactory.getLogger("BUSINESS - " + clazz.getSimpleName());
    }
    
}

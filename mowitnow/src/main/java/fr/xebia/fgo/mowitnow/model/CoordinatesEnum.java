/*
 * 
 */
package fr.xebia.fgo.mowitnow.model;

/**
 * Liste de directions possibles pour les tondeuses
 * 
 * @author fagossa
 */
public enum CoordinatesEnum {

    N, // north
    E, // east
    W, // west
    S; // south

    /**
     * Obtienne le valeur de l'enum ou null par rapport au valeur specifié. C'est
     * une alternative à <code>valueOf</code> mail il jete pas d'exception
     * @param value valeur à transformer
     * @return valeur de l'enum ou <b>null</b>
     */
    static CoordinatesEnum from(String value) {
        for (CoordinatesEnum currValue : values()) {
            if (currValue.toString().equals(value)) {
                return currValue;
            }
        }
        return null;
    }
    
}

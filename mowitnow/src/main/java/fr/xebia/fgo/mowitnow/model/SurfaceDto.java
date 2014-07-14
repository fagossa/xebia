/*
 * 
 */
package fr.xebia.fgo.mowitnow.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fagossa
 */
public class SurfaceDto {

    // coordonnées du coin supérieur droit de la pelouse
    private final int maxPosX;
    private final int maxPosY;

    // liste de tondeuses
    private List<MowerDto> mowers
            = new ArrayList<MowerDto>();

    /**
     * Construit d'une nouvelle instance de surface
     */
    public static class SurfaceBuilder {

        private final int maxPosX;
        private final int maxPosY;

        /**
         * Contruction du builder
         * 
         * @param config string contenant la longueur et la largeur séparé par
         * un space
         * @throws IllegalArgumentException en cas de mauvaise config
         */
        public SurfaceBuilder(String config) {
            String[] values = null;
            // invalid config?
            if (config == null
                    || (values = config.split(" ")).length != 2) {
                throw new IllegalArgumentException("invalid garden config " + config);
            }
            try {
                maxPosX = Integer.parseInt(values[0]);
                maxPosY = Integer.parseInt(values[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid config for width = "
                        + values[0] + " or height = " + values[1]);
            }
        }

        /**
         * @return nouvelle instance
         */
        public SurfaceDto build() {
            return new SurfaceDto(maxPosX, maxPosY);
        }
    }

    /*
     * création de la surface avec la largeur et le longeur
     */
    private SurfaceDto(int maxPosX, int maxPosY) {
        this.maxPosX = maxPosX;
        this.maxPosY = maxPosY;
    }

    public int getMaxPosX() {
        return maxPosX;
    }

    public int getMaxPosY() {
        return maxPosY;
    }

    public void setMowers(List<MowerDto> mowers) {
        this.mowers = mowers;
    }

    public List<MowerDto> getMowers() {
        return mowers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.maxPosX;
        hash = 61 * hash + this.maxPosY;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurfaceDto other = (SurfaceDto) obj;
        if (this.maxPosX != other.maxPosX) {
            return false;
        }
        if (this.maxPosY != other.maxPosY) {
            return false;
        }
        if (this.mowers != other.mowers && (this.mowers == null || !this.mowers.equals(other.mowers))) {
            return false;
        }
        return true;
    }

}

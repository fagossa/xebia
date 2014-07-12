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

    private final int width;
    private final int height;
    
    // liste de tondeuses
    private List<MowerDto> mowers
            = new ArrayList<MowerDto>();

    /**
     * Construit une nouvelle instance de surface
     *
     * @param config string contenant la longueur et la largeur séparé par un
     * space
     * @return nouvelle instance de Surface
     * @throws IllegalArgumentException en cas de mauvaise config
     */
    public static SurfaceDto buildSurface(String config) {
        String[] values = null;
        // invalid config?
        if (config == null
                || (values = config.split(" ")).length != 2) {
            throw new IllegalArgumentException("invalid garden config " + config);
        }
        int width, height;
        try {
            width = Integer.parseInt(values[0]);
            height = Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid config for width = "
                    + values[0] + " or height = " + values[1]);
        }
        return new SurfaceDto(width, height);
    }

    /*
     * création de la surface avec la largeur et le longeur
     */
    private SurfaceDto(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
        hash = 61 * hash + this.width;
        hash = 61 * hash + this.height;
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
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.mowers != other.mowers && (this.mowers == null || !this.mowers.equals(other.mowers))) {
            return false;
        }
        return true;
    }
    
    
}

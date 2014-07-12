/*
 * 
 */
package fr.xebia.fgo.mowitnow.model;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author fagossa
 */
public class MowerDto {

    private int x;
    private int y;
    private CoordinatesEnum dir;
    // FIFO queue
    private final Queue<MovementsEnum> queue
            = new LinkedTransferQueue<MovementsEnum>();

    /**
     * construction d'une instance de la classe
     *
     * @param config config initial de la tondeuse (e.g."1 2 N")
     * @return nouvelle instance
     * @throws IllegalArgumentException en cas de mauvaise config
     */
    public static MowerDto buildMowerDto(String config) {
        String[] data = null;
        // invalid config?
        if (config == null
                || (data = config.split(" ")).length != 3) {
            throw new IllegalArgumentException("invalid config " + config);
        }
        int x, y;
        try {
            x = Integer.parseInt(data[0]);
            y = Integer.parseInt(data[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid config for x = "
                    + data[0] + " or y = " + data[1]);
        }
        CoordinatesEnum dir = CoordinatesEnum.from(data[2]);
        // unknown direction?
        if (dir == null) {
            throw new IllegalArgumentException("invalid direction "
                    + data[2]);
        }
        return new MowerDto(x, y, dir);
    }

    private MowerDto(int x, int y, CoordinatesEnum dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    /**
     * ajoute les mouvements specifiés à la queue de mouvements
     *
     * @param steps mouvements en string conservant les valeurs de
     * <code>MovementsEnum</code>
     * @see MouvementsEnum
     */
    public void enqueue(String steps) {
        for (int i = 0; i < steps.length(); i++) {
            String step = String.valueOf(steps.charAt(i));
            try {
                MovementsEnum stepEnum
                        = MovementsEnum.valueOf(step);
                queue.offer(stepEnum);
            } catch (IllegalArgumentException e) {
                // does nothing if unknown value
            }
        }
    }

    /*
     * Retourne et enlève le dernier mouvement de la queue
     * @return la tête de la queue ou null
     */
    public MovementsEnum dequeue() {
        return queue.poll();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CoordinatesEnum getDirection() {
        return dir;
    }

    public void setDirection(CoordinatesEnum dir) {
        this.dir = dir;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        hash = 97 * hash + (this.dir != null ? this.dir.hashCode() : 0);
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
        final MowerDto other = (MowerDto) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.dir != other.dir) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MowerDto [x=" + x + ", y=" + y
                + ", direction=" + dir + ", queue="
                + queue + "]";
    }
}

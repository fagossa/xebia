/*
 * 
 */
package fr.xebia.fgo.mowitnow.model;

import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Répresentation de la tondeuse
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
     */
    public static class MowerBuilder {

        private final int x;
        private final int y;
        private final CoordinatesEnum dir;

        /**
         * Contruction du builder de la tondeuse
         * 
         * @param config La position et l'orientation sont fournies sous la 
         * forme de 2 chiffres et une lettre, séparés par un espace
         * (e.g."1 2 N")
         * @throws IllegalArgumentException en cas de mauvaise config
         */
        public MowerBuilder(String config) {
            String[] data = null;
            // config invalide?
            if (config == null
                    || (data = config.split(" ")).length != 3) {
                throw new IllegalArgumentException("invalid config " + config);
            }
            // validation des données
            try {
                x = Integer.parseInt(data[0]);
                y = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid config for x = "
                        + data[0] + " or y = " + data[1]);
            }
            dir = CoordinatesEnum.from(data[2]);
            // direction inconnue?
            if (dir == null) {
                throw new IllegalArgumentException("invalid direction "
                        + data[2]);
            }
        }

        /**
         * @return nouvelle instance 
         */
        public MowerDto build() {
            return new MowerDto(x, y, dir);
        }
    }

    /*
     * construction d'une nouvelle instance de la tondeuse par rapport au 
     * critères speficiées
     */
    private MowerDto(int x, int y, CoordinatesEnum dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    /**
     * ajoute les mouvements specifiés à la queue de mouvements. Le mouvements
     * invalides sont ignorés
     *
     * @param steps Les instructions sont une suite de caractères sans espaces 
     * selon les valeures de <code>MovementsEnum</code>
     *
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
                // fait rien si valeur non valide
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

    /**
     * @return position x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x nouvelle position de x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return position y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y nouvelle position de y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return direction de la tonteuse
     */
    public CoordinatesEnum getDirection() {
        return dir;
    }

    /**
     * @param dir nouvelle direction de la tondeuse
     */
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
    
    /**
     * Impresion de l'état et la position
     * 
     * @param out Strem pour imprimer la situation actuelle après d'exècuter les
     * mouvements
     */
    public void showCurrentState(PrintStream out) {
        out.println(toString());
    }

}

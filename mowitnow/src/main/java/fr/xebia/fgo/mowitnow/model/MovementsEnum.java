/*
 * 
 */
package fr.xebia.fgo.mowitnow.model;

/**
 * Ensemble de mouvements possibles pour une tondeuse
 * 
 * @author fagossa
 */
public enum MovementsEnum {
    /* virement à droite sans déplacer la tondeuse */
    D {
                @Override
                public void move(MowerDto dto, int width, int height) {
                    switch (dto.getDirection()) {
                        case N:
                            dto.setDirection(CoordinatesEnum.E);
                            break;
                        case E:
                            dto.setDirection(CoordinatesEnum.S);
                            break;
                        case W:
                            dto.setDirection(CoordinatesEnum.N);
                            break;
                        case S:
                            dto.setDirection(CoordinatesEnum.W);
                            break;
                    }
                }

            },
    /* virement à gauche sans déplacer la tondeuse */
    G {
                @Override
                public void move(MowerDto dto, int width, int height) {
                    switch (dto.getDirection()) {
                        case N:
                            dto.setDirection(CoordinatesEnum.W);
                            break;
                        case E:
                            dto.setDirection(CoordinatesEnum.N);
                            break;
                        case W:
                            dto.setDirection(CoordinatesEnum.S);
                            break;
                        case S:
                            dto.setDirection(CoordinatesEnum.E);
                            break;
                    }
                }

            },
    /* 
     * signifie que l'on avance la tondeuse d'une case dans la direction à 
     * laquelle elle fait face
     */
    A {
                @Override
                public void move(MowerDto dto, int width, int height) {
                    switch (dto.getDirection()) {
                        case N:
                            if (dto.getY() + 1 <= height) {
                                dto.setY(dto.getY() + 1);
                            }
                            break;
                        case E:
                            if (dto.getX() + 1 <= width) {
                                dto.setX(dto.getX() + 1);
                            }
                            break;
                        case W:
                            if (dto.getX() - 1 >= 0) {
                                dto.setX(dto.getX() - 1);
                            }
                            break;
                        case S:
                            if (dto.getY() - 1 >= 0) {
                                dto.setY(dto.getY() - 1);
                            }
                            break;
                    }
                }

            };

    /**
     * Exécute le mouvement sur une tondeuse
     * 
     * @param dto tondeuse à déplacer
     * @param width largeur de la surface
     * @param height hauteur de la surface
     */
    public abstract void move(MowerDto dto, int width, int height);
}

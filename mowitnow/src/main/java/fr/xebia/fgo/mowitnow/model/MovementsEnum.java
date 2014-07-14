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
                public void move(MowerDto dto, int maxPosX, int maxPosY) {
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
                public void move(MowerDto dto, int maxPosX, int maxPosY) {
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
     * laquelle elle fait face. La case directement au Nord de la 
     * position (x, y) a pour coordonnées (x, y+1).
     */
    A {
                @Override
                public void move(MowerDto dto, int maxPosX, int maxPosY) {
                    switch (dto.getDirection()) {
                        case N:
                            if (dto.getY() + 1 <= maxPosY) {
                                dto.setY(dto.getY() + 1);
                            }
                            break;
                        case E:
                            if (dto.getX() + 1 <= maxPosX) {
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
     * Exécute le mouvement sur une tondeuse. Si la position après mouvement 
     * est en dehors de la pelouse, la tondeuse ne bouge pas, conserve son 
     * orientation et traite la commande suivante.
     * 
     * @param dto tondeuse à déplacer
     * @param maxPosX position maximal sur X
     * @param maxPosY position maximal sur Y
     */
    public abstract void move(MowerDto dto, int maxPosX, int maxPosY);
}

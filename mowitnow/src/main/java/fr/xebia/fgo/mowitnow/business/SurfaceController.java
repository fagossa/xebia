/*
 * 
 */
package fr.xebia.fgo.mowitnow.business;

import fr.xebia.fgo.mowitnow.model.MovementsEnum;
import fr.xebia.fgo.mowitnow.model.MowerDto;
import fr.xebia.fgo.mowitnow.model.SurfaceDto;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Controlleur métier pour les surfaces
 *
 * @author fagossa
 */
public class SurfaceController {

    /**
     * Exécute la liste de mouvements des tondeuses sur la surface specifié
     * depuis un fichier <b>txt</b>
     *
     * @param file fichier contentant les instructions à exécuter
     * @return instance de Surface contenant les tondeuses dans la position
     * final du traitement
     * @throws FileNotFoundException en cas que le fichier n'existe pas
     * @throws IllegalArgumentException en cas de problèmes de format dans la
     * liste specifiée
     * @see #executeMovements(java.lang.String[]) 
     */
    public SurfaceDto executeMovements(final File file) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(file);
        List<String> instructions = new ArrayList<String>();
        while (fileScanner.hasNextLine()) {
            instructions.add(fileScanner.nextLine());
        }
        String[] array = instructions.toArray(new String[instructions.size()]);
        return executeMovements(array);
    }

    /**
     * Exécute la liste de mouvements des tondeuses sur la surface specifié.
     * Chaque paramètre dois être conforme au format de son builder.
     *
     * @param movements configuration de la surface et liste des mouvements à
     * exécuter. La première ligne répresente la taille de la surface.
     *
     * @return instance de Surface contenant les tondeuses dans la position
     * final du traitement
     * @throws IllegalArgumentException en cas de problèmes de format dans la
     * liste specifiée
     * 
     * @see SurfaceDto#buildSurface(java.lang.String)
     * @see MowerDto#buildMowerDto(java.lang.String)
     */
    public SurfaceDto executeMovements(final String[] movements) {
        if (movements == null || movements.length == 0) {
            throw new IllegalArgumentException("no movements");
        }
        if (movements.length % 2 == 0) {
            throw new IllegalArgumentException("config cannot be pair");
        }
        // la prémier pos est toujours la config de la surface
        SurfaceDto surface = new SurfaceDto.SurfaceBuilder(movements[0]).build();
        // lecture des autres positions
        List<MowerDto> mowers = getMowersFromArray(movements);
        surface.setMowers(mowers);
        // transformation et exécution de chaque mouvement
        executeQueuedMovements(surface);
        return surface;
    }

    /*
     * Obtenir la liste de tondeuses depuis l'array de mouvements specifié.
     * La suite du fichier permet de piloter toutes les tondeuses qui ont été 
     * déployées.
     * 
     * @param movements la prémier position a la config de la surface. Les 
     * autres sont la position initial de chaque tondeuse et sa liste de
     *  mouvements
     */
    private List<MowerDto> getMowersFromArray(
            final String[] movements) {
        List<MowerDto> list = new ArrayList<MowerDto>();
        // on est certain qu'il y a une quantité impair des tondeuses
        for (int i = 1; i < movements.length; i += 2) {
            MowerDto mower = new MowerDto.MowerBuilder(movements[i]).build();
            /* la seconde ligne est une série d'instructions ordonnant à la tondeuse d'explorer la
             * pelouse.
             */
            mower.enqueue(movements[i + 1]);
            list.add(mower);
        }
        return list;
    }

    /*
     * Exécute la liste de movements dans la queue de la tondeuse
     */
    private void executeQueuedMovements(final SurfaceDto surface) {
        List<MowerDto> mowerList = surface.getMowers();
        // Chaque tondeuse se déplace de façon séquentielle
        for (MowerDto mower : mowerList) {
            MovementsEnum movement;
            while ((movement = mower.dequeue()) != null) {
                movement.move(mower,
                        surface.getMaxPosX(), surface.getmaxPosY());
            }
            mower.showCurrentState(System.out);
        }
    }

}

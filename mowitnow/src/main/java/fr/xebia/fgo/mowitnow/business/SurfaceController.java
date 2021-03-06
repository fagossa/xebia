/*
 * 
 */
package fr.xebia.fgo.mowitnow.business;

import fr.xebia.fgo.mowitnow.model.MovementsEnum;
import fr.xebia.fgo.mowitnow.model.MowerDto;
import fr.xebia.fgo.mowitnow.model.SurfaceDto;
import fr.xebia.fgo.mowitnow.util.LoggerHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;

/**
 * Controlleur métier pour les surfaces
 *
 * @author fagossa
 */
public class SurfaceController {
    
    /**
     * Logger métier
     */
    private Logger logger = LoggerHelper.getBusinessLogger(SurfaceController.class);

    /**
     * Exécute la liste de mouvements des tondeuses en batch sur la surface specifié
     * depuis un fichier <b>txt</b>
     *
     * @param file fichier contentant les instructions à exécuter
     * @return instance de Surface contenant les tondeuses dans la position
     * final du traitement
     * @throws FileNotFoundException en cas que le fichier n'existe pas
     * @throws IllegalArgumentException en cas de problèmes de format dans la
     * liste d'instructions specifiée
     * @see #executeMovements(java.lang.String[]) 
     */
    public SurfaceDto executeMovements(final File file) throws FileNotFoundException {
        logger.info("begin executeMovements in batch for file {}", file);
        Scanner fileScanner = new Scanner(file);
        List<String> instructions = new ArrayList<String>();
        while (fileScanner.hasNextLine()) {
            instructions.add(fileScanner.nextLine());
        }
        String[] array = instructions.toArray(new String[instructions.size()]);
        SurfaceDto surface = executeMovements(array);
        logger.info("end executeMovements in batch");
        return surface;
    }
    
    /**
     * Exécute la liste de mouvements des tondeuses sur la surface specifié
     * depuis un fichier <b>txt</b>
     * 
     * @param file fichier contentant les instructions à exécuter
     * @return instance de Surface contenant les tondeuses dans la position
     * final du traitement
     * @throws FileNotFoundException en cas que le fichier n'existe pas
     * @throws IllegalArgumentException en cas de problèmes de format dans la
     * liste d'instructions specifiée
     */
    public SurfaceDto executeMovementsInLine(final File file) throws FileNotFoundException {
        logger.info("begin executeMovements inline for file {}", file);
        Scanner fileScanner = new Scanner(file);
        SurfaceDto surface = null;
        MowerDto mover = null;
        while (fileScanner.hasNextLine()) {
            String instruction = fileScanner.nextLine();
            if (surface == null) {
                surface = new SurfaceDto.SurfaceBuilder(instruction).build();
            } else {
                if (containsSteps(instruction, MovementsEnum.class)) {
                    if (mover != null) {
                        mover.enqueue(instruction);
                        mover.dequeue();
                    } else {
                        logger.error("Step specified but no mower declared for {}",instruction);
                        throw new IllegalStateException("Step specified but no "
                                + "mower declared for instruction " + instruction);
                    }
                } else {
                    if (surface == null) {
                        logger.error("No surface config");
                        throw new IllegalStateException("No surface config");
                    }
                    mover = new MowerDto.MowerBuilder(surface, instruction).build();
                    surface.getMowers().add(mover);
                }
            }
        }
        logger.info("end executeMovements inline");
        return surface;
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
     * @see SurfaceDto#SurfaceBuilder(java.lang.String)
     * @see MowerDto#MowerBuilder(SurfaceDto, java.lang.String)
     */
    public SurfaceDto executeMovements(final String[] movements) {
        logger.info("begin executeMovements from array");
        if (movements == null || movements.length == 0) {
            logger.error("no movements");
            throw new IllegalArgumentException("no movements");
        }
        if (movements.length % 2 == 0) {
             logger.error("the amt({}) of elements in the array cannot be pair", 
                     movements.length);
            throw new IllegalArgumentException("config cannot be pair");
        }
        // la prémier pos est toujours la config de la surface
        SurfaceDto surface = new SurfaceDto.SurfaceBuilder(movements[0]).build();
        // lecture des autres positions
        List<MowerDto> mowers = getMowersFromArray(surface, movements);
        surface.setMowers(mowers);
        // transformation et exécution de chaque mouvement
        executeQueuedMovements(surface);
        logger.info("end executeMovements from array");
        return surface;
    }
    
    /*
     * vérifie si l'instruction specifié fait partie de l'enum specifié
     */
    private <T extends Enum> boolean containsSteps(String instruction, Class<T> clazz) {
        Object[] values = clazz.getEnumConstants();
        for (Object value : values) {
            if (instruction.indexOf(value.toString())> 0) {
                return true;
            }
        }
        return false;
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
    private List<MowerDto> getMowersFromArray(SurfaceDto surface,
            final String[] movements) {
        List<MowerDto> list = new ArrayList<MowerDto>();
        // on est certain qu'il y a une quantité impair des tondeuses
        for (int i = 1; i < movements.length; i += 2) {
            MowerDto mower = new MowerDto.MowerBuilder(surface, movements[i]).build();
            /* la seconde ligne est une série d'instructions ordonnant à la 
             * tondeuse d'explorer la pelouse.
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
        for (MowerDto mowerDto : mowerList) {
            mowerDto.dequeue();
            /* Lorsqu'une tondeuse achève une série d'instruction, elle 
             * communique sa position et son orientation.
             */
            mowerDto.showCurrentState(System.out);
        }
    }

}

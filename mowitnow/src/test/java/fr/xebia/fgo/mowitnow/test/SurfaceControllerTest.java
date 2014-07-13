/*
 * 
 */
package fr.xebia.fgo.mowitnow.test;

import fr.xebia.fgo.mowitnow.business.SurfaceController;
import fr.xebia.fgo.mowitnow.model.MowerDto;
import fr.xebia.fgo.mowitnow.model.SurfaceDto;
import java.io.File;
import java.io.FileNotFoundException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import org.junit.Test;

/**
 *
 * @author fagossa
 */
public class SurfaceControllerTest {

    @Test
    public void testSurfaceConfig() {
        SurfaceController controller = new SurfaceController();
        String[] movements = {"5 5"};
        SurfaceDto surface = controller.executeMovements(movements);

        SurfaceDto expectedSurface = new SurfaceDto.SurfaceBuilder("5 5").build();
        assertEquals("surfaces not equals", surface, expectedSurface);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongSurfaceConfig() {
        SurfaceController controller = new SurfaceController();
        String[] movements = {"5"};
        SurfaceDto surface = controller.executeMovements(movements);
        fail("Invalid surface config did not generate exception");
    }

    @Test
    public void testQueueingMovements() {
        SurfaceController controller = new SurfaceController();
        String[] movements = {
            "5 5",
            "1 2 N",
            "GAGAGAGAA",
            "3 3 E",
            "AADAADADDA"
        };
        SurfaceDto surface = controller.executeMovements(movements);
        assertEquals("invalid mower count", 2, surface.getMowers().size());

        MowerDto expMower1 = new MowerDto.MowerBuilder("1 3 N").build();
        assertEquals("invalid mower result", expMower1, surface.getMowers().get(0));

        MowerDto expMower2 = new MowerDto.MowerBuilder("5 1 E").build();
        assertEquals("invalid mower result", expMower2, surface.getMowers().get(1));

    }

    @Test
    public void testQueueingMovementsFromFile() throws FileNotFoundException {
        String url = getClass().getResource("/mowitnow.txt").getFile();
        File file = new File(url);
        SurfaceController controller = new SurfaceController();
        SurfaceDto surface = controller.executeMovements(file);

        assertEquals("invalid mower count", 2, surface.getMowers().size());

        MowerDto expMower1 = new MowerDto.MowerBuilder("1 3 N").build();
        assertEquals("invalid mower result", expMower1, surface.getMowers().get(0));

        MowerDto expMower2 = new MowerDto.MowerBuilder("5 1 E").build();
        assertEquals("invalid mower result", expMower2, surface.getMowers().get(1));
    }
}

/*
 * 
 */
package fr.xebia.fgo.mowitnow.test;

import fr.xebia.fgo.mowitnow.model.SurfaceDto;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import org.junit.Test;

/**
 *
 * @author fagossa
 */
public class SurfaceDtoTest {

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrownTooFewParams() {
        SurfaceDto tester = new SurfaceDto.SurfaceBuilder("10").build();
        fail("Invalid config did not generate exception");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrownTooMuchParms() {
        SurfaceDto tester = new SurfaceDto.SurfaceBuilder("10 5 6").build();
        fail("Invalid config did not generate exception");
    }

    @Test
    public void testSucessfulBuilder() {
        SurfaceDto tester = new SurfaceDto.SurfaceBuilder("8 4").build();
        assertEquals("width must be 10", 8, tester.getMaxPosX());
        assertEquals("height must be 6", 4, tester.getMaxPosY());
    }
}

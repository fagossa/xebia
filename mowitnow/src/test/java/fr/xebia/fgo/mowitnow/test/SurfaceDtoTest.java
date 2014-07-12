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
        SurfaceDto tester = SurfaceDto.buildSurface("10");
        fail("Invalid config did not generate exception");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrownTooMuchParms() {
        SurfaceDto tester = SurfaceDto.buildSurface("10 5 6");
        fail("Invalid config did not generate exception");
    }

    @Test
    public void testSucessfulBuilder() {
        SurfaceDto tester = SurfaceDto.buildSurface("8 4");
        assertEquals("width must be 10", 8, tester.getWidth());
        assertEquals("height must be 6", 4, tester.getHeight());
    }
}

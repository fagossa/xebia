/*
 * 
 */
package fr.xebia.fgo.mowitnow.test;

import fr.xebia.fgo.mowitnow.model.CoordinatesEnum;
import fr.xebia.fgo.mowitnow.model.MowerDto;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import org.junit.Test;

/**
 *
 * @author fagossa
 */
public class MowerDtoTest {

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrown() {
        MowerDto tester = MowerDto.buildMowerDto("1 1 C");
        fail("Invalid config did not generate exception");
    }

    @Test
    public void testSucessfulBuilder() {
        MowerDto tester = MowerDto.buildMowerDto("10 6 E");
        assertEquals("x must be 10", 10, tester.getX());
        assertEquals("y must be 6", 6, tester.getY());
        assertEquals("direction must be E", CoordinatesEnum.E, 
                tester.getDirection());
    }
}

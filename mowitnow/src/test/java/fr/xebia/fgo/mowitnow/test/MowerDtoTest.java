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
        MowerDto tester = new MowerDto.MowerBuilder("1 1 C").build();
        fail("Invalid config did not generate exception");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrownNoDirection() {
        MowerDto tester = new MowerDto.MowerBuilder("1 1").build();
        fail("Invalid config did not generate exception");
    }

    @Test
    public void testSucessfulBuilder() {
        MowerDto tester = new MowerDto.MowerBuilder("10 6 E").build();
        assertEquals("x must be 10", 10, tester.getX());
        assertEquals("y must be 6", 6, tester.getY());
        assertEquals("direction must be E", CoordinatesEnum.E, 
                tester.getDirection());
    }
}

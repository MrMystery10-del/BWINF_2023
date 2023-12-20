package de.zauberschule;

import org.junit.jupiter.api.Test;

class ZauberschuleTest {

    @Test
    void testZauberSchule(){
        var zauberschule = new Zauberschule("src/main/resources/beispiel.txt");
        System.out.println(zauberschule.writePath());
    }
}
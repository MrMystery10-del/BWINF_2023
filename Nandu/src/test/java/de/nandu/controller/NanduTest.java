package de.nandu.controller;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class NanduTest {

    @Test
    void transformInformation() {
        LightBoxField result1 = TaskReader.transformInformation(Path.of("src/main/resources/Beispiel1.txt"));
        LightBoxField result2 = TaskReader.transformInformation(Path.of("src/main/resources/Beispiel2.txt"));
        LightBoxField result3 = TaskReader.transformInformation(Path.of("src/main/resources/Beispiel3.txt"));
        LightBoxField result4 = TaskReader.transformInformation(Path.of("src/main/resources/Beispiel4.txt"));
        LightBoxField result5 = TaskReader.transformInformation(Path.of("src/main/resources/Beispiel5.txt"));

        System.out.println(result1.getOutputTable() + '\n');
        System.out.println(result2.getOutputTable() + '\n');
        System.out.println(result3.getOutputTable() + '\n');
        System.out.println(result4.getOutputTable() + '\n');
        System.out.println(result5.getOutputTable() + '\n');
    }
}
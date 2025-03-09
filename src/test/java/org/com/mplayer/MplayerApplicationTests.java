package org.com.mplayer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class MplayerApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(MplayerApplication.class);
        modules.verify();
    }
}

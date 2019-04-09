package com.unloadbrain.assignement.evbox.util;

import java.util.UUID;

/**
 * Testable utils class, nice alternative of PowerMock ugly tests
 * Provides uuid related utils
 */
public class UuidUtil {

    public String getRandomUuid() {
        return UUID.randomUUID().toString();
    }
}


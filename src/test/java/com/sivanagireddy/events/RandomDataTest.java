package com.sivanagireddy.events;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class RandomDataTest {

    @Test
    void testRandomData() {
        EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .seed(12345L)
                .objectPoolSize(100)
                .randomizationDepth(1)
                .charset(StandardCharsets.UTF_8)
                .stringLengthRange(5, 10)
                .build();

        User user = random.nextObject(User.class);
        System.out.println(user.firstName + " " + user.lastName);
        user = random.nextObject(User.class);
        System.out.println(user.firstName + " " + user.lastName);
        user = random.nextObject(User.class);
        System.out.println(user.firstName + " " + user.lastName);

        random.objects(User.class, 50).forEach(u -> System.out.println(u.firstName + " " + u.lastName));
    }
}

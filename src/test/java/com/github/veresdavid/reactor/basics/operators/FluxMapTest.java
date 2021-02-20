package com.github.veresdavid.reactor.basics.operators;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * 09. Mapping options for Flux
 *
 * In the following examples we take a look at how can we use mapping related operators on {@link Flux}es.
 */
public class FluxMapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxMapTest.class);

    @Test
    public void fluxWithMapTest() {
        // given
        Flux<String> flux = Flux.just("X-Wing", "Y-Wing")
            .log()
            .map(String::toUpperCase);

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("X-WING", "Y-WING")
            .verifyComplete();
    }

    @Test
    public void fluxWithFlatMapTest() {
        // given
        Flux<String> flux = Flux.just("id_chew", "id_wick")
            .log()
            .flatMap(this::findHeroById);

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        TestUtil.logSeparatorLine();

        // when - then
        // In this case, our test will consistently pass, as the findHeroById is not a real database query and it
        // emits the items immediately.
        // But, flatMap on it's own not guarantee that the order of the emitted items will be the same, which can be
        // bad for us if the order is important.
        // To handle such situations, check the next test where we use the flatMapSequential operator!
        StepVerifier.create(flux)
            .expectNext("Chewbacca", "Wicket")
            .verifyComplete();
    }

    @Test
    public void fluxWithFlatMapSequentialTest() throws InterruptedException {
        // given
        // The flatMapSequential will guarantee that the order of the items will stay the same after performing the
        // flat mapping.
        // Otherwise, if we used regular flatMap, then the Millenium Falcon would be the first item, then the
        // Imperial Star Destroyer.
        Flux<String> flux = Flux.just("id_sdes", "id_mfal")
            .log()
            .flatMapSequential(this::findShipById);

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        Thread.sleep(500);

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Imperial Star Destroyer", "Millenium Falcon")
            .verifyComplete();
    }

    // Helper method to simulate DB operation.
    private Mono<String> findHeroById(String id) {
        Mono<String> name = Mono.empty();

        if ("id_chew".equals(id)) {
            name = Mono.just("Chewbacca");
        } else if ("id_wick".equals(id)) {
            name = Mono.just("Wicket");
        }

        return name;
    }

    // Helper method to simulate DB operation, with a short delay on one of the items.
    private Mono<String> findShipById(String id) {
        Mono<String> ship = Mono.empty();

        if ("id_mfal".equals(id)) {
            ship = Mono.just("Millenium Falcon");
        } else if ("id_sdes".equals(id)) {
            ship = Mono.just("Imperial Star Destroyer")
                .delayElement(Duration.ofMillis(200));
        }

        return ship;
    }

}

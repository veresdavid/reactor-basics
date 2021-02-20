package com.github.veresdavid.reactor.basics.operators;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * 10. Flux with fallback
 *
 * We can fall back to another {@link Flux}, in case the original one was empty.
 */
public class FluxFallbackTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxFallbackTest.class);

    @Test
    public void fluxWithSwitchIfEmptyTest() {
        // given
        Flux<Object> flux = Flux.empty()
            .log()
            .switchIfEmpty(Flux.just("Master Yoda", "Mace Windu"));

        // manual try
        flux.subscribe(o -> LOGGER.info("Value = {}", o));

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Master Yoda", "Mace Windu")
            .verifyComplete();
    }

}

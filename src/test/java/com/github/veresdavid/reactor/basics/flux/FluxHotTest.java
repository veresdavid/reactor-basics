package com.github.veresdavid.reactor.basics.flux;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * 07. Creating a hot Flux
 *
 * By default, if we crate a {@link Flux} it will be cold, meaning that it won't emit any items until it receives a
 * subscription. We can also create hot {@link Flux}es, which will start emitting items immediately. In this test case
 * we take a look at this option.
 */
public class FluxHotTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxHotTest.class);

    @Test
    public void fluxToHotFluxTest() throws InterruptedException {
        // given
        ConnectableFlux<Integer> flux = connectableFlux();

        // manual try
        // With calling the connect method on a ConnectableFlux, it will start emitting items immediately, doesn't
        // matter if it has any subscriptions or not.
        flux.connect();

        // We perform a short sleep, so when we subscribe we will see that some items were already emitted and we
        // won't receive them.
        Thread.sleep(300);

        flux.subscribe(integer -> LOGGER.info("{} little ewok", integer));

        Thread.sleep(500);

        TestUtil.logSeparatorLine();

        // when - then
        // For this example, we create a new Connectable just for the test case, as the previous one is already
        // completed and it has no more items, plus we have to manually connect to it.
        // To keep things simple, we skip the virtual time solution this time.
        ConnectableFlux<Integer> fluxToTest = connectableFlux();
        StepVerifier.create(fluxToTest)
            .then(fluxToTest::connect)
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete();
    }

    private ConnectableFlux<Integer> connectableFlux() {
        return Flux.range(1, 5)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish();
    }

}

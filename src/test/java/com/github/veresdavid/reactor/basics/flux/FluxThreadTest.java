package com.github.veresdavid.reactor.basics.flux;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/**
 * 06. Flux with threads
 *
 * We have the option to specify on which threads should some of our callbacks run. In the tests below, we take a look
 * at how can we do it.
 */
public class FluxThreadTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxThreadTest.class);

    @Test
    public void fluxWithSubscribeOn() {
        // given
        // In short, subscribeOn will affect where some of our callbacks, like doOnSubscribe, doOnNext, etc. will run.
        // This affects our whole chain, but publishOn (see it on the next example) can override it.
        Flux<String> flux = Flux.just("Admiral Ackbar", "Mon Mothma")
            .log()
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSubscribe(subscription -> LOGGER.info("doOnSubscribe on thread: {}", Thread.currentThread().getName()))
            .doOnNext(s -> LOGGER.info("doOnNext on thread: {}", Thread.currentThread().getName()));

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Admiral Ackbar", "Mon Mothma")
            .verifyComplete();
    }

    @Test
    public void fluxWithPublishOn() {
        // given
        // Operator publishOn will affect where our doOnNext, doOnError and doOnComplete callbacks will run.
        // This only affects the operators in the chain below it!
        // Thus, publishOn can override a previous publishOn, or even a subscribeOn.
        Flux<String> flux = Flux.just("Jabba", "Bib Fortuna")
            .publishOn(Schedulers.single())
            .map(s -> {
                LOGGER.info("On thread: {}", Thread.currentThread().getName());
                return s;
            })
            .publishOn(Schedulers.boundedElastic())
            .map(s -> {
                LOGGER.info("On thread: {}", Thread.currentThread().getName());
                return s;
            });

        // manual try
        flux.subscribe(s -> LOGGER.info("{}", s));

        // when - then
    }

}

package com.github.veresdavid.reactor.basics.operators;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * 12. Error handling while combining Fluxes
 *
 * We have a few fancy options on have we would like to handle exceptions. You can see some examples for it in the
 * tests below.
 */
public class FluxCombineErrorHandlingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxCombineErrorHandlingTest.class);

    @Test
    public void fluxWithConcatDelayErrorTest() {
        // given
        // If we concat two Fluxes, but the first one finishes with error, then the concatenated Flux will also finish,
        // further items from other sources won't be emitted.
        // To solve this problem, we can delay the errors with concatDelayError, so they will be only emitted after
        // we successfully get the correct items.
        Flux<String> apprentices = Flux.just("id_maul")
            .map(this::findApprenticeById);
        Flux<String> generals = Flux.just("General Grievous");
        Flux<String> flux = Flux.concatDelayError(apprentices, generals)
            .log();

        // manual try
        flux.subscribe(
            s -> LOGGER.info("Value = {}", s),
            throwable -> LOGGER.error("Something went wrong: {}", throwable.getMessage())
        );

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("General Grievous")
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void fluxWithMergeDelayErrorTest() throws InterruptedException {
        // given
        // Like with concat, we also have the option to delay errors in case of the merge operator as well.
        // Here, we delay the emission of Plo Koon, so the error from the first Flux will be emitted faster, but as
        // we delay the error, first we will receive Plo Koon, then the exception.
        Flux<Object> error = Flux.error(new RuntimeException("Master not found"));
        Flux<String> master = Flux.just("Plo Koon")
            .delayElements(Duration.ofMillis(200));
        Flux<Object> flux = Flux.mergeDelayError(1, error, master)
            .log();

        // manual try
        flux.subscribe(o -> LOGGER.info("Value = {}", o));

        Thread.sleep(500);

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Plo Koon")
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void fluxWithCheckedExceptionPropagateTest() {
        // given
        // Sometimes it can happen that we have to deal with checked exceptions, inside a map operator, for example.
        // In this case, we can use the Exceptions.propagate method to wrap our checked exception and rethrow it.
        // In this case, a new RuntimeException would be thrown, which we can catch in our errorConsumer. We will
        // receive the original typed exception in errorConsumer.
        Flux<String> flux = Flux.just("Qui-Gon Jinn")
            .log()
            .map(s -> {
                try {
                    return trainTheChosenOneBy(s);
                } catch (Exception e) {
                    throw Exceptions.propagate(e);
                }
            });

        // manual try
        flux.subscribe(
            s -> LOGGER.info("This will never happen"),
            throwable -> LOGGER.error("Error: {}", throwable.getMessage())
        );

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectError(TooOldException.class)
            .verify();
    }

    // Helper method to simulate DB operation.
    private String findApprenticeById(String id) {
        throw new RuntimeException("Apprentice with ID '" + id + "' not found");
    }

    // Helper method to throw checked exception.
    private String trainTheChosenOneBy(String name) throws Exception {
        throw new TooOldException("The chosen one is too old");
    }

    // Helper checked exception.
    private static class TooOldException extends Exception {
        public TooOldException(String message) {
            super(message);
        }
    }

}

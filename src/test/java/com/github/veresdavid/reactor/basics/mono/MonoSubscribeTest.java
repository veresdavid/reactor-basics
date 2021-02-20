package com.github.veresdavid.reactor.basics.mono;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 02. Subscribe to Mono
 *
 * While subscribing to a {@link Mono}, we can provide some callback methods which will be triggered
 * by specific signals. We will provide:
 * - a consumer of an item
 * - an error consumer
 * - a callback upon the completion signal
 * - a subscription consumer
 *
 * We can also see an example to the map operator.
 */
public class MonoSubscribeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonoSubscribeTest.class);

    @Test
    public void monoWithConsumer() {
        // given
        Mono<String> mono = Mono.just("Han Solo")
            .log();

        // manual try
        mono.subscribe(s -> LOGGER.info("Value = {}", s));

        // when - then
        StepVerifier.create(mono)
            .expectNext("Han Solo")
            .verifyComplete();
    }

    @Test
    public void monoWithErrorConsumer() {
        // given
        Mono<String> mono = Mono.just("Leia Organa")
            .log()
            .map(this::throwRuntimeExceptionOnString);

        // manual try
        mono.subscribe(
            s -> LOGGER.info("This will never happen..."),
            throwable -> LOGGER.error("Alert: {}", throwable.getMessage())
        );

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(mono)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void monoWithCompleteConsumer() {
        // given
        Mono<String> mono = Mono.just("Lando Calrissian")
            .log();

        // manual try
        mono.subscribe(
            s -> LOGGER.info("Value = {}", s),
            null,
            () -> LOGGER.info("Mono finished it's job!")
        );

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(mono)
            .expectNext("Lando Calrissian")
            .verifyComplete();
    }

    @Test
    public void monoWithSubscriptionConsumer() {
        // given
        Mono<String> mono = Mono.just("Owen Lars")
            .log();

        // manual try
        mono.subscribe(
            s -> LOGGER.info("Value = {}", s),
            null,
            null,
            subscription -> {
                LOGGER.info("We got our subscription!");
                LOGGER.info("Requesting 1 item...");
                subscription.request(1);
            }
        );

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(mono)
            .expectNext("Owen Lars")
            .verifyComplete();
    }

    private String throwRuntimeExceptionOnString(String string) {
        if ("Leia Organa".equals(string)) {
            throw new RuntimeException(string + " is a rebel scum!");
        }
        return string;
    }

}

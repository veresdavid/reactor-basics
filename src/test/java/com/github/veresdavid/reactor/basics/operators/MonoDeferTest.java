package com.github.veresdavid.reactor.basics.operators;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * 13. Mono with defer
 *
 * With defer we can create a {@link Mono} provider, that will supply a new {@link Mono} each time we subscribe to it.
 */
public class MonoDeferTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonoDeferTest.class);

	@Test
	public void monoWithDeferTest() throws InterruptedException {
		// given
		// The operator defer will provide a new Mono each time we subscribe.
		// In this example, it means that we will see different timestamps on the output.
		// Otherwise, if we would used the just operator, we would get the same timestamps everytime.
		Mono<Long> mono = Mono.defer(() -> Mono.just(System.currentTimeMillis()));

		// manual try
		mono.subscribe(aLong -> LOGGER.info("Value = {}", aLong));
		Thread.sleep(500);
		mono.subscribe(aLong -> LOGGER.info("Value = {}", aLong));
		Thread.sleep(500);
		mono.subscribe(aLong -> LOGGER.info("Value = {}", aLong));

		// when - then
		// No tests for this time, only manual try.
	}

}

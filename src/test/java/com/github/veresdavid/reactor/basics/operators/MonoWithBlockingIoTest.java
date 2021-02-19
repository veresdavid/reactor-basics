package com.github.veresdavid.reactor.basics.operators;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/**
 * 08. Mono with blocking IO operation
 *
 * In this example let's take a look at how can we handle blocking IO operations to run on a different thread and
 * not block the main thread.
 */
public class MonoWithBlockingIoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonoWithBlockingIoTest.class);

	@Test
	public void monoWithBlockingIoTest() throws InterruptedException {
		// given
		// We can also create a Mono from a return value of a Callable, by passing it to the fromCallable.
		// In this case, we will simulate that we are calling an external service that takes some time to respond.
		// In this case it is preferred to provide a Scheduler for the subscribeOn method, so our IO operation
		// won't block the main thread.
		Mono<String> mono = Mono.fromCallable(this::callEmperorService)
			.log()
			.subscribeOn(Schedulers.boundedElastic());

		// manual try
		mono.subscribe(s -> LOGGER.info("Value = {}", s));

		Thread.sleep(1500);

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("Darth Sidious")
			.verifyComplete();
	}

	// Helper method for simulating an external service call.
	private String callEmperorService() {
		LOGGER.info("{}", Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOGGER.error("The emperor is dead", e);
		}
		return "Darth Sidious";
	}

}

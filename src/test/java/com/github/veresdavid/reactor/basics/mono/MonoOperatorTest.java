package com.github.veresdavid.reactor.basics.mono;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 03. Mono operators
 *
 * In this test suite, we take a look at some of the available operators for {@link Mono}.
 *
 * Note, not all the operators are covered here, for more of them please check the official documentation!
 *
 * We also try out more operators in the {@link Flux} examples.
 */
public class MonoOperatorTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonoOperatorTest.class);

	@Test
	public void monoDoOnOperators() {
		// given
		Mono<String> mono = Mono.just("Beru Lars")
			.log()
			.doOnSubscribe(subscription -> LOGGER.info("Got a new subscription!"))
			.doOnRequest(value -> LOGGER.info("Number of items requested = {}", value))
			.doOnNext(s -> LOGGER.info("Emitting value = {}", s))
			.doOnSuccess(s -> LOGGER.info("Finished with {}", s));

		// manual try
		mono.subscribe(
			null,
			null,
			null,
			subscription -> subscription.request(1)
		);

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("Beru Lars")
			.verifyComplete();
	}

	@Test
	public void monoMapOperator() {
		// given
		Mono<String> mono = Mono.just("Wedge Antilles")
			.log()
			.map(String::toUpperCase);

		// manual try
		mono.subscribe(s -> LOGGER.info("Value = {}", s));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("WEDGE ANTILLES")
			.verifyComplete();
	}

	@Test
	public void monoErrorHandlingFallbackMono() {
		// given
		Mono<Object> mono = Mono.error(new RuntimeException("Who is Ben Kenobi?"))
			.log()
			.onErrorResume(throwable -> {
				LOGGER.error("Got error: {}", throwable.getMessage());
				LOGGER.info("Using fallback Mono...");
				return Mono.just("Obi-Wan Kenobi");
			});

		// manual try
		mono.subscribe(o -> LOGGER.info("Value = {}", o));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("Obi-Wan Kenobi")
			.verifyComplete();
	}

	@Test
	public void monoErrorHandlingFallbackValue() {
		// given
		Mono<Object> mono = Mono.error(new RuntimeException("Darth Vader"))
			.log()
			.onErrorReturn("Anakin Skywalker");

		// manual try
		mono.subscribe(o -> LOGGER.info("Value = {}", o));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("Anakin Skywalker")
			.verifyComplete();
	}

}

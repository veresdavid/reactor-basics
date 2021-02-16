package com.github.veresdavid.reactor.basics.mono;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 01. Mono creation
 *
 * This class provides a few examples on how can we create a {@link Mono}.
 *
 * In the examples, we already use an operator called log, to make it visible in the logs what is happening
 * with our {@link Mono}.
 */
public class MonoCreationTest {

	@Test
	public void monoWithOneItem() {
		// given
		Mono<String> mono = Mono.just("Luke Skywalker")
			.log();

		// manual try
		mono.subscribe();

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.expectNext("Luke Skywalker")
			.verifyComplete();
	}

	@Test
	public void monoWithZeroItem() {
		// given
		Mono<Object> mono = Mono.empty()
			.log();

		// manual try
		mono.subscribe();

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(mono)
			.verifyComplete();
	}

	@Test
	public void monoWithError() {
		// given
		Mono<Object> mono = Mono.error(new RuntimeException("Death Star explodes!"));

		// manual try
		mono.subscribe();

		// when - then
		StepVerifier.create(mono)
			.expectError(RuntimeException.class)
			.verify();
	}

}

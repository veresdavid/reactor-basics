package com.github.veresdavid.reactor.basics.flux;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

/**
 * 04. Flux creation
 *
 * This class provides some examples on how can we create {@link Flux}es.
 */
public class FluxCreationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FluxCreationTest.class);

	@Test
	public void fluxWithItems() {
		// given
		Flux<String> flux = Flux.just("R2-D2", "C3-PO")
			.log();

		// manual try
		flux.subscribe(s -> LOGGER.info("Value = {}", s));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(flux)
			.expectNext("R2-D2", "C3-PO")
			.verifyComplete();
	}

	@Test
	public void fluxWithZeroItem() {
		// given
		Flux<Object> flux = Flux.empty()
			.log();

		// manual try
		flux.subscribe();

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(flux)
			.verifyComplete();
	}

	@Test
	public void fluxWithError() {
		// given
		Flux<Object> flux = Flux.error(new RuntimeException("Second Death Start explodes!"))
			.log();

		// manual try
		flux.subscribe(
			o -> LOGGER.info("This will never happen!"),
			Throwable::printStackTrace
		);

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(flux)
			.expectError(RuntimeException.class)
			.verify();
	}

	@Test
	public void fluxWithRange() {
		// given
		Flux<Integer> flux = Flux.range(1, 5)
			.log();

		// manual try
		flux.subscribe(integer -> LOGGER.info("Captain Solo, this is Rogue {}", integer));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(flux)
			.expectNext(1, 2, 3, 4, 5)
			.verifyComplete();
	}

	@Test
	public void fluxFromIterable() {
		// given
		List<String> walkers = List.of("AT-AT", "AT-ST");
		Flux<String> flux = Flux.fromIterable(walkers)
			.log();

		// manual try
		flux.subscribe(s -> LOGGER.info("Value = {}", s));

		TestUtil.logSeparatorLine();

		// when - then
		StepVerifier.create(flux)
			.expectNext("AT-AT", "AT-ST")
			.verifyComplete();
	}

}

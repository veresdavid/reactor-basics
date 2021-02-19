package com.github.veresdavid.reactor.basics.flux;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * 05. Flux with delayed items
 *
 * Here we take a look at how can we delay the items of a {@link Flux} and how can we write tests for such cases.
 */
public class FluxDelayTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(FluxDelayTest.class);

	@Test
	public void fluxWithInterval() throws InterruptedException {
		// given
		// Here we create an endless Flux which emits items every 100 milliseconds, from 0 to n,
		// but we only take 5 of them.
		Flux<Long> flux = Flux.interval(Duration.ofMillis(100))
			.log()
			.take(5);

		// manual try
		flux.subscribe(aLong -> LOGGER.info("{} little jawa", aLong));

		// We perform a short manual sleep here, to make sure we get all the items from our Flux.
		Thread.sleep(1000);

		TestUtil.logSeparatorLine();

		// when - then
		// We expect for the first 5 items, then cancel our subscription and verify our result.
		// This is not a very effective way of writing test for a Flux which delays it's items, as in this case the
		// StepVerifier will also wait the delays. For a more effective way of testing such cases, check the following
		// test cases!
		StepVerifier.create(flux)
			.expectNext(0L, 1L, 2L, 3L, 4L)
			.thenCancel()
			.verify();
	}

	@Test
	public void fluxWithIntervalEffectiveTest() {
		// given
		// In this case, we created a Flux which emits items with a 1 day delay.
		Flux<Long> flux = Flux.interval(Duration.ofDays(1))
			.log()
			.take(2);

		// manual try
		// We don't want to wait for days here, so we commented out the manual test.
		// flux.subscribe(aLong -> LOGGER.info("Day {} - Drink your blue milk!", aLong));

		// when - then
		// We can use a VirtualTimeScheduler to test Fluxes with delays. With this, we can actually skip x amount of
		// time then make assertions.
		// To ensure the VirtualTimeScheduler is working for our Flux, we should instantiate the Flux inside the
		// callback of the withVirtualTime method!
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofDays(1)).log().take(2))
			.expectSubscription()
			.expectNoEvent(Duration.ofDays(1))
			.expectNext(0L)
			.thenAwait(Duration.ofDays(1))
			.expectNext(1L)
			.thenCancel()
			.verify();
	}

	@Test
	public void fluxWithDelayedCustomItemsTest() throws InterruptedException {
		// given
		Flux<String> flux = fluxOfHeadhunters();

		// manual try
		flux.subscribe(s -> LOGGER.info("Value = {}", s));

		Thread.sleep(2500);

		TestUtil.logSeparatorLine();

		// when - then
		// When testing delayed Fluxes, we can await more time, then assert to all the emitted items during the
		// passed time.
		StepVerifier.withVirtualTime(this::fluxOfHeadhunters)
			.expectSubscription()
			.thenAwait(Duration.ofSeconds(2))
			.expectNext("Boba Fett", "Greedo")
			.verifyComplete();
	}

	private Flux<String> fluxOfHeadhunters() {
		return Flux.just("Boba Fett", "Greedo")
			.log()
			.delayElements(Duration.ofSeconds(1));
	}

}

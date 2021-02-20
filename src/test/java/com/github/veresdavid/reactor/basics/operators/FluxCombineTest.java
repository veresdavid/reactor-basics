package com.github.veresdavid.reactor.basics.operators;

import com.github.veresdavid.reactor.basics.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

import java.time.Duration;

/**
 * 11. Combining Fluxes
 *
 * We can combine many {@link Flux}es to a new one. In this test suite, we take a look at some of our options.
 */
public class FluxCombineTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FluxCombineTest.class);

    @Test
    public void fluxWithConcatTest() {
        // given
        // With the concat operator, we can concatenate the items of 2 or more Fluxes. It will always wait for the first
        // Flux to be completed, then go for the remaining sources.
        // In this example, we can be sure about that Padmé Amidala will be the first item, followed by Bail Organa.
        Flux<String> senator1 = Flux.just("Padmé Amidala");
        Flux<String> senator2 = Flux.just("Bail Organa");
        Flux<String> flux = Flux.concat(senator1, senator2)
            .log();

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Padmé Amidala", "Bail Organa")
            .verifyComplete();
    }

    @Test
    public void fluxWithCombineLatestTest() throws InterruptedException {
        // given
        // As it's name says, we can use combineLatest to combine the most recent items of 2 or more Fluxes.
        // When an item is emitted from one of the sources, and there is also an item from the other(s), we call the
        // combinator method and produce a new value.
        // In this example, we put a small delay on the jedi Flux, so we can make sure that the rank Flux is already
        // emitted all of it's items and we will always get Master from it as a latest item.
        Flux<String> rank = Flux.just("Jedi Knight", "Master");
        Flux<String> jedi = Flux.just("Ki-Adi-Mundi", "Plo Koon")
            .delayElements(Duration.ofMillis(100));
        Flux<String> flux = Flux.combineLatest(rank, jedi, (s1, s2) -> s1 + " " + s2)
            .log();

        // manual try
        flux.subscribe(s -> LOGGER.info("Value = {}", s));

        Thread.sleep(500);

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Master Ki-Adi-Mundi", "Master Plo Koon")
            .verifyComplete();
    }

    @Test
    public void fluxWithMergeTest() throws InterruptedException {
        // given
        // Unlike the concat operator, merge will eagerly get items from the given sources.
        // In this example, we put a short delay on the first source, then merge it with the second source, which is
        // without delay. We will see, that we get Aayla Secura first then Shaak Ti, as merge will not wait for the
        // first source to finish, it will grab the items whenever they are ready.
        Flux<String> master1 = Flux.just("Shaak Ti")
            .delayElements(Duration.ofMillis(300));
        Flux<String> master2 = Flux.just("Aayla Secura");
        Flux<String> flux = Flux.merge(master1, master2)
            .log();

        // manual try
        flux.subscribe(s -> LOGGER.info("{}", s));

        Thread.sleep(500);

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNext("Aayla Secura", "Shaak Ti")
            .verifyComplete();
    }

    @Test
    public void fluxWithZipTest() {
        // given
        // With zip, we can combine items from many sources into one new item. For example, the new first item will
        // consist of the first items of the provided source, the new second item will consist of the second ones,
        // and so on.
        // The result of the zip will be a Tuple of n elements, so we also use the map operator here to convert the
        // Tuples to a business object (Jedi in this case).
        Flux<String> name = Flux.just("Luminara Unduli", "Bariss Offee");
        Flux<String> rank = Flux.just("Master", "Padawan");
        Flux<String> saberColor = Flux.just("green", "blue");
        Flux<Jedi> flux = Flux.zip(name, rank, saberColor)
            .log()
            .map(this::tupleToJedi);

        // manual try
        flux.subscribe(jedi -> LOGGER.info("{}", jedi));

        TestUtil.logSeparatorLine();

        // when - then
        StepVerifier.create(flux)
            .expectNextCount(2)
            .verifyComplete();
    }

    // Helper method to convert a Tuple of 3 Strings to a Jedi object.
    private Jedi tupleToJedi(Tuple3<String, String, String> tuple) {
        return new Jedi(tuple.getT1(), tuple.getT2(), tuple.getT3());
    }

    // Helper class to represent a jedi.
    // For simplicity, it only contains the necessary methods.
    private static class Jedi {
        private final String name;
        private final String rank;
        private final String saberColor;

        public Jedi(String name, String rank, String saberColor) {
            this.name = name;
            this.rank = rank;
            this.saberColor = saberColor;
        }

        @Override
        public String toString() {
            return "Jedi{" +
                "name='" + name + '\'' +
                ", rank='" + rank + '\'' +
                ", saberColor='" + saberColor + '\'' +
                '}';
        }
    }

}

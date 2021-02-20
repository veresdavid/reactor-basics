# Reactor Basics

A small collection of basic Reactor Core examples, with a bit of Star Wars flavor.

## Purpose

This collection showcases the most basic operations provided by Reactor Core, which
can be useful for those who are:
* new to Reactor and want to learn the basics
* only rarely using Reactor and want to refresh their knowledge

## Background

Before creating this project, I was brand new to Reactor and started to go through
different tutorials, meanwhile writing some dummy code for trying things out. Then
I thought it would be nice to have a categorized collection of basic examples,
that are well written and easy to understand. So later on, if don't use Reactor
for a while and forget things, I can just quickly go through the examples and
catch up things. But not just me, anyone who is in a similar situation can
refer to it and learn the basics. So was born this repository.

## Sources

Shout out to the [official Reactor organisation on GitHub](https://github.com/reactor)
and to the [DevDojo Academy on YouTube](https://www.youtube.com/c/DevDojoAcademy),
the example codes have been created based on their awesome tutorials!

If you are new to Reactor, I would also like to recommend you these tutorials:
* [Reactive Programming with Reactor 3 by Reactor - Tech.io playground](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro)
* [Project Reactor Essentials by DevDojo Academy - YouTube playlist](https://www.youtube.com/watch?v=lCTUOERTXyw&list=PL0Un1HNdB4jFCsHsQg2HOfO03XfECuMiw)

## Structure

All the different examples have been written in the form of unit test cases (inspired
by the DevDojo Academy tutorials). This way we can learn how to use Reactor and also
learn how can we write unit tests for such code.

Each case can be separated to 3 main parts:
* **given** : As in case of a regular unit test, we set up the things here that we
want to test.
* **manual try** : In this section, we manually try out the things we set up above
with some logging.
* **when - then** : The when and then sections of a unit test has been merged, as
we executing the tested functionality and writing assertions to it in the same
time.

### Example

```java
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
```

## Chapters

1. Mono
    1. [Mono creation](src/test/java/com/github/veresdavid/reactor/basics/mono/MonoCreationTest.java)
    2. [Subscribe to Mono](src/test/java/com/github/veresdavid/reactor/basics/mono/MonoSubscribeTest.java)
    3. [Mono operators](src/test/java/com/github/veresdavid/reactor/basics/mono/MonoOperatorTest.java)
2. Flux
    1. [Flux creation](src/test/java/com/github/veresdavid/reactor/basics/flux/FluxCreationTest.java)
    2. [Flux with delayed items](src/test/java/com/github/veresdavid/reactor/basics/flux/FluxDelayTest.java)
    3. [Flux with threads](src/test/java/com/github/veresdavid/reactor/basics/flux/FluxThreadTest.java)
    4. [Creating a hot Flux](src/test/java/com/github/veresdavid/reactor/basics/flux/FluxHotTest.java)
3. More operators
    1. [Mono with blocking IO operation](src/test/java/com/github/veresdavid/reactor/basics/operators/MonoWithBlockingIoTest.java)
    2. [Mapping options for Flux](src/test/java/com/github/veresdavid/reactor/basics/operators/FluxMapTest.java)
    3. [Flux with fallback](src/test/java/com/github/veresdavid/reactor/basics/operators/FluxFallbackTest.java)
    4. [Combining Fluxes](src/test/java/com/github/veresdavid/reactor/basics/operators/FluxCombineTest.java)
    5. [Error handling while combining Fluxes](src/test/java/com/github/veresdavid/reactor/basics/operators/FluxCombineErrorHandlingTest.java)
    6. [Mono with defer](src/test/java/com/github/veresdavid/reactor/basics/operators/MonoDeferTest.java)

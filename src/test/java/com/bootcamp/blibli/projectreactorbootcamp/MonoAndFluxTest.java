package com.bootcamp.blibli.projectreactorbootcamp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;


@Slf4j
public class MonoAndFluxTest {
  public void constructMonoAndFluxTest() {
    Flux<String> fluxAlphabetJust = Flux.just("a", "b", "c", "d", "e");
    Flux<String> fluxAlphabetFromArray = Flux.fromArray(new String[]{"a", "b", "c", "d", "e"});
    Flux<String> fluxAlphabetFromIterable = Flux.fromIterable(Arrays.asList("a", "b", "c", "d", "e"));
    Flux<String> fluxAlphabetFromStream = Flux.fromStream(Stream.of("a", "b", "c", "d", "e"));

    Mono<String> monoAlphabetJust = Mono.just("a");
  }

  @Test
  public void fluxTest() {
    Flux<String> fluxAlphabetJust = Flux.just("a", "b", "c", "d", "e");
//        Flux<String> fluxAlphabetTransformed = fluxAlphabetJust.log()
//                .map(String::toUpperCase);
    Flux<String> fluxAlphabetTransformed = fluxAlphabetJust.log()
            .map(String::toUpperCase);

    StepVerifier.create(fluxAlphabetTransformed)
            .expectNext("A")
            .expectNext("B")
            .expectNext("C")
            .expectNext("D")
            .expectNext("E")
            .verifyComplete();
  }

  @Test
  public void fluxTestParallel() {
    Flux<String> fluxAlphabetJust = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
    fluxAlphabetJust.log()
            .map(String::toUpperCase)
            .flatMap(alphabet -> Mono.just(alphabet).subscribeOn(Schedulers.parallel()))
            .subscribe(s -> log.info("Consumed: {}", s));
  }

  @Test
  public void fluxTestWithBackPressure() {
    Flux<String> fluxAlphabetJust = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
    fluxAlphabetJust.log().subscribe(new Subscriber<String>() {

      private long count = 0;
      private Subscription subscription;

      @Override
      public void onSubscribe(Subscription s) {
        this.subscription = s;
        s.request(3);
      }

      @Override
      public void onNext(String s) {
        count++;
        if(count == 3) {
          this.subscription.request(2);
          count = 0;
        }
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onComplete() {

      }
    });
  }

  @Test
  public void fluxTestMergeWith() {
    Flux<String> fluxAlphabet1 = Flux.just("a", "b", "c")
            .log()
            .delayElements(Duration.ofMillis(100));
    Flux<String> fluxAlphabet2 = Flux.just("d", "e", "f")
            .log()
            .delaySubscription(Duration.ofMillis(50))
            .delayElements(Duration.ofMillis(100));

    Flux<String> fluxAlphabetMerged = fluxAlphabet1.mergeWith(fluxAlphabet2);

    StepVerifier.create(fluxAlphabetMerged)
            .expectNext("a")
            .expectNext("d")
            .expectNext("b")
            .expectNext("e")
            .expectNext("c")
            .expectNext("f")
            .verifyComplete();
  }
}


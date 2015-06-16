package kuvaldis.play.gpars

import groovyx.gpars.GParsExecutorsPool
import groovyx.gpars.GParsExecutorsPoolEnhancer
import groovyx.gpars.GParsPool
import groovyx.gpars.ParallelEnhancer
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class ParallelSpec extends Specification {

    // uses fork join pool
    def "calculate integers"() {
        given:
        final result = new AtomicInteger();
        final array = [1, 2, 3, 4, 5]
        when:
        GParsPool.withPool(3) {
            array.eachParallel { result.addAndGet(it) }
        }
        then:
        15 == result.intValue()
    }

    def "calculate with parallel enhancer"() {
        given:
        final integers = [1, 2, 3, 4, 5]
        // mix parallel methods
        ParallelEnhancer.enhanceInstance integers
        when:
        final result = integers.minParallel();
        then:
        1 == result
    }

    def "sequential and concurrent print"() {
        given:
        final integers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0]
        when:
        GParsPool.withPool {
            integers.makeConcurrent()
            println 'Parallel print:'
            integers.each { print it + ' ' } // will print something like 6 7 2 8 9 0 1 3 4 5
            println()
            integers.makeSequential()
            println 'Sequential print'
            integers.each { print it + ' ' }
            println()
            integers.asConcurrent {
                println 'Concurrent context print'
                integers.each { print it + ' ' }
            }
            println()
            println 'Still sequential print'
            integers.each { print it + ' ' }
        }
        then:
        true
    }

    // operations are run on ExecutorService
    def "executors pool"() {
        given:
        final integers = [1, 2, 3, 4, 5]
        def futures
        when:
        GParsExecutorsPool.withPool(3) {
            final closure = { it * 10 }
            final asyncClosure = closure.async()
            futures = integers.collect asyncClosure
        }
        then:
        [10, 20, 30, 40, 50] as Set == (futures*.get()) as Set
    }

    def "executors pool enhancer"() {
        given:
        final integers = [1, 2, 3, 4, 5]
        GParsExecutorsPoolEnhancer.enhanceInstance(integers)
        when:
        final result = integers.collectParallel { it * 10 }
        then:
        [10, 20, 30, 40, 50] as Set == result as Set
    }

    def "closure caching should remember call results and not to perform closure body"() {
        given:
        final num = 7
        def result;
        def calls = []
        when:
        GParsPool.withPool {
            final fib
            fib = { n ->
                calls << n
                n > 1 ? fib(n - 1) + fib(n - 2) : n
            }.memoize()
            result = fib(num)
        }
        then:
        13 == result
        and:
        calls.size() == 8
        [0, 1, 2, 3, 4, 5, 6, 7] as Set == calls as Set
    }

}

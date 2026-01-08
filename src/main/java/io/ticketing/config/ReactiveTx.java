package io.ticketing.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@Component
public class ReactiveTx {

    private final TransactionalOperator tx;

    public ReactiveTx(TransactionalOperator tx) {
        this.tx = tx;
    }

    /** Default REQUIRED transaction */
    public <T> Mono<T> required(Supplier<Mono<T>> work) {
        return work.get().as(tx::transactional);
    }

    /** Default REQUIRED transaction */
    public <T> Flux<T> requiredMany(Supplier<Flux<T>> work) {
        return work.get().as(tx::transactional);
    }

//    /** Customizable transaction definition (isolation/timeout/etc) */
//    public <T> Mono<T> withDefinition(DefaultTransactionDefinition def, Supplier<Mono<T>> work) {
//        TransactionalOperator custom = TransactionalOperator.create(tx.getTransactionManager(), def);
//        return work.get().as(custom::transactional);
//    }

    public static DefaultTransactionDefinition readCommitted() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return def;
    }
}
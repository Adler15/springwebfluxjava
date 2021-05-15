package com.adler.springwebfluxjava.dao;

import com.adler.springwebfluxjava.domain.Book;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface BookRepository extends ReactiveSortingRepository<Book,Long> {

    Flux<Book> findByAuthor(String author);
}

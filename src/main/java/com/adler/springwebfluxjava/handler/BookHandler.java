package com.adler.springwebfluxjava.handler;

import com.adler.springwebfluxjava.dao.BookRepository;
import com.adler.springwebfluxjava.domain.Book;
import com.adler.springwebfluxjava.utils.PageInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BookHandler {

    @Resource
    private BookRepository bookRepository;
    @Resource
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<ServerResponse> addOneBook(ServerRequest request) {
        return request.bodyToMono(String.class).flatMap(
                bookString -> {
                    Mono<Book> savedBook = Mono.empty();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Book value = mapper.readValue(bookString, Book.class);
                        savedBook = bookRepository.save(value);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return ServerResponse.ok().body(savedBook, Book.class);
                }
        );
    }

    public Mono<ServerResponse> updateOneBook(ServerRequest request) {
        return request.bodyToMono(String.class).flatMap(
                bookString -> {
                    Mono<Book> updatedBook = Mono.empty();
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        Book value = mapper.readValue(bookString, Book.class);
                        updatedBook = bookRepository.save(value);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return ServerResponse.ok().body(updatedBook, Book.class);
                }
        );
    }

    public Mono<ServerResponse> getBookById(ServerRequest request){
        return ServerResponse.ok().body(
                bookRepository.findById(Long.valueOf(request.pathVariable("bookId"))),
                Book.class);
    }

    public Mono<ServerResponse> findBookById(ServerRequest request){
        RequestPath requestPath = request.requestPath();
        System.out.println("请求的路径："+requestPath.toString());
        Optional<String> id = request.queryParam("id");
        String sid = id.get();
        System.out.println("queryParam:"+sid);
        MultiValueMap<String, String> requestMap = request.queryParams();
        requestMap.keySet().forEach(it->{
            System.out.println("key:"+it);
            System.out.println("value:"+requestMap.toSingleValueMap().get(it));
        });
        return ServerResponse.ok().body(
                bookRepository.findById(Long.valueOf(sid)),
                Book.class);
    }

    public Mono<ServerResponse> getAll(ServerRequest request){
        return ServerResponse.ok().body(
                bookRepository.findAll(Sort.by(Sort.Direction.ASC,"price")),
                Book.class
        );
    }

    public Mono<ServerResponse> getPage(ServerRequest request){
        Map<String, String> requestMap = request.queryParams().toSingleValueMap();
        Long current = Long.valueOf(requestMap.get("current"));
        Long size = Long.valueOf(requestMap.get("size"));
        Flux<Book> bookFlux = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "price"))
                .skip((current - 1) * size).take(size);
        return ServerResponse.ok().body(bookFlux,Book.class);
    }

    public Mono<ServerResponse> getByAuthor(ServerRequest request){
        Map<String, String> requestMap = request.queryParams().toSingleValueMap();
        Integer current = Integer.valueOf(requestMap.getOrDefault("current", "1"));
        Integer size = Integer.valueOf(requestMap.getOrDefault("size", "10"));
        String author = requestMap.get("author");
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(current,size,sort);
        Flux<Book> books = r2dbcEntityTemplate.select(Query.query(Criteria.where("author").is(author)).
                with(pageRequest), Book.class);
        System.out.println(r2dbcEntityTemplate.getDatabaseClient().getConnectionFactory().toString());
        return ServerResponse.ok().body(books,Book.class);

    }


}

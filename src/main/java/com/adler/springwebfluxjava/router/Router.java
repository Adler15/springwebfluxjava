package com.adler.springwebfluxjava.router;

import com.adler.springwebfluxjava.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import javax.annotation.Resource;

@Configuration
public class Router {

    @Resource
    private BookHandler bookHandler;

    @Bean
    public RouterFunction<ServerResponse> bookRouter() {
        return RouterFunctions.nest(
                //相当于controller 下的 request mapping
                RequestPredicates.path("book"),
                //各个路由节点
                RouterFunctions.route(RequestPredicates.POST("addOneBook")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),bookHandler::addOneBook)
                .andRoute(RequestPredicates.PUT("updateOneBook")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), bookHandler::updateOneBook)
                .andRoute(RequestPredicates.GET("findById")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), bookHandler::findBookById)
                .andRoute(RequestPredicates.GET("book{bookId}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), bookHandler::getBookById)
                .andRoute(RequestPredicates.GET("allBooks"), bookHandler::getAll)
                .andRoute(RequestPredicates.GET("getPage")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), bookHandler::getPage)
                .andRoute(RequestPredicates.POST("getByAuthor")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)),bookHandler::getByAuthor)
        );
//        return RouterFunctions.route(RequestPredicates.POST("/addOneBook")
//                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), bookHandler::addOneBook)
//                .andRoute(RequestPredicates.PUT("/updateOneBook").
//                        and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), bookHandler::updateOneBook);
    }
}

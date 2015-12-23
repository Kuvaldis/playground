package controllers;

import ninja.Result;
import ninja.Results;
import service.GreetingService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HelloWorldController {

    @Inject
    private GreetingService greeter;

    public Result helloWorld() {
        return Results.html().render("greeting", greeter.hello());
    }
}

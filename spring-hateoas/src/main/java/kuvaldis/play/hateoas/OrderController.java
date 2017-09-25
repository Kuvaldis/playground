package kuvaldis.play.hateoas;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ExposesResourceFor(Order.class)
@RequestMapping("/orders")
public class OrderController {

    @GetMapping
    ResponseEntity orders() {
        return null;
    }

    @GetMapping("/{id}")
    ResponseEntity order(final @PathVariable("id") Long id) {
        return null;
    }
}

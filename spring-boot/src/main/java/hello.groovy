// just run spring run hello.groovy. It'll download everything you need
@RestController
class WebApplication {

    @RequestMapping("/")
    String home() {
        "Hello World!"
    }

}
package mjc.ramenlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @PostMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }
}

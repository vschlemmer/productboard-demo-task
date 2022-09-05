package cz.productboard.hire.schlemmer.languagepercentage.rest

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/test"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class TestController {

    @GetMapping()
    fun getTest() =
        "Hello world"
}

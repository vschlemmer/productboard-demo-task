package cz.productboard.hire.schlemmer.languagepercentage.api

import cz.productboard.hire.schlemmer.languagepercentage.domain.language.LanguagePercentageService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/language-percentage"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class LanguagePercentageController(
    private val languagePercentageService: LanguagePercentageService
) {

    @GetMapping()
    fun getAll(): LanguagePercentagesDto =
        languagePercentageService.getAll()

    @PostMapping("/reload-from-github")
    fun reloadLanguagesFromGithub() {
        languagePercentageService.reloadLanguagesFromGithub()
    }
}

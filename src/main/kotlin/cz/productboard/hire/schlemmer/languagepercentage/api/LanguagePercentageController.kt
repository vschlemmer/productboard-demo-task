package cz.productboard.hire.schlemmer.languagepercentage.api

import cz.productboard.hire.schlemmer.languagepercentage.domain.CreateLanguagePercentageDto
import cz.productboard.hire.schlemmer.languagepercentage.domain.LanguagePercentageService
import javax.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping()
    fun create(@RequestBody @NotNull createLanguagePercentageDto: CreateLanguagePercentageDto) =
        languagePercentageService.save(createLanguagePercentageDto)
}

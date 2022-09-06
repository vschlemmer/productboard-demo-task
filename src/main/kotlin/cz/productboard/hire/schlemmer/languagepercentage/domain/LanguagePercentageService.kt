package cz.productboard.hire.schlemmer.languagepercentage.domain

import cz.productboard.hire.schlemmer.languagepercentage.api.LanguagePercentagesDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LanguagePercentageService(
    private val languagePercentageRepository: LanguagePercentageRepository
) {

    fun save(createLanguagePercentageDto: CreateLanguagePercentageDto) =
        languagePercentageRepository.save(
            LanguagePercentageEntity(
                languageName = createLanguagePercentageDto.languageName,
                percentage = createLanguagePercentageDto.percentage
            )
        )

    fun getAll(): LanguagePercentagesDto =
        LanguagePercentagesDto(languagePercentageRepository.findAll())
}

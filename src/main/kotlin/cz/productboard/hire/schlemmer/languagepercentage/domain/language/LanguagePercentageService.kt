package cz.productboard.hire.schlemmer.languagepercentage.domain.language

import cz.productboard.hire.schlemmer.languagepercentage.COMPANY_NAME
import cz.productboard.hire.schlemmer.languagepercentage.PERCENTAGE_SCALE
import cz.productboard.hire.schlemmer.languagepercentage.api.LanguagePercentagesDto
import cz.productboard.hire.schlemmer.languagepercentage.domain.github.GithubFacade
import java.math.BigDecimal
import java.math.RoundingMode
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
class LanguagePercentageService(
    private val languagePercentageRepository: LanguagePercentageRepository,
    private val githubFacade: GithubFacade
) {

    @Transactional
    fun save(createLanguagePercentageDto: CreateLanguagePercentageDto) =
        languagePercentageRepository.findByLanguageName(createLanguagePercentageDto.languageName)?.also {
            it.percentage = createLanguagePercentageDto.percentage
            languagePercentageRepository.save(it)
        } ?: languagePercentageRepository.save(
            LanguagePercentageEntity(
                languageName = createLanguagePercentageDto.languageName,
                percentage = createLanguagePercentageDto.percentage
            )
        )

    fun getAll() =
        LanguagePercentagesDto(languagePercentageRepository.findAll())

    @Transactional
    fun reloadLanguagesFromGithub() {
        val languagesPercentages = calculateLanguagesPercentages(getLanguagesWithCounts())

        log.info { "Recalculated languages percentages, new values are: [$languagesPercentages]" }

        languagesPercentages.forEach {
            save(CreateLanguagePercentageDto(it.key, it.value))
        }
        removeUnusedLanguages(languagesPercentages.keys)
    }

    private fun getLanguagesWithCounts() =
        githubFacade.getAllRepositories(COMPANY_NAME)
            .map { githubFacade.getRepositoryLanguages(COMPANY_NAME, it.name) }
            .flatten()
            .groupingBy { it }
            .eachCount()

    private fun calculateLanguagesPercentages(languagesWithCounts: Map<String, Int>): Map<String, BigDecimal> {
        val totalCounts = BigDecimal(languagesWithCounts.values.sum())
        return languagesWithCounts.mapValues {
            BigDecimal(it.value).divide(totalCounts, PERCENTAGE_SCALE, RoundingMode.HALF_UP)
        }
    }

    private fun removeUnusedLanguages(usedLanguages: Set<String>) {
        getAll().languagePercentages?.forEach { languagePercentageEntity ->
            if (languagePercentageEntity.languageName !in usedLanguages) {
                languagePercentageRepository.findByLanguageName(languagePercentageEntity.languageName)?.let {
                    languagePercentageRepository.delete(it)
                }
            }
        }
    }
}

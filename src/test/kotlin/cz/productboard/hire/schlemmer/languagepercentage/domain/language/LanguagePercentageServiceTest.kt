package cz.productboard.hire.schlemmer.languagepercentage.domain.language

import cz.productboard.hire.schlemmer.languagepercentage.COMPANY_NAME
import cz.productboard.hire.schlemmer.languagepercentage.PERCENTAGE_SCALE
import cz.productboard.hire.schlemmer.languagepercentage.domain.github.GithubFacade
import cz.productboard.hire.schlemmer.languagepercentage.domain.github.RepositoryDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal
import java.math.MathContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val KOTLIN_LANG_NAME = "Kotlin"
private val KOTLIN_LANG_PERCENTAGE = BigDecimal(0.3)
private val KOTLIN_UPDATED_LANG_PERCENTAGE = BigDecimal(0.4)
private const val JAVASCRIPT_LANG_NAME = "JavaScript"
private val JAVASCRIPT_LANG_PERCENTAGE = BigDecimal(0.2)

private const val REPOSITORY_NAME_1 = "test1"
private const val REPOSITORY_NAME_2 = "test2"

class LanguagePercentageServiceTest : DescribeSpec() {

    init {
        val languagePercentageRepository: LanguagePercentageRepository = mockk()
        val githubFacade: GithubFacade = mockk()
        val languagePercentageService = LanguagePercentageService(languagePercentageRepository, githubFacade)

        describe("save") {
            it("should update language when existing or create new if not yet existing") {
                val entity = LanguagePercentageEntity(
                    languageName = KOTLIN_LANG_NAME,
                    percentage = KOTLIN_LANG_PERCENTAGE
                )
                val updatedEntity = entity
                updatedEntity.percentage = KOTLIN_UPDATED_LANG_PERCENTAGE

                every { languagePercentageRepository.findByLanguageName(KOTLIN_LANG_NAME) } returns entity
                every { languagePercentageRepository.save(updatedEntity) } returns updatedEntity

                val result = withContext(Dispatchers.IO) {
                    languagePercentageService.save(
                        CreateLanguagePercentageDto(
                            languageName = KOTLIN_LANG_NAME,
                            percentage = KOTLIN_UPDATED_LANG_PERCENTAGE
                        )
                    )
                }

                result.languageName shouldBe KOTLIN_LANG_NAME
                result.percentage shouldBe KOTLIN_UPDATED_LANG_PERCENTAGE

                verify { languagePercentageRepository.findByLanguageName(KOTLIN_LANG_NAME) }
                verify { languagePercentageRepository.save(updatedEntity) }
            }

            it("should create new if not yet existing") {
                every { languagePercentageRepository.findByLanguageName(JAVASCRIPT_LANG_NAME) } returns null
                every { languagePercentageRepository.save(any()) } returns LanguagePercentageEntity(
                    languageName = JAVASCRIPT_LANG_NAME,
                    percentage = JAVASCRIPT_LANG_PERCENTAGE
                )

                val result = withContext(Dispatchers.IO) {
                    languagePercentageService.save(
                        CreateLanguagePercentageDto(
                            languageName = JAVASCRIPT_LANG_NAME,
                            percentage = JAVASCRIPT_LANG_PERCENTAGE
                        )
                    )
                }

                result.languageName shouldBe JAVASCRIPT_LANG_NAME
                result.percentage shouldBe JAVASCRIPT_LANG_PERCENTAGE

                verify { languagePercentageRepository.findByLanguageName(JAVASCRIPT_LANG_NAME) }
                verify { languagePercentageRepository.save(any()) }
            }
        }

        describe("reloadLanguagesFromGithub") {
            it("should calculate and save percentages") {
                val kotlinEntity = LanguagePercentageEntity(
                    languageName = KOTLIN_LANG_NAME,
                    percentage = BigDecimal(0.5)
                )
                val javascriptEntity = LanguagePercentageEntity(
                    languageName = JAVASCRIPT_LANG_NAME,
                    percentage = BigDecimal(0.5)
                )
                every { githubFacade.getAllRepositories(COMPANY_NAME) } returns listOf(RepositoryDto(REPOSITORY_NAME_1), RepositoryDto(REPOSITORY_NAME_2))
                every { githubFacade.getRepositoryLanguages(COMPANY_NAME, REPOSITORY_NAME_1) } returns listOf(KOTLIN_LANG_NAME, JAVASCRIPT_LANG_NAME)
                every { githubFacade.getRepositoryLanguages(COMPANY_NAME, REPOSITORY_NAME_2) } returns listOf(KOTLIN_LANG_NAME, JAVASCRIPT_LANG_NAME)
                every { languagePercentageRepository.findByLanguageName(KOTLIN_LANG_NAME) } returns null
                every { languagePercentageRepository.save(any()) } returns kotlinEntity
                every { languagePercentageRepository.findByLanguageName(JAVASCRIPT_LANG_NAME) } returns null
                every { languagePercentageRepository.save(any()) } returns javascriptEntity
                every { languagePercentageRepository.findAll() } returns listOf(kotlinEntity, javascriptEntity)

                withContext(Dispatchers.IO) {
                    languagePercentageService.reloadLanguagesFromGithub()
                }

                verify { githubFacade.getAllRepositories(COMPANY_NAME) }
                verify { githubFacade.getRepositoryLanguages(COMPANY_NAME, REPOSITORY_NAME_1) }
                verify { githubFacade.getRepositoryLanguages(COMPANY_NAME, REPOSITORY_NAME_2) }
                verify { languagePercentageRepository.findByLanguageName(KOTLIN_LANG_NAME) }
                verify { languagePercentageRepository.save(any()) }
                verify { languagePercentageRepository.findByLanguageName(JAVASCRIPT_LANG_NAME) }
                verify { languagePercentageRepository.save(any()) }
                verify { languagePercentageRepository.findAll() }
            }
        }

        describe("calculateLanguagesPercentages") {
            it("should calculate percentages") {
                val languagesDataToExpectedPercentagesMapping = mapOf(
                    mapOf(KOTLIN_LANG_NAME to 1, JAVASCRIPT_LANG_NAME to 2)
                        to mapOf(KOTLIN_LANG_NAME to getDecimal(0.3), JAVASCRIPT_LANG_NAME to getDecimal(0.7)),
                    mapOf(KOTLIN_LANG_NAME to 1, JAVASCRIPT_LANG_NAME to 4)
                        to mapOf(KOTLIN_LANG_NAME to getDecimal(0.2), JAVASCRIPT_LANG_NAME to getDecimal(0.8)),
                    mapOf(KOTLIN_LANG_NAME to 1, JAVASCRIPT_LANG_NAME to 1)
                        to mapOf(KOTLIN_LANG_NAME to getDecimal(0.5), JAVASCRIPT_LANG_NAME to getDecimal(0.5)),
                )

                languagesDataToExpectedPercentagesMapping.forEach {
                    val result = withContext(Dispatchers.IO) {
                        languagePercentageService.calculateLanguagesPercentages(it.key)
                    }
                    result shouldBe it.value
                }
            }
        }
    }

    private fun getDecimal(value: Double) =
        BigDecimal(value, MathContext(PERCENTAGE_SCALE))
}

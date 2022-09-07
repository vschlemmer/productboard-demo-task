package cz.productboard.hire.schlemmer.languagepercentage.scheduler

import cz.productboard.hire.schlemmer.languagepercentage.domain.language.LanguagePercentageService
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class Scheduler(
    private val languagePercentageService: LanguagePercentageService
) {

    @Scheduled(cron = "\${scheduler}")
    fun reloadLanguages() {
        log.info { "Starting scheduled reloading of languages percentages from Github." }
        languagePercentageService.reloadLanguagesFromGithub()
        log.info { "Finished scheduled reloading of languages percentages from Github." }
    }
}

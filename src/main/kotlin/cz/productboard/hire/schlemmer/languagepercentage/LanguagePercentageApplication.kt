package cz.productboard.hire.schlemmer.languagepercentage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class LanguagePercentageApplication

fun main(args: Array<String>) {
    runApplication<LanguagePercentageApplication>(*args)
}

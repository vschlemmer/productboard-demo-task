package cz.productboard.hire.schlemmer.languagepercentage.domain.github

import com.fasterxml.jackson.databind.ObjectMapper

fun deserializeLanguages(languagesJson: String?): List<String> =
    ObjectMapper().readTree(languagesJson)
        .fieldNames()
        .asSequence()
        .toList()

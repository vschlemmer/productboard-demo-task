package cz.productboard.hire.schlemmer.languagepercentage.api

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import cz.productboard.hire.schlemmer.languagepercentage.domain.LanguagePercentageEntity

@JsonSerialize(using = LanguagePercentageSerializer::class)
data class LanguagePercentagesDto(
    val languagePercentages: List<LanguagePercentageEntity>?
)

package cz.productboard.hire.schlemmer.languagepercentage.domain

import java.math.BigDecimal

data class CreateLanguagePercentageDto(
    val languageName: String,
    val percentage: BigDecimal,
)

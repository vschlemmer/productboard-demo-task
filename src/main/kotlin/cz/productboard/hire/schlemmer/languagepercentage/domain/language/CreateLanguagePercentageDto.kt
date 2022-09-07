package cz.productboard.hire.schlemmer.languagepercentage.domain.language

import java.math.BigDecimal

data class CreateLanguagePercentageDto(
    val languageName: String,
    val percentage: BigDecimal,
)

package cz.productboard.hire.schlemmer.languagepercentage.domain.language

import org.springframework.data.jpa.repository.JpaRepository

interface LanguagePercentageRepository : JpaRepository<LanguagePercentageEntity, Long> {

    fun findByLanguageName(name: String): LanguagePercentageEntity?
}

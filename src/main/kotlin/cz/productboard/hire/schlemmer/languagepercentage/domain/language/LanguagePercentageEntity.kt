package cz.productboard.hire.schlemmer.languagepercentage.domain.language

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "language_percentage")
class LanguagePercentageEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(name = "language_name", nullable = false)
    var languageName: String,

    @Column(name = "percentage", nullable = false)
    var percentage: BigDecimal
)

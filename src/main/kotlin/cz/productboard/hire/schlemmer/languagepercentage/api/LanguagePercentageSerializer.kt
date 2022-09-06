package cz.productboard.hire.schlemmer.languagepercentage.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

/**
 * This serializer is needed because the list of language percentages has to be serialized into a single Json object,
 * where each list item is represented by a single field in the resulting Json object.
 */
class LanguagePercentageSerializer(clazz: Class<LanguagePercentagesDto>?) : StdSerializer<LanguagePercentagesDto>(clazz) {

    override fun serialize(values: LanguagePercentagesDto?, generator: JsonGenerator, provider: SerializerProvider) {
        values?.let { languagePercentagesDto ->
            languagePercentagesDto.languagePercentages?.let { languagePercentages ->
                generator.writeStartObject()
                languagePercentages.forEach {
                    generator.writeObjectField(it.languageName, it.percentage)
                }
                generator.writeEndObject()
            }
        }
    }
}

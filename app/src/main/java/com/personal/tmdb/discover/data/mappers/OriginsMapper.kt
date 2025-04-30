package com.personal.tmdb.discover.data.mappers

import com.personal.tmdb.discover.data.models.CountryDto
import com.personal.tmdb.discover.data.models.LanguageDto
import com.personal.tmdb.discover.domain.models.Country
import com.personal.tmdb.discover.domain.models.Language
import java.util.Locale

fun List<CountryDto>.toCountry(): List<Country> {
    return this
        .map { countryDto ->
            val locale = Locale("", countryDto.iso31661)
            Country(
                code = countryDto.iso31661,
                englishName = countryDto.englishName,
                locale = locale
            )
        }
}

fun List<LanguageDto>.toLanguage(): List<Language> {
    return this
        .map { languageDto ->
            val locale = Locale(languageDto.iso6391)
            Language(
                code = languageDto.iso6391,
                englishName = languageDto.englishName,
                locale = locale
            )
        }
}
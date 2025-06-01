package com.personal.tmdb.discover.data.mappers

import com.personal.tmdb.discover.data.models.CountryDto
import com.personal.tmdb.discover.domain.models.Country
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
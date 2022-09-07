package cz.productboard.hire.schlemmer.languagepercentage.domain.github

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

const val REPOSITORIES_URL = "https://api.github.com/orgs/%s/repos"
const val REPOSITORY_LANGUAGES_URL = "https://api.github.com/repos/%s/%s/languages"

@Service
class GithubFacade(
    private val restTemplate: RestTemplate
) {

    fun getAllRepositories(companyName: String) =
        restTemplate.exchange(
            String.format(REPOSITORIES_URL, companyName),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<RepositoryDto>>() {}
        ).body!!

    fun getRepositoryLanguages(companyName: String, repositoryName: String) =
        deserializeLanguages(
            restTemplate.exchange(
                String.format(REPOSITORY_LANGUAGES_URL, companyName, repositoryName),
                HttpMethod.GET,
                null,
                String::class.java
            ).body
        )
}

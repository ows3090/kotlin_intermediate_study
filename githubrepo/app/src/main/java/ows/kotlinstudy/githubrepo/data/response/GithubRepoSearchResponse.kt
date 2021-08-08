package ows.kotlinstudy.githubrepo.data.response

import ows.kotlinstudy.githubrepo.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
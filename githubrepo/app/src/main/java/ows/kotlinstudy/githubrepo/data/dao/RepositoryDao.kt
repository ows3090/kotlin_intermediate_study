package ows.kotlinstudy.githubrepo.data.dao

import androidx.room.*
import ows.kotlinstudy.githubrepo.data.entity.GithubRepoEntity

@Dao
interface RepositoryDao {

    @Insert
    suspend fun insert(repo: GithubRepoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repoList: List<GithubRepoEntity>)

    @Query("SELECT * FROM Githubrepository")
    suspend fun getHistory(): List<GithubRepoEntity>

    @Query("SELECT * FROM GithubRepository WHERE fullName = :fullName")
    suspend fun getRepository(fullName: String): GithubRepoEntity

    @Query("DELETE FROM Githubrepository WHERE fullName = :fullName")
    suspend fun remove(fullName: String)

    @Query("DELETE FROM Githubrepository")
    suspend fun clearAll()
}
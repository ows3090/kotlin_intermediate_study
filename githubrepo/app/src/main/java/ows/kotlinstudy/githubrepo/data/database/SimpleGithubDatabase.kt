package ows.kotlinstudy.githubrepo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ows.kotlinstudy.githubrepo.data.dao.RepositoryDao
import ows.kotlinstudy.githubrepo.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1 )
abstract class SimpleGithubDatabase: RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}
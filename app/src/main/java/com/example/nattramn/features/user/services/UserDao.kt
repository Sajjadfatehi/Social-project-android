package com.example.nattramn.features.user.services

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.nattramn.features.user.data.UserAndArticle
import com.example.nattramn.features.user.data.UserAndArticleCount
import com.example.nattramn.features.user.data.UserEntity

@Dao
interface UserDao {

    @Query("delete from users")
    fun clearUserTable()

    @Query("select * from users where username =:username")
    fun getUser(username: String): UserEntity

    @Query("select * from users")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity?)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: List<UserEntity>)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Update
    fun updateUser(userEntity: UserEntity)

    @Query("select *, count(article.title) as count from users user join articles article on user.username = article.ownerUsername group by user.username")
    fun getUsersWithArticleCount(): LiveData<List<UserAndArticleCount>>

    @Transaction
    @Query("select * from users")
    fun getUsersAndArticles(): LiveData<List<UserAndArticle>>

    /*@Transaction
    @Query("select * from users")
    fun getUserWithArticlesAndCommentsAndTags(): LiveData<List<UserWithArticleAndCommentsAndTags>>*/

}
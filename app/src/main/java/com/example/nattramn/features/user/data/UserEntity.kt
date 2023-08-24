package com.example.nattramn.features.user.data

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.nattramn.features.article.data.entities.ArticleEntity
import com.example.nattramn.features.user.ui.UserView
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "users",
    primaryKeys = ["username"]
)
@Parcelize
data class UserEntity(
    var username: String,
    var following: Boolean? = false,
    var image: String
) : Parcelable {
    fun toUserView(): UserView {
        return UserView(
            name = username,
            job = "مدرس زبان انگلیسی",
            image = image,
            followers = "85",
            following = following
        )
    }
}

data class UserAndArticle(
    @Embedded val userEntity: UserEntity,
    @Relation(
        parentColumn = "username",
        entityColumn = "ownerUsername"
    )
    val articleEntity: List<ArticleEntity>
)

data class UserAndArticleCount(
    val count: Int,
    @Embedded val user: UserEntity
)
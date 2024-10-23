package kr.linkerbell.campusmarket.android.data.remote.local.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Entity(tableName = "user")
data class UserProfileEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "profileImage") val profileImage: String,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
) : DataMapper<UserProfile> {
    override fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            nickname = nickname,
            profileImage = profileImage,
            rating = rating,
            createdAt = createdAt
        )
    }
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        nickname = nickname,
        profileImage = profileImage,
        rating = rating,
        createdAt = createdAt
    )
}

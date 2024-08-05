package kr.linkerbell.boardlink.android.data.remote.mapper

interface DataMapper<D> {
    fun toDomain(): D
}

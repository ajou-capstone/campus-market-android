package kr.linkerbell.campusmarket.android.data.remote.mapper

interface DataMapper<D> {
    fun toDomain(): D
}

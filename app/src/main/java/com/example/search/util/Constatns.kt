package com.example.search.util

object Constants {
    // Kakao Image Search API의 기본 URL
    const val BASE_URL = "https://dapi.kakao.com/"

    // Kakao API를 사용하기 위한 인증 헤더
    const val AUTH_HEADER = "KakaoAK ffee0de3b0664df4ef6c5ed489a3f384"

    // 앱의 Shared Preferences 파일 이름
    const val PREFS_NAME = "com.example.search.pref"

    // 마지막 검색어를 저장하기 위한 키 값
    const val LAST_QUERY = "LAST_QUERY"

    // 이미지 검색 타입 코드
    const val SEARCH_TYPE_IMAGE = 0

    // 비디오 검색 타입 코드
    const val SEARCH_TYPE_VIDEO = 1
}
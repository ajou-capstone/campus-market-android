# CampusMarket

[![android-ci](https://github.com/ajou-capstone/campus-market-android/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/ajou-capstone/campus-market-android/actions/workflows/android-ci.yml)

CampusMarket 안드로이드 레포지토리입니다.

## Member

| 이름  | 기여 분야                 |           연락처           |
|:---:|-----------------------|:-----------------------:|
| 장성혁 | 전체 구조, 회원가입 및 로그인, 채팅 | edicom000111@ajou.ac.kr |
| 정은재 | 게시글, 마이페이지            |   siria22@ajou.ac.kr    |

## Build

- Gradle 8.9
- AGP 8.5.1
- Kotlin 2.0.0
- JDK 17
- Kotlin DSL
- Version Catalog

## Library

- KotlinX
  - Coroutines
  - Serialization
  - DateTime
- AndroidX
  - Room
  - Paging
  - Compose + NavigationUI
- Dagger Hilt
- Ktor
- Stomp
- Coil
- Debug & Log
  - OkHttp3 Logging Interceptor (App Inspection)
  - Leak Canary 2
  - Timber
  - Firebase

## 주요 디렉토리 구조
```bash
app

common

data
├─remote
│ ├─local
│ └─remote
│   └─api
└─repository

domain
├─model
├─repository
└─usecase

presentation
├─common
├─model
└─ui
  ├─splash
  ├─nonlogin
  └─home
    ├─trade
    ├─chatroom
    ├─schedule
    └─mypage
``` 
- `app`은 전반적인 애플리케이션을 담당합니다.
- `common`은 애플리케이션 전반적으로 사용되는 순수 Kotlin 유틸리티성 파일들을 모아놓은 곳입니다.
- `data`는 데이터 관련 로직을 담당합니다.
  - `data/remote`는 remote 와 통신하는 로직을 담당합니다.
  - `data/repository`는 데이터 관련 로직을 담당합니다.
- `domain`은 비즈니스 로직을 담당합니다.
- `presentation`은 UI 로직을 담당합니다.
  - `presentation/ui`는 화면을 담당합니다.
    - `presentation/ui/nonlogin`는 로그인 화면을 담당합니다.
    - `presentation/ui/home`는 메인 화면을 담당합니다.
      - `presentation/ui/home/trade`는 거래 화면을 담당합니다.
      - `presentation/ui/home/chatroom`는 채팅 화면을 담당합니다.
      - `presentation/ui/home/schedule`는 시간표 화면을 담당합니다.
      - `presentation/ui/home/mypage`는 마이페이지 화면을 담당합니다.

<br/>

## Code / Style Convention
- Android Studio Kotlin Style Guide 를 따릅니다. (https://developer.android.com/kotlin/style-guide)
- 기술되어있지 않은 경우 Kotlin Style Guide 를 따릅니다. (https://kotlinlang.org/docs/coding-conventions.html)

## Git Convention
Github Flow 를 사용합니다.

![https://miro.medium.com/v2/resize:fit:1400/format:webp/1*bvubr15Z_l3UknX1w8Mx1A.png](https://miro.medium.com/v2/resize:fit:1400/format:webp/1*bvubr15Z_l3UknX1w8Mx1A.png)

<h1 align="center">Hi-Jack-Mocker</h1>
:balloon: Hi-Jack-Mocker is a project that leverages OkHttp3's interceptor to intercept and modify network requests and responses, allowing you to verify the UI easily.

|Requests|Response|
|:------:|:-----:|
| <img width="300" src="./screenshot/request.gif"> | <img width="300" src="./screenshot/response.gif"> |

## Setup
To integrate Hi-Jack-Mocker into your Android project, follow these steps:

### Initialize Hi-Jack-Mocker in your application:

```kotlin
override fun onCreate() {
    super.onCreate()
    HiJackMocker.initialize(this)
}
```
Add the above code in the onCreate method of your Application class.

### Configure OkHttpClient:

When creating your OkHttpClient instance, include the addHiJackMocker() method:

```kotlin
@Provides
@Singleton
fun provideHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .readTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .connectTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TEN_SECONDS, TimeUnit.SECONDS)
        .addHiJackMocker()
        .addInterceptor(getLoggingInterceptor())
        .build()
```
## Usage
To enable or disable the Hi-Jack-Mocker mode, use the ImageButton provided at the top of every activity. 

This allows you to control whether the hjm mode is active.
|Mode Off|Mode On|
|:------:|:-----:|
| <img width="200" src="./screenshot/hjm_mode_off.png"> | <img width="200" src="./screenshot/hjm_mode_on.png"> |

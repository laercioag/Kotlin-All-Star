# Kotlin All Star
Simple android app that lists Kotlin repos on GitHub by stars.

# Paging
The data is paged to the UI using Room to create the DataSource.Factory and the Paging Boundary Callback to get notified when the Paging library consumes the available local data and request new data from the api.

Repository states are propagated to the UI using LiveData.

# Request Rate Limit
Due to GitHub`s api access limit a Interceptor was created to lock and wait for when the api can accept new requests in case the rate limit is exceeded.

# About
This project was build with:
- Android Architecture Components
- Kotlin
- Repository Pattern
- RxJava 2
- Dagger 2
- Ktlint
- jUnit
- Mockito


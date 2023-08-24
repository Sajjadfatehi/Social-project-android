Nattramn Android App
======================

Nattramn is the Android app for sharing educational articles.

# Features

The app displays a list of articles. In the main page(Home page) the user can
see articles of users who are currently in their *following* list and on the bottom
of the page the app displays latest published articles. Nattramn allows users to publish 
their articles online. A user can share, comment on, like and search articles.

<img align="center" src="app/src/main/java/com/example/nattramn/core/screenshots/home_tab_one.jpg" alt="Article Fragment" height="640" width="320">
<img align="center" src="app/src/main/java/com/example/nattramn/core/screenshots/article.jpg" alt="Article Fragment" height="640" width="320">
<img align="center" src="app/src/main/java/com/example/nattramn/core/screenshots/profile.jpg" alt="Article Fragment" height="640" width="320">
<img align="center" src="app/src/main/java/com/example/nattramn/core/screenshots/register.jpg" alt="Article Fragment" height="640" width="320">

# Development Environment

The app is written entirely in Kotlin and uses the Gradle build system.

# Architecture

The architecture is built around
[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/).

I kept logic away from
Activities and Fragments and implemented it to
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)s.
I observed data using
[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
and used the [Data Binding Library](https://developer.android.com/topic/libraries/data-binding/)
to bind UI components in layouts to the app's data sources.

I used a Repository layer for handling data operations.

I used [Navigation component](https://developer.android.com/guide/navigation)
to simplify into a single Activity app.

I used [Room](https://developer.android.com/jetpack/androidx/releases/room)
for caching data in order to use when offline.


# Movie Catalog

## Brief

Movie Catalog is a native Android application designed to allow users who have a keen interest in Film and TV, to view an archive on the latest and greatest in digital media. Users can search for a movie or TV program and add it to their "watchlist" as a record of the things they want to watch later.

All data is pulled from the The Movie Database api: https://developers.themoviedb.org/3/getting-started/introduction

## Setup Instructions

Obtain the project by doing **one** of the following:
1. Open Android Studio, and click File -> New -> Project from Version Control -> GitHub. Paste in the repository URL and click clone. Alternatively, you can press "Get from Version Control" on the Android Studio splash screen.
2. Click the bright green button that says "Code" to download the ZIP file and unzip it onto your desktop. Open Android Studio and import the project by navigting to the project location.
3. Manually clone the repository to your machine by using the repository URL. Open Android Studio and import the project by navigting to the project location.

Note: the repository URL can be found by clicking the green button labeled "Code".

Once you have the project on your machine and Android Studio is open:
1. Click Build -> Clean Project
2. Click Build -> Make Project
3. Decide if you want to run the app on a virtual device, or on a physical device. The instructions for each are listed [here](https://developer.android.com/training/basics/firstapp/running-app).
4. Run the app

## Libraries and Frameworks

* **Retrofit** for managing web requests.
* **Leak Canary** for monitoring and fixing memory leaks.
* **Espresso** for UI testing.
* **JUnit** for unit testing.
* **Glide** for image loading.
* **Paging 3** for paging movie and tv data.
* **Room** for storing and retrieving local data.
* **Gson** for converting Java Objects into their JSON representation.
* **Coroutines** for making asynchronous requests.
* **Navigation Component** for navigation between fragments.

See the source code for more documentation on my implementation.

Screenshots

![Movies](/screenshots/movies.png?raw=true)
![Watchlist](/screenshots/watchlist.png?raw=true)

Demo

<img src="/screenshots/demo.gif" width="200" height="200" />




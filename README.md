# Login and Signup App with News API
#### This Android app allows users to sign up and login using their email and password, or via Google or Facebook authentication. Upon successful login, users are taken to a home screen that displays news articles from https://newsapi.org/.
## App Preview
https://user-images.githubusercontent.com/68557395/234525207-8e70c184-bab4-46f3-99d0-4fdc8f138e15.mp4
## Functionality
### Sign Up and Login
Users can sign up with a valid email and password. Validation is done on the client side to ensure that the email and password meet the required criteria. If the sign up is successful, the user's information is stored in Firestore.<br><br>
Users can then log in using the same email and password. Validation is also done on the client side to ensure that the email and password are entered correctly. If the login is successful, the user is taken to the Home page.<br>
### Third Party Login
Users can also log in using their Google or Facebook accounts. This is done using Firebase Authentication. Users can click on the corresponding icon on the login screen to initiate the login process. If the login is successful, the user is taken to the Home page.

### Home Page
The Home page displays a list of news articles obtained from the News API. The articles are displayed in cards with rounded corners and shadows. Each card displays the article's title, description, published date, source, and image. Users can click on a card to view the article in a web view.

Users can search for articles by entering a search query in the Search bar.

## Libraries Used
##### This app uses the following libraries:
Navigation: Used to navigate between different fragments and implement the back stack.<br>
implementation "androidx.navigation:navigation-fragment-ktx:2.5.3"<br>
implementation "androidx.navigation:navigation-ui-ktx:3.5.3"<br><br>
ViewModel and LiveData: Used to implement the MVVM architecture pattern and observe changes in the data. <br>
implementation "androidx.lifecycle:lifecycle-livedata:2.6.0"<br>
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0"<br><br>
Dagger 2: Used for dependency injection to facilitate separation of concerns and improve maintainability. <br>
implementation 'com.google.dagger:dagger:2.44'<br>
kapt 'com.google.dagger:dagger-compiler:2.44'<br><br>
Country Code Picker: Used to provide a UI for selecting a country code. <br>
implementation 'io.michaelrocks:libphonenumber-android:8.12.44'<br>
implementation 'com.github.joielechong:countrycodepicker:2.4.2'<br>
Retrofit: Used to make API calls and parse the JSON responses.<br>
implementation 'com.squareup.retrofit2:retrofit:2.9.0'<br>
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'<br><br>
Glide: Used to load and display images from the internet.<br>
implementation 'com.github.bumptech.glide:glide:4.13.0'<br>
implementation 'com.github.bumptech.glide:compiler:4.12.0'<br><br>
Firebase: Used for authentication and Remote database storage.

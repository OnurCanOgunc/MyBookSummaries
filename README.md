# BookNest

BookNest is a mobile application designed to help users organize their books, save summaries, track reading progress, and set monthly reading goals. The app works offline, ensuring seamless access to your data anytime. Additionally, it provides insightful reading statistics, including the total number of books read.

## Features:  
*   **Interactive Onboarding:** Guides users through the app’s core features for a smooth start.
*   **Add Books:** Add books with title, author, page count, genre, and cover image.
*   **Add Summaries:** Save book summaries with timestamps.
*   **Save Quotes:** Store important excerpts and highlights from books.
*   **Categories & Tags:** Organize books into categories and add tags.
*   **Search & Filter:** Find books by title, author, or genre.
*   **Reading Progress:** Track reading status ("Queued", "Reading", "Finished").
*   **Reading Statistics:** Set monthly reading goals and track progress.
*   **Offline Support:** Access and add books without an internet connection.
*   **Total Books Read:** Displays the total number of books read.
*   **Profile Management:** Update user information.
*   **Light & Dark Mode Support** 

## Tech Stack
*   **Kotlin:** Modern programming language for Android development.
*   **Jetpack Compose:** Modern UI framework.
*   **Material Design:** Provides a user-friendly and aesthetic design.
*   **Hilt:** Dependency Injection for better modularity.
*   **Room:** Local database storage with SQLite.
*   **Navigation:** Manages screen transitions.
*   **WorkManager:** Background data synchronization.
*   **Firebase:** Authentication and cloud database.
*   **Coroutines - Flow:** Asynchronous programming and data streams.
*   **SplashScreen API:** Displays a splash screen on startup.
*   **Coil:** Efficient image loading.
*   **MVVM:** Separates concerns between UI, business logic, and data layers.
*   **Clean Architecture:** Ensures modular, scalable, and maintainable code structure.

## Screens:  
*   **Onboarding:** Explains the basic features of the application on first launch.
*   **Welcome:** Welcome screen.
*   **SignIn/SignUp:** User login and registration.
*   **Home:** Main screen with book list.
*   **AddBook:** Screen for adding new books.
*   **Detail:** Book details, summaries, and quotes.
*   **Profile:** User profile and reading statistics.
*   **EditProfile:** Edit your user information and set your monthly book target

## Screenshots
| Ekran Adı       | Dark Mode                                                                                               | Light Mode                                                                                              |
|-----------------|---------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| **Onboarding** | <img src="https://github.com/user-attachments/assets/bf14474f-af88-4284-a28c-9f0d6eb57e2c" width="250">  | <img src="https://github.com/user-attachments/assets/0cd9e08e-4ec8-49d4-854a-45b26ae3ccca" width="250"> |
| **Welcome** | <img src="https://github.com/user-attachments/assets/869a598c-1875-4318-b547-ed2e6c746cc1" width="250">     | <img src="https://github.com/user-attachments/assets/c29c43e4-5f08-4c36-bf38-bb4c97004cd9" width="250">    |
| **SignIn/SignUp**| <img src="https://github.com/user-attachments/assets/bf69dba4-4914-4ed8-84ee-cbf968cb7dd8" width="250"><br><img src="https://github.com/user-attachments/assets/c4b76a9c-447f-4787-96ea-ced2f87d1f46" width="250"> | <img src="https://github.com/user-attachments/assets/ac38c66b-ac57-4360-b8d6-f94c4c41191c" width="250"><br><img src="https://github.com/user-attachments/assets/714be065-f657-469a-b865-24d769ab34e1" width="250"><br><img src="https://github.com/user-attachments/assets/6a7a45e6-0307-4d9a-bda3-bca4a38e1bd4" width="250">  |
| **Home** | <img src="https://github.com/user-attachments/assets/b1bb25ec-5ca4-47bb-b955-a96d518957c8" width="250">      | <img src="https://github.com/user-attachments/assets/4f49d70a-9500-4c77-9c6e-8c5c0d8bae76" width="250"><br><img src="https://github.com/user-attachments/assets/d1153007-2639-41e8-8c41-c6c629f4939d" width="250"><br><img src="https://github.com/user-attachments/assets/3b148ac8-8a5e-4f9a-a1fe-6a55acce8744" width="250"><br><img src="https://github.com/user-attachments/assets/0b185bfe-d6f6-4b2c-9a37-8b6dffe55c74" width="250"><br><img src="https://github.com/user-attachments/assets/40f7482a-3ee6-48b6-a6e4-e94e940a23e8" width="250"> |
| **AddBook** | <img src="https://github.com/user-attachments/assets/7829ae01-4c37-4847-8874-e182efe110ab" width="250">    | <img src="https://github.com/user-attachments/assets/d7b3dd90-931d-4771-a64a-cb5ca0b864ec" width="250"><br><img src="https://github.com/user-attachments/assets/d08de87a-66bb-40ce-a56d-e879f554bad3" width="250"> |
| **Detail** | <img src="https://github.com/user-attachments/assets/6aa75222-df84-489f-a36b-1fcfd31261dd" width="250"><br><img src="https://github.com/user-attachments/assets/784793e5-f653-45d8-b2df-61018f68c77a" width="250"><br><img src="https://github.com/user-attachments/assets/8439a4e0-3c3f-4a18-bddd-e3f7422821ab" width="250"><br><img src="https://github.com/user-attachments/assets/3d2638f5-6fcc-4cac-9088-24b2046f83f4" width="250"> | <img src="https://github.com/user-attachments/assets/b37bc277-e219-46e1-8878-e4b298416e8e" width="250"> |
| **Profile** | <img src="https://github.com/user-attachments/assets/617bea19-5cec-4350-bb2f-6f5934378bd6" width="250"><br><img src="https://github.com/user-attachments/assets/fce76641-3d02-4e32-957d-89651bb2bbdd" width="250"> | <img src="https://github.com/user-attachments/assets/f9abd385-7253-4ad0-b208-289bba545d57" width="250"><br><img src="https://github.com/user-attachments/assets/6c89f218-3f7c-4454-af5a-48655f358007" width="250"> |
| **EditProfile** | <img src="https://github.com/user-attachments/assets/7ead73de-15f0-4a9b-9393-b1bf55238aab" width="250"> | <img src="https://github.com/user-attachments/assets/6c2ca174-5c4c-4c5e-ba38-8152944d94c3" width="250"> |

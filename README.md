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
| **Splash** | <img src="https://github.com/user-attachments/assets/29db30e0-d3f4-4ab2-a176-0e888572f5dd" width="250">  | <img src="https://github.com/user-attachments/assets/23142b84-0b13-4453-b344-746d20dfe502" width="250"> |
| **Onboarding** | <img src="https://github.com/user-attachments/assets/98aa0958-b356-4e94-b07e-259d7c6d7438" width="250">  | <img src="https://github.com/user-attachments/assets/0eeab76a-bff4-4b71-85c4-5853c86b60d8" width="250"> |
| **Welcome** | <img src="https://github.com/user-attachments/assets/d668b9b2-8389-4390-ac60-0a043cb0b65f" width="250">     | <img src="https://github.com/user-attachments/assets/fae0b4c6-5819-45e1-86d7-2a941978c0dc" width="250">    |
| **SignIn/SignUp**| <img src="https://github.com/user-attachments/assets/b0ee041d-f98b-4822-9381-8d47e4fbee48" width="250"><br><img src="https://github.com/user-attachments/assets/d0adb29f-ff56-4dd6-8e1d-bbc0d909de17" width="250"> | <img src="https://github.com/user-attachments/assets/8a37574a-94b6-4f7c-8164-30ac6756530f" width="250"><br><img src="https://github.com/user-attachments/assets/7b9fee2a-b70c-41e0-a8ce-721975beef65" width="250">  |
| **Forgot Password** | <img src="https://github.com/user-attachments/assets/29941160-7ad9-411c-ad9b-c694f1b7db8c" width="250">  | <img src="https://github.com/user-attachments/assets/b2685774-7720-43e8-a866-eb7d4f9d4cd3" width="250"> |
| **Home** | <img src="https://github.com/user-attachments/assets/b8ffe272-9121-4a88-a612-715f4a8e424a" width="250"><br><img src="https://github.com/user-attachments/assets/c57c1eac-7334-4753-84cc-0288123008e5" width="250"><br><img src="https://github.com/user-attachments/assets/f1a13157-b1d9-4a36-b873-08e57bee1315" width="250"> | <img src="https://github.com/user-attachments/assets/e2eab372-bfbd-4583-ba00-1d7b274a899b" width="250"><br><img src="https://github.com/user-attachments/assets/44baee86-88ab-4ed8-a215-721a2ac5b82d" width="250"><br><img src="https://github.com/user-attachments/assets/8a69babb-8b91-4e49-aa79-26502abbb1dc" width="250"> |
| **AddBook** | <img src="https://github.com/user-attachments/assets/5b0f8464-9aba-42dd-a286-06cb8908ce0f" width="250">    | <img src="https://github.com/user-attachments/assets/78a6c70e-97a1-4ba4-ad5f-b36cce890475" width="250"> |
| **EditBook** | <img src="https://github.com/user-attachments/assets/7bd4d112-f6a1-4eba-b155-7b451f0f7dc1" width="250"> | <img src="https://github.com/user-attachments/assets/cd1e89d4-c81e-43d9-ad29-d21bf5b711ec" width="250"> |
| **Detail** | <img src="https://github.com/user-attachments/assets/9cb2420a-4880-4c6e-9f5c-da73cb71f73a" width="250"><br><img src="https://github.com/user-attachments/assets/8c76126d-abd2-48c6-bcb4-dada247de1dc" width="250"> | <img src="https://github.com/user-attachments/assets/40a74b2f-1778-42a6-8eec-c36db51e8437" width="250"><br><img src="https://github.com/user-attachments/assets/1f994a41-0a07-44d4-bb70-1883ae342ddb" width="250"> |
| **Profile** | <img src="https://github.com/user-attachments/assets/c78bb4a5-9eb8-4c1c-93cb-4f48e904b6ad" width="250"><br><img src="https://github.com/user-attachments/assets/da795730-b0ac-41ec-80be-3d9a7de25d6f" width="250"> | <img src="https://github.com/user-attachments/assets/449e05bf-6b14-4a00-8784-0ef3faffe55f" width="250"><br><img src="https://github.com/user-attachments/assets/638f6e31-37d3-4de4-9090-36e286bef156" width="250"> |
| **EditProfile** | <img src="https://github.com/user-attachments/assets/c8cce7a3-f124-454e-9b00-66fcce35abec" width="250"> | <img src="https://github.com/user-attachments/assets/8fd7fdcb-50e9-4851-9c4d-2dc934b731a3" width="250"> |


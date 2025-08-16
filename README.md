# VibraGram

VibraGram is a full-stack project with a **Spring Boot backend** and a **React Native frontend**.
This repository contains both applications, organized into separate directories.

---

## 📂 Project Structure

```
VibraGram/
│── VibraGramBackend/       # Spring Boot API (Java)
│── VibraGramFrontend/      # React Native App
│── .gitignore
│── README.md
```

---

## 🚀 Getting Started

### Backend (Spring Boot)

1. Navigate to the backend folder:

   ```bash
   cd backend
   ```
2. Build and run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

   or if you’re using Gradle:

   ```bash
   ./gradlew bootRun
   ```

The backend will run at **[http://localhost:8080](http://localhost:8080)** by default.

---

### Frontend (React Native)

1. Navigate to the frontend folder:

   ```bash
   cd frontend
   ```
2. Install dependencies:

   ```bash
   npm install
   ```
3. Start the development server:

   ```bash
   npm start
   ```

You can run the app on:

* **Android:** `npm run android`
* **iOS (Mac only):** `npm run ios`
* **Web (Expo):** `npm run web`

---

## ⚙️ Technologies

* **Backend:** Spring Boot, Java
* **Frontend:** React Native, Expo, JavaScript/TypeScript
* **Build Tools:** Maven/Gradle, Node.js
* **Version Control:** Git & GitHub

---

## 📌 Future Enhancements

* ✅ Set up project structure
* ⬜ Add API endpoints for authentication & user management
* ⬜ Connect frontend with backend API
* ⬜ Add database integration (PostgreSQL/MySQL)
* ⬜ Implement real-time features

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

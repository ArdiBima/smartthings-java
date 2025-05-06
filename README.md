# SmartThings Java Project

This is a SmartThings IoT backend project built using Java and the Ratpack framework. The project follows a layered architecture with `controller`, `service`, `repository`, and `model` packages.

## 📁 Project Structure

- `src/main/java/com/task1/smartthings` - Main source code
- `resources/db/init.sql` - SQL script to initialize the database
- `build.gradle` - Gradle build configuration
- `local.env` - Environment configuration

---

## 🛠 Prerequisites

- Java 17+
- Gradle (or use the included `gradlew` wrapper)
- PostgreSQL or your configured database

---

## 🧰 Step 1: Initialize the Database

1. Create a database (e.g., `smartthings_db`).
2. Run the SQL script to set up tables:

   ```bash
   psql -U your_user -d smartthings_db -f src/main/resources/db/init.sql
   ```

   > Replace `your_user` and `smartthings_db` with your actual database credentials. and set them in local.env file

---

## 📦 Step 2: Build the Project

Use the Gradle wrapper:

```bash
./gradlew build
```

Or if Gradle is installed globally:

```bash
gradle build
```

---

## 🚀 Step 3: Run the Application

```bash
./gradlew run
```

Or with global Gradle:

```bash
gradle run
```

The server will start, and you should see logs indicating that it is listening for requests.

---

## ⚙️ Configuration

Make sure to set your environment variables in `local.env` or use a `.env` loader in development.

Common variables include:

```env
DB_URL=jdbc:postgresql://localhost:5433/smartthings_db
DB_USER=your_user
DB_PASSWORD=your_password
TRANSLATOR_BASEURL=http://localhost:5050/v1/smartthings
JWT_PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\nyour_private_key\n-----END PRIVATE KEY-----"
JWT_PUBLIC_KEY= "-----BEGIN PUBLIC KEY-----\nyour_public_key\n-----END PUBLIC KEY-----"
ADMIN_USERNAME= your_admin_username
ADMIN_PASSWORD= your_admin_password
VENDOR_USERNAME= your_vendor_username
VENDOR_PASSWORD= your_vendor_password
```

---


## 📁 Packages Overview

- `controller` – Handles HTTP requests
- `service` – Business logic
- `repository` – Data access layer
- `model` – Domain objects
- `util` – Utility classes
- `config` – App and middleware configuration

---

## 📂 Postman Collection

You can export the Postman collection `Smartthings` from the `Smartthings.postman_collection.json` file.
<!-- 
## 📄 License

This project is licensed under the MIT License. -->

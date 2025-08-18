# PureLeaf - Tea Factory Backend

This is the backend API service for the PureLeaf Tea Factory Management System. Built with Java Spring Boot, PostgreSQL, and Firebase Authentication, it provides secure and scalable endpoints for managing factory operations, supply chains, users, and analytics.

---

## 🚀 Features

- RESTful API for all core system operations  
- User authentication and role-based authorization via Firebase  
- Supply chain and shipment management  
- Factory resource and workforce management  
- Automated reporting and analytics  
- Notification and alert endpoints  

---

## 🛠️ Technology Stack

- Java 17+ (Spring Boot)  
- PostgreSQL  
- Spring Data JPA  
- Spring Security  
- Firebase Authentication  
- Maven  

---

## 📦 Project Structure

```
pureleaf-backend/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── pureleaf/
│ │ │ ├── controller/                  # REST API controllers
│ │ │ ├── model/                       # Entity classes
│ │ │ ├── repository/                  # Spring Data JPA repositories
│ │ │ ├── service/                     # Business logic services
│ │ │ └── PureLeafApplication.java     # Main Spring Boot app class
│ │ └── resources/
│ │ ├── application.properties         # Configuration properties
│ │ ├── firebase/                      # Firebase service account JSON
│ │ └── static/                        # Static resources if any
├── pom.xml                            # Maven project configuration
└── README.md
```


---

## ⚡ Getting Started

### Clone the repository

```
git clone https://github.com/3rd-year-project-14/tea-factory-backend.git
cd tea-factory-backend
```

### Configure the database

Update `src/main/resources/application.properties` with your PostgreSQL database credentials:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Configure Firebase

1. Download your Firebase service account key JSON file from the Firebase Console.  
2. Place it under `src/main/resources/firebase/`.  
3. Add the path in your `application.properties` file:

```
firebase.config.path=src/main/resources/firebase/your-firebase-key.json
```

### Build and run the application
```
./mvnw clean install
./mvnw spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

User roles and permissions are managed via Firebase and backend logic.

---

## 🧪 Running Tests

Run unit and integration tests with:

```
./mvnw test
```

---

## 🤝 Contributing

- Fork the repository and create a feature branch.  
- Follow code conventions and best practices.  
- Write clear commit messages and update documentation as needed.  
- Submit pull requests for review.  

---

## 📢 Notes

- For frontend setup, see [PureLeaf Frontend Web Repository](https://github.com/3rd-year-project-14/tea-factory-frontend-web).  
- For mobile app features, see [PureLeaf Mobile App Repository](https://github.com/3rd-year-project-14/tea-factory-mobile-app).  

© 2025 3rd-year-project-14 Organization. All rights reserved.

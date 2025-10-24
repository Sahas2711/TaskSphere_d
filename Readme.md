# 🌐 TaskSphere: Microservices-Driven Team Task Management

**TaskSphere** is a high-availability, microservices-based application engineered to streamline team collaboration and maximize productivity through intelligent task prioritization.

It combines a modern **React** frontend with scalable **Spring Boot** microservices, all orchestrated for rapid deployment via **Docker**. This setup is designed for maintainability, independent scaling, and developer velocity.

---

## ✨ Project Highlights & Architecture

TaskSphere employs a **2-Layer Microservices Architecture** to clearly delineate responsibility and ensure operational independence.

| Layer | Service(s) | Primary Functionality | Database Schema | Inter-Service Communication |
| :--- | :--- | :--- | :--- | :--- |
| **Service Layer 1** | **User Service** | **Authentication, Authorization (JWT),** Team Management, Role Assignment. | `users_db` (`users`, `teams`) | N/A (Client-facing) |
| **Service Layer 2** | **Task Service** | Task CRUD, **Smart Prioritization (AI-Ready),** Notifications, Productivity Analytics. | `tasks_db` (`tasks`, `comments`) | **Feign Client** calls User Service for assignee lookup and validation. |
| **Presentation** | **React Frontend** | User Interface, consuming REST APIs from both services. | N/A | Direct API calls. |

---

## 🛠️ Technology Stack

| Category | Technology | Purpose |
| :--- | :--- | :--- |
| **Backend Framework** | **Spring Boot** (Java 17+) | Rapid development of RESTful microservices. |
| **Data Persistence** | **MySQL** | Reliable, transactional relational database for core data storage. |
| **Frontend** | **React** | Component-based, dynamic user interface. |
| **Containerization** | **Docker & Docker Compose** | Packaging and isolating services and dependencies for environment consistency. |
| **Version Control** | **Git** | Distributed source code management. |
| **Inter-Service Comms** | **Spring Cloud Feign** | Declarative REST client for seamless service-to-service interaction. |
| **API Documentation** | **Swagger/OpenAPI** | Defining and visualizing all service endpoints. |

---

## 🚀 Getting Started (Developer Workflow)

This guide outlines the quickest way to get all services, the database, and the frontend running locally using **Docker Compose**.

### Prerequisites

You must have the following installed on your machine:
* **Docker Engine** and **Docker Compose**
* **Java 17+** (for local development/debugging outside of Docker)
* **Node.js 18+** (for frontend development)
* **Git**

### 1. Repository Setup

```bash
# Clone the project and navigate into the root directory
git clone <repository_url> tasksphere
cd tasksphere
````

### 2\. Build & Deploy with Docker Compose (Recommended)

The `docker-compose.yml` file handles the creation of the networks, volumes, two MySQL instances, the two Spring Boot services, and the React frontend.

1.  **Build the Service Images:**
    The following command builds the JARs for the Spring Boot services, the Docker images, and the frontend artifacts.

    ```bash
    docker compose build
    ```

2.  **Start the Entire Stack:**
    This command pulls the MySQL image, and starts all four custom services/containers in detached mode.

    ```bash
    docker compose up -d
    ```

| Component | Default Access URL |
| :--- | :--- |
| **React Frontend** | `http://localhost:3000` |
| **User Service** | `http://localhost:8080` |
| **Task Service** | `http://localhost:8081` |
| **User Service DB** | Host: `mysql-users`, Port: 3306 |
| **Task Service DB** | Host: `mysql-tasks`, Port: 3306 |

3.  **Clean Up:**
    To stop and remove all containers, networks, and volumes:
    ```bash
    docker compose down -v
    ```

-----

## 💡 Key API Endpoints

The full contract is documented via the **Swagger/OpenAPI** file (`taskspare.json`).

### User Service Endpoints (`http://localhost:8080/api/users/`)

| Endpoint | Method | Security | Description |
| :--- | :--- | :--- | :--- |
| `/register` | `POST` | Public | Creates a new user account. |
| `/login` | `POST` | Public | Authenticates and issues a **JWT** token. |
| `/profile/{id}` | `GET` | **Secured (JWT)** | Retrieves user details. |
| `/teams` | `POST` | **Secured (JWT)** | Creates a new team. |

### Task Service Endpoints (`http://localhost:8081/api/tasks/`)

| Endpoint | Method | Security | Description |
| :--- | :--- | :--- | :--- |
| `/` | `POST` | **Secured (JWT)** | Creates a new task. |
| `/{id}` | `PUT` | **Secured (JWT)** | Updates task status, progress, or priority. |
| `/{id}/reassign` | `PUT` | **Secured (JWT)** | Reassigns the task to another user (requires User Service lookup). |
| `/analytics` | `GET` | **Secured (JWT)** | Fetches AI-driven productivity insights. |

-----

## 📄 Database Schemas

The project utilizes two separate MySQL databases to enforce microservice data independence.

### `users_db` (Owned by User Service)

| Table Name | Description | Key Columns | Relationships (FK) |
| :--- | :--- | :--- | :--- |
| `users` | Core user credentials and roles. | `id` (PK), `email`, `role` | - |
| `teams` | Organization of teams. | `id` (PK), `name` | `created_by` $\rightarrow$ ` users(id)$ | |  `team\_members`| Mappings of users to teams. |`id`(PK) |`team\_id`$\rightarrow$`teams(id)$ , `user_id` $\rightarrow$ \`users(id)$ |

### `tasks_db` (Owned by Task Service)

| Table Name | Description | Key Columns | Relationships (FK) |
| :--- | :--- | :--- | :--- |
| `tasks` | Task details, status, and AI priority score. | `id` (PK), `title`, `deadline`, `priority` | `assigned_to` $\rightarrow$ ` users(id)$ | |  `comments`| Task-specific discussion logs. |`id`(PK),`text`|`task\_id`$\rightarrow$`tasks(id)$ , `commented_by` $\rightarrow$ ` users(id)$ | |  `activity\_logs`| Audit trail for task actions (e.g., status changes, reassignments). |`id`(PK),`action`|`task\_id`$\rightarrow$`tasks(id)$ , `user_id` $\rightarrow$ \`users(id)$ |

-----

## 🤝 Contribution Guidelines

We welcome contributions\! Please ensure you adhere to the following:

1.  Fork the repository.
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request against the main branch, detailing the changes and linking to any relevant issue.

<!-- end list -->


## 📜 License
This project is licensed under the MIT License. See the LICENSE file in the root directory for details.
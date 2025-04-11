# Power BI Project

This repository contains a full-stack application for managing and sharing dashboard links. It consists of two main components:

1. **Power BI Frontend**: A React-based frontend application for managing users, channels, and links.
2. **Power BI API**: A Spring Boot-based REST API for backend operations, including user authentication, channel management, and link sharing.

---

## Features

### Frontend
- **User Management**: Manage users, super users, and their roles.
- **Channel Management**: Create, update, and manage channels with roles like Admin, Member, and Owner.
- **Link Management**: Add, edit, and view links associated with channels.
- **Authentication**: Secure login using Azure authentication.
- **Custom Theme**: Light and dark mode themes using Material UI.

### Backend
- **Channel Management**: Create, update, delete, and manage channels and their members.
- **User Management**: Manage users and their roles within the system.
- **Permission Management**: Handle access control and permissions for various resources.
- **Azure AD Integration**: Secure the API using OAuth2 with Azure Active Directory.
- **JWT Authentication**: Issue and validate JSON Web Tokens for session management.
- **Database Integration**: Uses Microsoft SQL Server for data persistence.

---

## Tech Stack

### Frontend
- **React**: Frontend library for building user interfaces.
- **TypeScript**: Strongly typed JavaScript for better development experience.
- **Material UI (Joy)**: UI components for consistent design.
- **Axios**: HTTP client for API calls.
- **React Router**: For routing and navigation.
- **Vite**: Fast development server and build tool.

### Backend
- **Spring Boot**: Framework for building REST APIs.
- **Java**: Backend programming language.
- **Gradle**: Build tool for managing dependencies and builds.
- **Microsoft SQL Server**: Database for data persistence.
- **Azure AD**: Authentication and authorization.

---

## Prerequisites

### Frontend
- Node.js 18 or higher
- npm

### Backend
- Java 23 or higher
- Gradle
- Microsoft SQL Server
- Azure Active Directory credentials

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/hm-yang2/power-bi.git

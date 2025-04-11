# Power BI API

This is a Spring Boot-based REST API for managing dashboards with features such as channels, users, and permissions. The API provides endpoints for CRUD operations and integrates with Azure Active Directory for authentication and authorization.

## Features

- **Channel Management**: Create, update, delete, and manage channels and their members.
- **User Management**: Manage users and their roles within the system.
- **Permission Management**: Handle access control and permissions for various resources.
- **Azure AD Integration**: Secure the API using OAuth2 with Azure Active Directory.
- **JWT Authentication**: Issue and validate JSON Web Tokens for session management.
- **Database Integration**: Uses Microsoft SQL Server for data persistence.
- **Environment Configuration**: Supports `.env` files for managing environment-specific configurations.

## Prerequisites

- Java 23 or higher
- Gradle
- Microsoft SQL Server
- Azure Active Directory credentials

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/hm-yang2/power-bi.git
cd power-bi/api
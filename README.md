# UrbexProject

**UrbexProject** is a web application designed for managing and exploring Urbex (urban exploration) locations, particularly abandoned places across Italy. The project provides a platform where users can discover and review Urbex sites, and administrators can manage the details of each location.

## Features

### User Features
- **Browse Locations**: Users can view available Urbex locations.
- **Search Locations**: Search by Italian region or by address to find specific sites.
- **Review and Rate**: Users can submit reviews and rate locations.

### Admin Features
- **Add Locations**: Administrators can add new locations, including detailed descriptions and images.
- **Edit Locations**: Modify location information with a small embedded map (using OpenStreetMap) to specify precise addresses.
- **Manage Reviews**: Administrators have the ability to remove reviews as needed.

## Technologies Used

- **Backend**: Java with Spring Framework (Spring Boot)
- **Database**: PostgreSQL with pgAdmin for management
- **Frontend**: HTML, CSS, JavaScript, Bootstrap for responsive design
- **Mapping**: OpenStreetMap API for geolocation functionality
- **Architecture**: Follows the MVC pattern with separate layers for Controllers, Repositories, and Services.

## Project Structure

- **Controllers**: Handle HTTP requests and user interactions.
- **Services**: Contain business logic and interact with repositories.
- **Repositories**: Manage database operations with Spring Data JPA.
  
## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/UrbexProject.git

Bitcoin Price Service
    This service provides historical Bitcoin prices based on user-provided start and end dates, along with currency conversion options. It includes offline mode support, caching, and ensures data availability even when the external API is down.

Features
    Fetch historical Bitcoin prices for a user-defined date range.
    Currency conversion based on real-time exchange rates (default is USD).
    Offline mode that uses cached data when enabled.
    Caching of API responses to reduce external API calls.
    RESTful API for easy integration with frontend or external services.
    Technologies Used
    Java Spring Boot
    REST API using Spring MVC
    H2 Database (for caching)
    Maven (for dependency management)
    Jackson (for JSON processing)
    External API: CoinDesk Bitcoin Price API

    Endpoints
        Get Historical Prices

        POST /api/bitcoin/historical-prices
        Request:
        json
        Copy code
        {
        "startDate": "YYYY-MM-DD",
        "endDate": "YYYY-MM-DD",
        "currency": "USD"
        }
        Response:
        json
        Copy code
        {
        "prices": [
            { "date": "YYYY-MM-DD", "price": 45000.75 },
            { "date": "YYYY-MM-DD", "price": 47000.89 }
        ],
        "minPrice": 45000.75,
        "maxPrice": 47000.89
        }
        Offline Mode Toggle

        POST /api/bitcoin/offline-mode?offlineMode=true
        GET /api/bitcoin/offline-mode to check the current mode (offline or online).

    How It Works
        The service fetches Bitcoin prices from the external CoinDesk API for a given date range. It supports the conversion of prices to different currencies and caches the result for future use. If offline mode is enabled, the service serves data from the cache.


Key Classes
    BitcoinPriceService: The core service that handles fetching historical Bitcoin prices from CoinDesk API, calculating the min and max prices, and caching the results. It also handles the logic for offline mode.
    BitcoinPriceCacheRepository: Responsible for interacting with the H2 database to store and retrieve cached results.
    ExchangeRateService: Provides real-time exchange rate conversion using an external API.
    BitcoinPriceCache: Entity representing cached data.

    Design Patterns Used:
        
        Repository Pattern:

            Description: The repository pattern is used in BitcoinPriceCacheRepository to abstract the interaction with the underlying database (H2). It provides a simple interface to perform CRUD operations without exposing the database details.
            Why: This pattern promotes loose coupling between the service and data layer, making the code more maintainable and scalable.

        Template Method Pattern:

            Description: The fetchBitcoinPricesFromApi method in BitcoinPriceService fetches historical Bitcoin prices using a predefined template of steps, such as preparing the API URL, handling the response, and processing the data.
            Why: This promotes code reusability and consistency across multiple API requests.
            
        Singleton Pattern:

            Description: The RestTemplate is implemented as a singleton to ensure that only one instance of the HTTP client exists throughout the application lifecycle.
            Why: This avoids the overhead of creating new instances and provides a performance boost in network communications.

        Cache-aside Pattern:

            Description: The service first checks the cache before making an API call. If the data exists in the cache, it returns that; otherwise, it fetches data from the external API and stores it in the cache for future use.
            Why: This pattern improves performance by reducing the number of API calls and ensures that the service can function in offline mode.

Getting Started
    Prerequisites
        JDK 11+
        Maven
        Postman (for testing API)
        H2 Database (in-memory, no setup required)
        Installation

    Clone the repository:

    bash
    Copy code
    git clone https://github.com/abhishekst28/bitcoin-price-service.git
    cd bitcoin-price-service
    Build the project:

    bash
    Copy code
    mvn clean install
    Run the application:

    bash
    Copy code
    mvn spring-boot:run
    Testing
    Open Postman and send a POST request to:

    bash
    Copy code
    http://localhost:8080/api/bitcoin/historical-prices
    with the following JSON body:

    json
    Copy code
    {
    "startDate": "2023-01-01",
    "endDate": "2023-01-10",
    "currency": "USD"
    }
    Check the /api/bitcoin/offline-mode toggle using a GET request to:

    bash
    Copy code
    http://localhost:8080/api/bitcoin/offline-mode
    Deployment
    You can deploy this service on platforms like Render, AWS, or DigitalOcean. For more detailed steps, refer to the platform-specific documentation.

License
    This project is licensed under the MIT License.

    This README provides a complete overview of your backend service, including key architectural details, design patterns, and instructions for installation, running, and testing the service.
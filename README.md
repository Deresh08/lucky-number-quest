# Lucky Number Quest

## How to Run the Application

To run the application, follow these steps:

1. Open a terminal or command prompt in the root directory.
2. Run the command `mvn spring-boot:run`.
3. Once the application is running, you can access the API from your browser using the following URL: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

## Application Design

The Lucky Number Quest application is designed with modularity and flexibility in mind. It consists of separate services that can function independently and communicate with each other effectively.

- **AppGateway**: This service exposes client-facing services, providing an entry point for external interactions.

- **GameService**: Responsible for managing game-related logic, such as generating random numbers for gameplay.

- **BetService**: Handles the placement of bets and processes outcomes based on events. It also stores odds for different events.

- **WalletService**: Manages the creation and transactions of wallets. It allows for the possibility of multiple wallets per player and supports different currencies.

- **PlayerService**: Handles player registration and wallet initialization.

This modular design enables the application to accommodate future enhancements and expansions, such as:

- Adding new games to the system.
- Enhancing the bet service to handle various types of bets.
- Allowing multiple wallets per player and supporting different currencies in the wallet service.

By adopting this architecture, Lucky Number Quest can adapt and evolve to meet changing requirements and scale efficiently.

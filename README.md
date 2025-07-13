# Nearby Search Bot

Nearby Search Bot is a Telegram bot built with Spring Boot that helps users find nearby places (restaurants, cafes, shops, etc.) using the Google Places API. It supports both keyword-based and location-based search and responds in the user's preferred language.

## Features

- Search nearby places using text queries and current location
- Integration with Google Places API
- Supports three languages: English (EN), Russian (RU), and Ukrainian (UA)
- Structured and formatted results with links and basic details, sorted by distance from the user
- Supports webhook operation mode

## Technologies

- Java 17+
- Spring Boot
- TelegramBots Java API
- Google Places API
- JUnit 5 / Mockito for testing
- Maven
- Fly.io (optional deployment platform)

## Getting Started

1. Clone the repository

       git clone https://github.com/your-username/nearby-search-bot.git && cd nearby-search-bot

2. Build the Docker image

        docker build -t nearby-search-bot .

3. Run the container

        docker run -d -p 8080:8080 \
          -e BOT_NAME=NearbySearchBot \
          -e BOT_TOKEN=your_token \
          -e GOOGLE_PLACES_API_KEY=your_api_key \
          nearby-search-bot

4. Set the Telegram webhook

Visit the following URL in your browser to register the webhook:

    https://api.telegram.org/bot<BOT_TOKEN>/setWebhook?url=<YOUR_PUBLIC_URL>/webhook

Example:

    https://api.telegram.org/bot123456789:example_token/setWebhook?url=https://abc123.com/webhook

## Getting API Keys

### Telegram Bot Token

1. Open Telegram and start a chat with [@BotFather](https://t.me/BotFather).
2. Use the `/newbot` command and follow the instructions.
3. After completion, you will receive a token like: `123456789:ABCDefGhIjKlmNoPQRstuVWxyz1234567890`
4. Use this as the `BOT_TOKEN` environment variable.

### Google Places API Key

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project (or use an existing one).
3. Enable the **Places API** under "APIs & Services" > "Library".
4. Go to "Credentials" and create an **API key**.
5. Restrict the key to the Places API (optional but recommended).
6. Use this as the `GOOGLE_PLACES_API_KEY` environment variable.

# ApolloTracker

ApolloTracker is a cryptocurrency tracking application that allows users to view the current price, historical data, and other relevant information about various cryptocurrencies. The app provides real-time updates and graphical representations of price trends.

## Description of the App

ApolloTracker is designed to help users keep track of their favorite cryptocurrencies. The main features of the app include:
- Viewing the current price of Bitcoin and other altcoins.
- Accessing historical price data and visualizing it through interactive graphs.
- Switching between different currencies to see prices in the preferred currency.
- A smooth and responsive UI built with Jetpack Compose.

### Bonus Items Implemented

1. **Automatic Refresh**: The app automatically refreshes the currently displayed prices every 1 minute. This ensures that users always have the latest price information without having to manually refresh.
2. **Currency Selection**: Users can select the currency of the displayed prices from USD, GBP, and EUR. The app remembers the user's selection across re-launches by saving it in shared preferences. Prices are fetched in the selected currency using query parameters in the API call.
3. **Historical Price Graph**: The app displays past prices in a graph using the MPAndroidChart library. Historical prices are fetched from the CoinPaprika API, and the data is plotted on an interactive graph.
4. **Unit and UI Tests**: The app includes comprehensive unit and UI tests to ensure the correctness of the functionality and user interface.

## Third-Party Libraries Used

The app utilizes several third-party libraries to enhance its functionality. These libraries include:

- **Chucker**: A library to intercept and view HTTP requests/responses. Useful for debugging network calls.
- **Retrofit**: A type-safe HTTP client for Android and Java to handle REST API calls.
- **OkHttp**: An HTTP client that supports HTTP/2, web sockets, and connection pooling.
- **Gson**: A library to convert Java objects to JSON and vice versa.
- **Coil**: An image loading library for Android backed by Kotlin Coroutines.
- **MPAndroidChart**: A powerful charting library for Android to create beautiful and interactive charts.
- **Hilt**: A dependency injection library for Android that reduces the boilerplate of manual dependency injection.
- **MockK**: A mocking library for Kotlin.

## App Design and Architecture

ApolloTracker follows the MVI (Model-View-Intent) pattern combined with a derivation of Redux architecture. The app uses a unidirectional data flow to manage the state effectively and ensure a responsive user interface.

### Design Principles

1. **Single Source of Truth**: The app's state is managed centrally using `StateFlow` and `ModelStore`. This ensures consistency and predictability in state management.
2. **Unidirectional Data Flow**: The app employs a unidirectional data flow where the UI sends intents to the ViewModel, which processes them and updates the state. The state is then observed by the UI to render updates.
3. **Decoupled Components**: The architecture promotes decoupling of components, making the app more modular and easier to test.

### Core Components

- **ApolloTrackerApplication**: The main Application class annotated with `@HiltAndroidApp` to setup Hilt.
- **MainActivity**: The entry point of the app, hosting the navigation graph and setting up the initial UI.
- **NetworkModule**: Provides network-related dependencies like Retrofit and OkHttpClient.
- **DispatcherModule**: Provides different Coroutine Dispatchers for managing background tasks.
- **Router**: Manages the navigation and the current route of the application.
- **CoinRepository**: Handles data operations and provides methods to fetch data from the CoinPaprika API.
- **ViewModels**: Separate ViewModel classes for each screen (e.g., `MainViewModel`, `GraphViewModel`, `AltcoinViewModel`, `SettingsViewModel`, `SplashViewModel`) to handle the business logic and UI state management.
- **Composables**: Various Jetpack Compose functions to build the UI (e.g., `GraphScreen`, `MainScreen`, `SettingsScreen`, `AltcoinScreen`, `SplashScreen`).

The app is designed to be maintainable and scalable, allowing for easy addition of new features and modifications.

#### ModelStore and StatefulStore

The `ModelStore` interface represents a state management store using `StateFlow`. The `StatefulStore` implementation ensures thread-safe state updates using a `Mutex` and sequential processing of state reducers using a `Channel`.

```kotlin
interface ModelStore<T> {
    val state: StateFlow<T>
    fun process(reducer: (T) -> T)
}

class StatefulStore<T>(initialValue: T, private val scope: CoroutineScope) : ModelStore<T> {
    private val _state = MutableStateFlow(initialValue)
    override val state: StateFlow<T> get() = _state.asStateFlow()
    private val updateChannel = Channel<(T) -> T>(Channel.UNLIMITED)
    private val mutex = Mutex()

    init {
        scope.launch {
            for (reducer in updateChannel) {
                mutex.withLock {
                    _state.update { reducer(it) }
                }
            }
        }
    }

    override fun process(reducer: (T) -> T) {
        scope.launch {
            updateChannel.send(reducer)
        }
    }
}

# Frantic
![Heroku](https://heroku-badge.herokuapp.com/?app=sopra-fs20-group-09-server)

Frantic is a chaotic card game created by Rulefactory. This repository is the back-end part of an online implementation for this card game.

Please find the current build under http://frantic.online/.

## Development

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

# kman

Kman is a handy little CLI tool I developed to help me keep my sensitive data safe and sound.
The app encrypts my credentials with a symmetric encryption key and stores it securely in a
database. When I need to retrieve some data, the app will decrypt the database content and
display the credentials for me.

## Requirements

The project requires the following dependencies to run:

- JRE/JDK 21
- MySQL 8 or newer

## Building The Project

The easier way to use the application is to directly download the fat jar from the [release page](https://github.com/aymenhta/kman/releases). Or if you can use the following commands to build it.

```shell
git clone https://github.com/aymenhta/kman
cd ./kman
./gradlew build
```

## Running The Application

Before using the application you need to set the following environment variable:

```shell
export KMAN_DATABASE_URI=
export KMAN_DATABASE_USER=
export KMAN_DATABASE_PASSWORD=
export KMAN_KEY=
```

when using the application you'll be prompted with the usage help message:

```shell
java -jar .\build\libs\kman-1.0.0-standalone.jar
```

```text
Usage: kman [<options>] <command> [<args>]...

  a cli tool to manage your credentials

Options:
  -h, --help  Show this message and exit

Commands:
  generate      generate a random password/encryption key
  add           add a new credential to the database
  delete        delete a credential from the database
  list          list all credentials in the database
  export        export database entries to json
```

Kman is easy to explore by yourself, feel free to play with it.

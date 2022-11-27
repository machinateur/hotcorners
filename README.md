# hotcorners

A minimalist approach to hotcorners for windows.

## Prerequisites

- Java Runtime (JRE Version 8 or later; JRE Version 17 is recommended)

Make sure to add the location of the java runtime to the `PATH` environment variable.

## Installation

Currently, the only way to install the application is to download the `.jar` file. It is attached to the latest release.

Note that the while a release comes as `.zip` file of all sources, most of its content is not required.

## Usage

To run the program, it's recommended to use the provided `hotcorners.cmd` file. It has to be placed in the same
directory as the `hotcorners.jar` file.

### Command line

You can also run the `.jar` file directly from the command line using the following command:

```cmd
java -jar hotcorners.jar
```

### Command line options

There is a number of command line options that are parsed sequentially. See what they do below.

| Command                           | Description                                                                   |
|-----------------------------------|-------------------------------------------------------------------------------|
| `--verbose`                       | Write any mouse movement detection to the output stream (for debugging).      |
| `--run-configuration-calculation` | Run a calculation based on the primary screen size to determine corner areas. |
| `--store-configuration`           | Write the configuration. This will erase any existing configuration.          |
| `--exit`                          | Exit after parsing of all command line options.                               |

### Without the command line window

In case you do not wish to see the command line window that opens when running that way, you could also just
double-click the `.jar`. Just be informed, that currently there is no way to stop the application, other than killing
the correct java process from within the task manager.

## Configuration

Upon first running the application, a new configuration file will be created. That configuration assumes a standard HD
screen (`1920x1080`).

To generate a configuration that is based on your individual primary screen size, run the `hotcorners-configuration.cmd`
file.

The configuration is written to the `hotcorners.properties` file, which can be changed to meet your needs.

Any line starting with the `@` symbol, will be interpreted as area configuration (or at least tried to). The key format
is `@x0,y0,x1,y1`. Spaces in keys have to be escaped according to the `properties` format. The area is created by
using `x0` and `y0` to create a point `p0` on the screen coordinate system, which is then extended to a point `p1`,
which is defined by `x1` and `y1`. The key is followed by an `=` sign. The value is a `+`-separated list
of [VK_-Constants](https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html#field_summary). Currently,
there is limit of `5` for the list of values.

## Screen resolution matters

The screen resolution matters, as it reduces the virtual screen size. So with a zoom factor of `125%`, some areas of the
screen you've configured for keystrokes might not be accessible to the mouse! Just keep that in mind.

## Executing keystrokes

Any one of the [VK_-Constants](https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html#field_summary)
that is supported by your keyboard can be added to the list. It's up to you to decide which ones make sense.

The defaults are just the ones I use.

## License

It's MIT.

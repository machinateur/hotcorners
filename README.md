# hotcorners

A minimalist approach to hotcorners for windows.

## Prerequisites

- Java Runtime (JRE Version 8 or later; JRE Version 17 is recommended)

Make sure to add the location of the java runtime to the `PATH` environment variable.

## Installation

Currently, the only way to install the application is to download the `.jar` file. It contained in the `.zip` file,
attached to the [latest release](https://github.com/machinateur/hotcorners/releases/latest).

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
of [VK_-Constants](https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html#field_summary) by name.
Currently, there is limit of `5` for the list of values, which seemed reasonable to me.

There is a special `delay` configuration key, which sets the amount of milliseconds that's used for global mouse
detection. It's set to a sensible default of `10`. In case you encounter a performance impact, play around with this
value.

Here is an example of the initial configuration:

```properties
#See https://en.wikipedia.org/wiki/.properties#Format and https://github.com/machinateur/hotcorners#configuration for more information on the format.
#Sat Nov 26 19:35:19 CET 2022
#Corner top left
@0,0,2,2=VK_CONTROL + VK_ALT + VK_TAB
#Corner bottom left
@0,1078,2,1080=VK_WINDOWS + VK_TAB
#Corner top left
@1918,0,1920,2=VK_WINDOWS + VK_A
#Corner bottom right
@1918,1078,1920,1080=VK_WINDOWS + VK_D
#Delay for mouse movement detection in milliseconds
delay=10

```

### Screen resolution matters

The screen resolution matters, as it reduces the virtual screen size. So with a zoom factor of `125%`, some areas of the
screen you've configured for keystrokes might not be accessible to the mouse! Just keep that in mind.

### Executing keystrokes

Any one of the [VK_-Constants](https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html#field_summary)
that is supported by your keyboard can be added to the list. It's up to you to decide which ones make sense.

The defaults are just the ones I use.

### A word of warning

Just a word of warning: **A faulty configuration may result in significant usability issues.** For example if an action
area is defined over the whole screen area, tabbing through the windows, that would most certainly be annoying. That
was the reason I introduced the automatic calculation functionality in the first place (yes, I miscalculated my areas
during testing, let's not talk about it).

## License

It's MIT.

/*
 * MIT License
 *
 * Copyright (c) 2021-2022 machinateur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.machinateur.hotcorners;

import java.time.Year;

final public class Main {

    public static final String VERSION = "0.1.0";

    public static final String NEW_LINE = "%n";

    private static boolean VERBOSE_MODE = false;

    private Main() {
    }

    public static void main(String[] args) {
        Year year = Year.now();

        System.out.printf("hotcorners %s - https://github.com/machinateur/hotcorners" + Main.NEW_LINE, Main.VERSION);
        System.out.printf("Copyright (c) 2021-%d machinateur" + Main.NEW_LINE, year.getValue());
        System.out.println();

        ApplicationConfiguration configuration = Main.getApplicationConfiguration(args);

        Application application = new Application(configuration);
        application.start();
    }

    private static ApplicationConfiguration getApplicationConfiguration(String[] args) {
        ApplicationConfiguration configuration = new ApplicationConfiguration();

        if (args.length > 0) {
            boolean exit = false;
            int exitCode = 0;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--run-configuration-calculation" -> {
                        System.out.println("Argument: Run configuration calculation based on primary screen size...");

                        configuration = new ApplicationConfigurationCalculation();
                    }
                    case "--store-configuration" -> {
                        System.out.println("Argument: Store configuration to file...");

                        configuration.storeConfiguration();
                    }
                    case "--exit" -> {
                        System.out.println("Argument: Exit afterwards...");

                        exit = true;
                    }
                    case "--verbose" -> {
                        System.out.println("Argument: Activate verbose mode...");

                        Main.VERBOSE_MODE = true;
                    }
                    default -> {
                        System.out.printf("Argument: Unknown: '%s'!" + Main.NEW_LINE, args[i]);
                    }
                }
            }

            if (exit) {
                System.exit(exitCode);
            }

            System.out.println();
        }

        return configuration;
    }

    public static boolean isVerboseMode() {
        return Main.VERBOSE_MODE;
    }
}

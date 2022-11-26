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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class Application extends MouseMotionSubscriber {

    private static final String CONFIGURATION_FILE_PATH
            = "./hotcorners.properties";

    private static final String CONFIGURATION_FILE_COMMENTS
            // TODO: Link to the repository README.md section.
            = "See https://en.wikipedia.org/wiki/.properties#Format and  for more information on the format.";

    public static final String CONFIGURATION_SPLIT_REGEX
            = "[,;+]";

    private static final char CONFIGURATION_MARKER = '@';

    private static final int COORDINATE_PAIR_SIZE
            = 4;

    private static final int COMMAND_STACK_SIZE
            = 5;

    private final MouseMotionObserver mouseMotionObserver;

    private final Map<Rectangle, int[]> keyStrokeMap;

    private final Robot robot;

    public Application() {
        final Properties configurationDefaults = this.initializeConfiguration();
        final Properties configuration = new Properties();

        configuration.putAll(configurationDefaults);

        this.loadConfiguration(configuration);

        Component component = new JPanel();
        int delay = 10;

        if (configuration.containsKey("delay")) {
            delay = Integer.parseInt((String) configuration.remove("delay"));
        }

        this.mouseMotionObserver = new MouseMotionObserver(component, delay);
        this.mouseMotionObserver.addMouseMotionListener(this);

        this.keyStrokeMap = this.parseConfiguration(configuration);

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        this.mouseMotionObserver.start();
    }

    public void stop() {
        this.mouseMotionObserver.stop();
    }

    private Properties initializeConfiguration() {
        final Properties configurationDefaults = new Properties();

        // The default configuration value of the timer delay.
        configurationDefaults.put("delay", "10");

        // The default configuration for a standard HD screen (1920x1080).
        configurationDefaults.put("@0,0,2,2", "vk_control + vk_alt + vk_tab");
        configurationDefaults.put("@1918,0,1920,0", "vk_windows + vk_a");
        configurationDefaults.put("@0,1078,2,1080", "vk_windows + vk_tab");
        configurationDefaults.put("@1918,1078,1920,1080", "vk_windows + vk_d");

        return configurationDefaults;
    }

    private void loadConfiguration(Properties configuration) {
        try (
                InputStream inputStream = new FileInputStream(Application.CONFIGURATION_FILE_PATH);
        ) {
            configuration.load(inputStream);
        } catch (FileNotFoundException e) {
            this.storeConfiguration(configuration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeConfiguration(Properties configuration) {
        try (
                OutputStream outputStream = new FileOutputStream(Application.CONFIGURATION_FILE_PATH);
        ) {
            configuration.store(outputStream, Application.CONFIGURATION_FILE_COMMENTS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Rectangle, int[]> parseConfiguration(Properties configuration) {
        final Map<Rectangle, int[]> keyStrokeMap = new HashMap<Rectangle, int[]>();

        try {
            for (Map.Entry<Object, Object> entry : configuration.entrySet()) {
                String key = (String) entry.getKey();
                // Remove any whitespaces from the key.
                key = key.trim();

                // Skip any key that does not start with the marker char (i.e. "@").
                if (key.charAt(0) != Application.CONFIGURATION_MARKER) {
                    System.out.printf("Configuration: Skip key '%s'..." + Main.NEW_LINE, key);

                    continue;
                } else {
                    System.out.printf("Configuration: Parse key '%s'..." + Main.NEW_LINE, key);

                    key = key.substring(1);
                }

                String value = (String) entry.getValue();

                System.out.printf("Configuration: Parse value '%s'..." + Main.NEW_LINE, value);

                keyStrokeMap.put(this.parseConfigurationKey(key), this.parseConfigurationValue(value));

                System.out.println();
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return keyStrokeMap;
    }

    private Rectangle parseConfigurationKey(String key) {
        // Cut the coordinates into pieces, i.e. values, at any of the split chars (regex).
        String[] coordinatePair = key.split(Application.CONFIGURATION_SPLIT_REGEX);

        // Make sure the size matches up.
        if (coordinatePair.length != Application.COORDINATE_PAIR_SIZE) {
            System.out.printf("Configuration: Intersection-Area: Invalid!" + Main.NEW_LINE);

            throw new IndexOutOfBoundsException(coordinatePair.length);
        }

        // Remove any whitespaces from the values and parse the coordinates into p0/p1.
        int x0 = Integer.parseInt(coordinatePair[0].trim());
        int y0 = Integer.parseInt(coordinatePair[1].trim());
        int x1 = Integer.parseInt(coordinatePair[2].trim());
        int y1 = Integer.parseInt(coordinatePair[3].trim());

        Point p0 = new Point(x0, y0);
        Point p1 = new Point(x1, y1);

        // Construct the rectangle of the area.
        final Rectangle rectangle = new Rectangle((int) p0.getX(), (int) p0.getY(), 0, 0);
        rectangle.add(p1);

        System.out.printf("Configuration: Intersection-Area: %s." + Main.NEW_LINE, rectangle);

        return rectangle;
    }

    private int[] parseConfigurationValue(String value) throws NoSuchFieldException, IllegalAccessException {
        // Create a limited size command stack.
        int[] commandStack = new int[Application.COMMAND_STACK_SIZE];
        // Cut the coordinates into pieces, i.e. values, at any of the split chars (regex).
        String[] commandList = value.split(Application.CONFIGURATION_SPLIT_REGEX);

        if (commandList.length > commandStack.length) {
            System.out.printf("Configuration: Command-Stack: Invalid!" + Main.NEW_LINE);

            throw new IndexOutOfBoundsException(commandList.length);
        }

        for (int i = 0; i < commandList.length; i++) {
            // Remove any whitespaces from the command (i.e. "VK_*" constant name), then convert them to uppercase.
            String command = commandList[i].trim()
                    .toUpperCase();

            // Try to find the static field corresponding to the given command.
            Field field = KeyEvent.class.getDeclaredField(command);

            if (Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                int commandValue = commandStack[i] = field.getInt(null);

                System.out.printf("Configuration: Command %d: '%s' = %d" + Main.NEW_LINE, i, command, commandValue);
            } else {
                System.out.printf("Configuration: Command %d: '%s' = %s" + Main.NEW_LINE, i, command, "(unknown)");
            }
        }

        return commandStack;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        final Point point = event.getPoint();

        for (final Map.Entry<Rectangle, int[]> keyStrokeEntry : this.keyStrokeMap.entrySet()) {
            final Rectangle area = keyStrokeEntry.getKey();

            if (area.contains(point)) {
                System.out.printf("Event: Intersection of %s in %s" + Main.NEW_LINE, point, area);

                this.executeKeyStrokeSequence(keyStrokeEntry.getValue());
            } else {
                System.out.printf("Event: Intersection of %s in %s" + Main.NEW_LINE, point, "(unknown)");
            }
        }
    }

    private void executeKeyStrokeSequence(int[] keyStroke) {
        for (int key : keyStroke) {
            if (KeyEvent.VK_UNDEFINED == key) {
                continue;
            }

            System.out.printf("Execute: Key-Press: %d..." + Main.NEW_LINE, key);

            this.robot.keyPress(key);
        }
        for (int key : keyStroke) {
            if (KeyEvent.VK_UNDEFINED == key) {
                continue;
            }

            System.out.printf("Execute: Key-Release: %d..." + Main.NEW_LINE, key);

            this.robot.keyRelease(key);
        }
    }
}

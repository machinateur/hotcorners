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
import java.util.Map;

public final class Application extends MouseMotionSubscriber {

    private final MouseMotionObserver mouseMotionObserver;

    private final Map<Rectangle, int[]> keyStrokeMap;

    private final Robot robot;

    public Application(ApplicationConfiguration configuration) {
        configuration.loadConfiguration();

        Component component = new JPanel();
        int delay = 10;

        if (configuration.containsKey("delay")) {
            delay = Integer.parseInt((String) configuration.remove("delay"));
        }

        this.mouseMotionObserver = new MouseMotionObserver(component, delay);
        this.mouseMotionObserver.addMouseMotionListener(this);

        this.keyStrokeMap = configuration.parseConfiguration();

        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public Application() {
        this(new ApplicationConfiguration());
    }

    public void start() {
        this.mouseMotionObserver.start();
    }

    public void stop() {
        this.mouseMotionObserver.stop();
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
                if (Main.isVerboseMode()) {
                    System.out.printf("Event: Intersection of %s in %s" + Main.NEW_LINE, point, "(unknown)");
                }
            }
        }
    }

    private void executeKeyStrokeSequence(int[] keyStroke) {
        for (int key : keyStroke) {
            if (KeyEvent.VK_UNDEFINED == key) {
                continue;
            }

            if (Main.isVerboseMode()) {
                System.out.printf("Execute: Key-Press: %d..." + Main.NEW_LINE, key);
            }

            this.robot.keyPress(key);
        }
        for (int key : keyStroke) {
            if (KeyEvent.VK_UNDEFINED == key) {
                continue;
            }

            if (Main.isVerboseMode()) {
                System.out.printf("Execute: Key-Release: %d..." + Main.NEW_LINE, key);
            }

            this.robot.keyRelease(key);
        }
    }
}

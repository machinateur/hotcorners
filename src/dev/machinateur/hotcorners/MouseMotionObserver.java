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

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

final public class MouseMotionObserver implements ActionListener {
    private final int delay;

    private final Timer timer;

    private final Component source;

    private final Set<MouseMotionListener> listenerSet;

    private Point point;

    public MouseMotionObserver(Component source, int delay) {
        if (source == null) {
            throw new IllegalArgumentException("Null component not allowed as source!");
        }

        this.source = source;
        this.delay = delay;

        this.timer = new Timer(this.delay, this);

        this.listenerSet = new HashSet<MouseMotionListener>();
    }

    public MouseMotionObserver(Component source) {
        this(source, 10);
    }

    @Override
    public synchronized void actionPerformed(ActionEvent event) {
        Point point = MouseInfo.getPointerInfo()
                .getLocation();

        if (point.equals(this.point)) {
            return;
        } else {
            this.point = point;
        }

        this.fireMouseMotionEvent();
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    public int getDelay() {
        return this.delay;
    }

    public synchronized void addMouseMotionListener(MouseMotionListener listener) {
        this.listenerSet.add(listener);
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener listener) {
        this.listenerSet.remove(listener);
    }

    private synchronized void fireMouseMotionEventForPoint(Point point) {
        if (point == null) {
            point = this.point;
        }

        for (final MouseMotionListener listener : this.listenerSet) {
            final MouseMotionEvent event = new MouseMotionEvent(this.source, point);

            listener.mouseMoved(event);
        }
    }

    private synchronized void fireMouseMotionEvent() {
        this.fireMouseMotionEventForPoint(null);
    }
}

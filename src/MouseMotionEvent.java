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

import java.awt.*;
import java.awt.event.MouseEvent;

public final class MouseMotionEvent extends MouseEvent {

    public MouseMotionEvent(Component source, long when, int x, int y) {
        super(source, MouseEvent.MOUSE_MOVED, when, 0, x, y, 0, false);
    }

    public MouseMotionEvent(Component source, int x, int y) {
        this(source, System.currentTimeMillis(), x, y);
    }

    public MouseMotionEvent(Component source, long when, Point point) {
        this(source, when, point.x, point.y);
    }

    public MouseMotionEvent(Component source, Point point) {
        this(source, System.currentTimeMillis(), point);
    }
}

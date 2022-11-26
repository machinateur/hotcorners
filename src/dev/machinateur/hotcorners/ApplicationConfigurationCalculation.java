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
import java.awt.event.KeyEvent;

public class ApplicationConfigurationCalculation extends ApplicationConfiguration {

    protected static final int CALCULATION_THRESHOLD
            = 2;

    /**
     * Set the configuration based on primary screen size or use the defaults. See {@link Toolkit#getScreenSize()} for
     * more information.
     *
     * @param runCalculation Werther to run the calculation based on screen size or not.
     */
    public ApplicationConfigurationCalculation(boolean runCalculation) {
        super();

        if (runCalculation) {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

            this.runCalculation(screen);
        }
    }

    public ApplicationConfigurationCalculation() {
        this(true);
    }

    protected void runCalculation(int width, int height) {
        int threshold = ApplicationConfigurationCalculation.CALCULATION_THRESHOLD;

        // - Top left corner.
        this.put(0, 0, threshold, threshold, new int[]{
                KeyEvent.VK_CONTROL,
                KeyEvent.VK_ALT,
                KeyEvent.VK_TAB,
        });
        // - Top right corner.
        this.put(width - threshold, 0, width, threshold, new int[]{
                KeyEvent.VK_WINDOWS,
                KeyEvent.VK_A,
        });
        // - Bottom left corner.
        this.put(0, height - threshold, threshold, height, new int[]{
                KeyEvent.VK_WINDOWS,
                KeyEvent.VK_TAB,
        });
        // - Top right corner.
        this.put(width - threshold, height - threshold, width, height, new int[]{
                KeyEvent.VK_WINDOWS,
                KeyEvent.VK_D,
        });
    }

    protected void runCalculation(Dimension dimension) {
        this.runCalculation(dimension.width, dimension.height);
    }
}

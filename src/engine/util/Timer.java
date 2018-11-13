/******************************************************************************
 * Copyright 2018 Andrew James McGuiness
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to 
 * deal in the Software without restriction, including without limitation the 
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 *****************************************************************************/
package engine.util;

public class Timer {
    private float deltaTime = 0.0f;
    private long timeNow;
    private long timePrev;

    private boolean printFPS = false;
    private int lastFPS = 0;
    private int frames = 0;
    private float elapsed = 0.0f;


    public Timer() {
        timeNow = timePrev = System.nanoTime();
        ;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public float update() {
        timeNow = System.nanoTime();
        deltaTime = (timeNow - timePrev) / 1_000_000_000.0f;
        timePrev = timeNow;

        if( printFPS ) {
            frames++;
            elapsed += deltaTime;
            if( elapsed >= 1.0f ) {
                elapsed -= 1.0f;
                lastFPS = frames;
                frames = 0;
                System.out.println( "FPS: " + lastFPS );
            }
        }

        return deltaTime;
    }

    public void printFPS( boolean enabled ) {
        printFPS = enabled;
    }
}

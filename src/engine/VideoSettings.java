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
package engine;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class VideoSettings {
    public String title;
    public int width;
    public int height;
    public boolean vsync;

    public static VideoSettings LoadFromFile() {
        VideoSettings vs = new VideoSettings();

        try {
            FileReader settingsFile = new FileReader( "assets/settings.json" );
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse( settingsFile );
            JsonObject jsonObject = root.getAsJsonObject().get( "videoSettings" ).getAsJsonObject();
            vs.title = jsonObject.get( "title" ).getAsString();
            vs.width = jsonObject.get( "width" ).getAsInt();
            vs.height = jsonObject.get( "height" ).getAsInt();
            vs.vsync = jsonObject.get( "vsync" ).getAsInt() > 0;
        }
        catch( Exception e ) {
            vs.title = "Default Settings";
            vs.width = 800;
            vs.height = 600;
            vs.vsync = true;
        }

        return vs;
    }

    public static void WriteToFile( VideoSettings settings ) {
        // STUB
    }
}

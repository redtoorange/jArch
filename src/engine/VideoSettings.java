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

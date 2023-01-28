package advisor.Controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Commands {
    private static final String NEW = "/v1/browse/new-releases";
    private static final String FEATURED = "/v1/browse/featured-playlists";
    private static final String CATEGORIES = "/v1/browse/categories";
    private static String categorieId = "";
    private static String PLAYLIST = "";

    public static List<String> newCommand() throws IOException, InterruptedException {
        JsonObject newAlbums = JSONHandler.getJO(NEW);
        if (JSONHandler.checkError(newAlbums)) return null;
        newAlbums = newAlbums.getAsJsonObject("albums");
        List<String> albums = new ArrayList<>();
        for (JsonElement album : newAlbums.getAsJsonArray("items")) {
            JsonObject albumObj = album.getAsJsonObject();
            String name = albumObj.get("name").getAsString();
            String albumUrl = albumObj.getAsJsonObject("external_urls").get("spotify").getAsString();

            List<String> artists = new ArrayList<>();
            for (JsonElement artist : albumObj.getAsJsonArray("artists")) {
                artists.add(artist.getAsJsonObject().get("name").getAsString());
            }
            albums.add(name + "\n" + artists + "\n" + albumUrl + "\n");
        }
        return albums;

    }

    public static List<String> featuredCommand() throws IOException, InterruptedException {
        JsonObject featuredPlaylists = JSONHandler.getJO(FEATURED);
        if (JSONHandler.checkError(featuredPlaylists)) return null;
        featuredPlaylists = featuredPlaylists.getAsJsonObject("playlists");
        List<String> playlists = new ArrayList<>();
        for (JsonElement playlist: featuredPlaylists.getAsJsonArray("items")) {
            JsonObject playlistObj = playlist.getAsJsonObject();
            String name = playlistObj.get("name").getAsString();
            String playlistUrl = playlistObj.getAsJsonObject("external_urls").get("spotify").getAsString();
            playlists.add(name + "\n" + playlistUrl + "\n");
        }

        return playlists;

    }

    public static List<String> categoriesCommand() throws IOException, InterruptedException {
        JsonObject playlistCategories = JSONHandler.getJO(CATEGORIES);
        if (JSONHandler.checkError(playlistCategories)) return null;
        playlistCategories = playlistCategories.getAsJsonObject("categories");
        List<String> categories = new ArrayList<>();
        for (JsonElement category: playlistCategories.getAsJsonArray("items")) {
            JsonObject categoryObj = category.getAsJsonObject();
            String name = categoryObj.get("name").getAsString();

            categories.add(name);
        }

        return categories;

    }

    public static List<String> playlistCommand(String categoryInput) throws IOException, InterruptedException {
        JsonObject categories = JSONHandler.getJO(CATEGORIES).getAsJsonObject("categories");
        for (JsonElement category : categories.getAsJsonArray("items")) {
            JsonObject categoryObj = category.getAsJsonObject();
            if (categoryObj.get("name").getAsString().replaceAll("\\s","").equals(categoryInput.replaceAll("\\s", ""))) {
                categorieId = categoryObj.get("id").getAsString();

            }
        }
        PLAYLIST = "/v1/browse/categories/"+ categorieId +"/playlists";
        JsonObject playlistCategory = JSONHandler.getJO(PLAYLIST);
        if (JSONHandler.checkError(playlistCategory)) return null;
        playlistCategory = playlistCategory.getAsJsonObject("playlists");
        List<String> playlists = new ArrayList<>();
        for (JsonElement playlist : playlistCategory.getAsJsonArray("items")) {
            JsonObject playlistObj = playlist.getAsJsonObject();
            String name = playlistObj.get("name").getAsString();
            String playlistUrl = playlistObj.get("external_urls").getAsJsonObject().get("spotify").getAsString();
            playlists.add(name + "\n" + playlistUrl + "\n");
        }
        categorieId = "";

        return playlists;

    }
}

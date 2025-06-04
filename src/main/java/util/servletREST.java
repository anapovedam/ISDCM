package util;

import model.Video; // Assuming your Video class is in the model package
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.Duration; // Using java.time.Duration for HttpClient timeout

/**
 * ServletREST class to interact with an external Video REST API.
 * This class handles HTTP requests to perform CRUD operations on videos.
 */
public class servletREST {

    private static final String API_BASE_URL = "http://localhost:8080/ISDCM-21_REST_Service_v2/res";

    private static final String VIDEOS_PATH = "/videos";
    private static final String INCR_VIEWS_PATH = "/views"; // Path para incrementar vistas
    private static final String FILTERED_PATH = "/filtered"; // Path para videos filtrados

    private final HttpClient httpClient;
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sqlTimeFormat = new SimpleDateFormat("HH:mm:ss");
    /**
     * Constructor to initialize the HttpClient.
     */
    public servletREST() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10)) // Timeout for establishing a connection
                .build();
    }

    private HttpRequest.Builder authorizedRequestBuilder(String jwtToken) {
        return HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + jwtToken)
                .header("Accept", "application/json");
    }
    
    public String login(String username, String password) throws IOException, InterruptedException {
        // Build form body: username=<…>&password=<…>
        String formData = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
                        + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/videos/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        int status = resp.statusCode();
        if (status == 200) {
            try {
                JSONObject json = new JSONObject(resp.body());
                return json.getString("token");
            } catch (JSONException ex) {
                // Malformed JSON response
                return null;
            }
        } else {
            // 401 Unauthorized or other → login failed
            System.out.println("El status del login es " + status);
            return null;
        }
    }
    /**
     * Converts a Video object to its JSON string representation.
     *
     * @param video The Video object to convert.
     * @return A JSONObject representing the video.
     */
    private JSONObject videoToJson(Video video) {
        JSONObject jsonVideo = new JSONObject();
        try {
            // Only include ID if it's valid (e.g., for updates, not for creation if API assigns ID)
            if (video.getId() > 0) {
                jsonVideo.put("id", video.getId());
            }
            jsonVideo.put("title", video.getTitle());
            jsonVideo.put("author", video.getAuthor());
            // Assuming author_id is handled by the API or derived from 'author'
            // If author_id needs to be sent explicitly:
            // jsonVideo.put("author_id", video.getAuthorID()); 
            jsonVideo.put("creation_date", video.getCreationDate() != null ? sqlDateFormat.format(video.getCreationDate()) : JSONObject.NULL);
            jsonVideo.put("duration", video.getDuration() != null ? sqlTimeFormat.format(video.getDuration()) : JSONObject.NULL);
            jsonVideo.put("views", video.getViews());
            jsonVideo.put("description", video.getDescription() != null ? video.getDescription() : "");
            jsonVideo.put("format", video.getFormat());
            jsonVideo.put("url", video.getUrl());
        } catch (JSONException e) {
            System.err.println("Error creating JSON from Video object: " + e.getMessage());
            // Potentially throw a custom exception or return null
        }
        return jsonVideo;
    }

    /**
     * Converts a JSONObject to a Video object.
     *
     * @param jsonVideo The JSONObject to convert.
     * @return A Video object.
     */
    private Video jsonToVideo(JSONObject jsonVideo) {
        if (jsonVideo == null) {
            return null;
        }
        Video video = new Video();
        try {
            video.setId(jsonVideo.optInt("id", -1));
            video.setTitle(jsonVideo.optString("title"));
            video.setAuthor(jsonVideo.optString("author"));
            // If author_id is present in the response and needed:
            // video.setAuthorID(jsonVideo.optInt("author_id", -1));

            String creationDateStr = jsonVideo.optString("creation_date");
            if (creationDateStr != null && !creationDateStr.isEmpty() && !creationDateStr.equals("null")) {
                try {
                    java.util.Date parsedDate = sqlDateFormat.parse(creationDateStr);
                    video.setCreationDate(new Date(parsedDate.getTime()));
                } catch (java.text.ParseException e) {
                    System.err.println("Error parsing creation_date: " + e.getMessage());
                    video.setCreationDate(null);
                }
            } else {
                video.setCreationDate(null);
            }

            String durationStr = jsonVideo.optString("duration");
             if (durationStr != null && !durationStr.isEmpty() && !durationStr.equals("null")) {
                try {
                    // Assuming duration is in HH:mm:ss format from API
                    // java.sql.Time can parse this directly if the format matches
                    video.setDuration(Time.valueOf(durationStr));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing duration: " + e.getMessage());
                    video.setDuration(null);
                }
            } else {
                video.setDuration(null);
            }

            video.setViews(jsonVideo.optInt("views"));
            video.setDescripcton(jsonVideo.optString("description")); // Note: Typo in Video.java setter (setDescripcton)
            video.setFormat(jsonVideo.optString("format"));
            video.setUrl(jsonVideo.optString("url"));
        } catch (JSONException e) {
            System.err.println("Error creating Video from JSON object: " + e.getMessage());
            return null; // Or throw a custom exception
        }
        return video;
    }

    /**
     * Fetches all videos from the API.
     * @return A list of Video objects.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the HTTP request is interrupted.
     * @throws JSONException If there's an error parsing the JSON response.
     */
    public List<Video> getAllVideos() throws IOException, InterruptedException, JSONException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + VIDEOS_PATH))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<Video> videos = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                videos.add(jsonToVideo(jsonArray.getJSONObject(i)));
            }
            return videos;
        } else {
            throw new IOException("Failed to get videos: HTTP status code " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Fetches filtered videos from the API.
     *
     * @param filters A map of filter parameters (e.g., "title", "author").
     * @return A list of filtered Video objects.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the request is interrupted.
     * @throws JSONException If JSON parsing fails.
     */
    public List<Video> getVideoFiltered(Map<String, String> filters) throws IOException, InterruptedException, JSONException {
        StringBuilder queryParams = new StringBuilder("?");
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (queryParams.length() > 1) {
                    queryParams.append("&");
                }
                queryParams.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                queryParams.append("=");
                queryParams.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
        }

        String apiUrl = String.format("%s%s%s%s",
            API_BASE_URL,
            VIDEOS_PATH,
            FILTERED_PATH,
            queryParams.length() > 1 ? queryParams.toString() : ""
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray jsonArray = new JSONArray(response.body());
            List<Video> videos = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                videos.add(jsonToVideo(jsonArray.getJSONObject(i)));
            }
            return videos;
        } else {
            throw new IOException("Failed to get filtered videos: HTTP status code " + response.statusCode() + " - " + response.body());
        }
    }
    
    /**
     * Fetches a single video by its ID from the API.
     *
     * @param videoId The ID of the video to fetch.
     * @return The Video object if found, otherwise null.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the request is interrupted.
     * @throws JSONException If JSON parsing fails.
     */
    public Video getVideoById(int videoId) throws IOException, InterruptedException, JSONException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + VIDEOS_PATH + "/" + videoId))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return jsonToVideo(new JSONObject(response.body()));
        } else if (response.statusCode() == 404) {
            return null;
        } else {
            throw new IOException("Failed to get video by ID " + videoId + ": HTTP status code " + response.statusCode() + " - " + response.body());
        }
    }


    /**
     * Creates a new video via the API.
     *
     * @param video The Video object to create. The ID field is typically ignored or should be unset,
     * as the API usually assigns it.
     * @return The created Video object, possibly with an ID assigned by the API.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the request is interrupted.
     * @throws JSONException If JSON processing fails.
     */
    public Video createVideo(Video video) throws IOException, InterruptedException, JSONException {
        JSONObject jsonVideo = videoToJson(video);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + VIDEOS_PATH))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonVideo.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) { 
            return jsonToVideo(new JSONObject(response.body()));
        } else {
            throw new IOException("Failed to create video: HTTP status code " + response.statusCode() + " - " + response.body());
        }
    }


    /**
     * Deletes a video via the API.
     *
     * @param videoId The ID of the video to delete.
     * @return true if deletion was successful (e.g., API returns 200 OK or 204 No Content), false otherwise.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the request is interrupted.
     */
    public boolean deleteVideo(int videoId) throws IOException, InterruptedException {
        if (videoId <= 0) {
            throw new IllegalArgumentException("Video ID must be valid for deletion.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + VIDEOS_PATH + "/" + videoId))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 204) {
            return true;
        } else if (response.statusCode() == 404) {
            System.err.println("Video not found for deletion (ID: " + videoId + ")");
            return false; 
        }
        else {
            System.err.println("Failed to delete video " + videoId + ": HTTP status code " + response.statusCode() + " - " + response.body());
            return false;
        }
    }

    public void incrementViews(int videoId) throws IOException, InterruptedException {
        if (videoId <= 0) {
            throw new IllegalArgumentException("El ID del video debe ser un valor positivo.");
        }

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("videoId", videoId); 

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + VIDEOS_PATH + "/" + videoId + INCR_VIEWS_PATH))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonInput.toString())) 
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falló el incremento de vistas para el video con ID " + videoId +
                    ". Código de estado: " + response.statusCode() +
                    ", Respuesta del servidor: " + response.body());
        }
    }
}

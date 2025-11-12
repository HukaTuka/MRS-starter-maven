package dk.easv.mrs.DAL;
//Package imports
import dk.easv.mrs.BE.Movie;
//Java imports
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;

public class MovieDAO_File implements IMovieDataAccess {

    private static final String pathToFile = "data/movie_titles.txt";

    @Override
    public List<Movie> getAllMovies() throws IOException {

        // Read all lines from file
        List<String> lines = Files.readAllLines(Path.of(pathToFile));
        List<Movie> movies = new ArrayList<>();

        // Parse each line as movie
        for (String line: lines) {
            String[] separatedLine = line.split(",");

            int id = Integer.parseInt(separatedLine[0]);
            int year = Integer.parseInt(separatedLine[1]);
            String title = separatedLine[2];
            if(separatedLine.length > 3)
            {
                for(int i = 3; i < separatedLine.length; i++)
                {
                    title += "," + separatedLine[i];
                }
            }
            Movie movie = new Movie(id, year, title);
            movies.add(movie);
        }

        return movies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        List<String> movies = Files.readAllLines(Path.of(pathToFile));

        int nextId = 1; // Default ID if file is empty

        if (movies.size() > 0) {
            // Get the last movie's ID and increment
            String[] separatedLine = movies.get(movies.size() - 1).split(",");
            nextId = Integer.parseInt(separatedLine[0]) + 1;
        }

        // Create the new movie line and write to file
        String newMovieLine = nextId + "," + newMovie.getYear() + "," + newMovie.getTitle();
        Files.write(Path.of(pathToFile), (newMovieLine + "\r\n").getBytes(), APPEND);

        // Return a new Movie object with the correct ID
        return new Movie(nextId, newMovie.getYear(), newMovie.getTitle());
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        // Read all lines from file
        List<String> lines = Files.readAllLines(Path.of(pathToFile));
        List<String> updatedLines = new ArrayList<>();

        // Keep all lines except the one matching the movie ID
        for (String line : lines) {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);

            if (id != movie.getId()) {
                updatedLines.add(line);
            }
        }

        // Write the updated list back to the file
        Files.write(Path.of(pathToFile), String.join("\r\n", updatedLines).getBytes());

        // Add a newline at the end if there are any movies left
        if (!updatedLines.isEmpty()) {
            Files.write(Path.of(pathToFile), "\r\n".getBytes(), APPEND);
        }
    }
}
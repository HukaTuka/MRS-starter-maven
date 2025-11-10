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

    //The @Override annotation is not required, but is recommended for readability
    // and to force the compiler to check and generate error msg. if needed etc.
    //@Override
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
    public Movie createMovie(String title, int year) throws Exception {
        List<String> movies = Files.readAllLines(Path.of(pathToFile));

        if (movies.size() > 0) {
            // get next id
            String[] separatedLine = movies.get(movies.size() - 1).split(",");
            int nextId = Integer.parseInt(separatedLine[0]) + 1;
            String newMovieLine = nextId + "," + year + "," + title;
            Files.write(Path.of(pathToFile), (newMovieLine + "\r\n").getBytes(), APPEND);

            return new Movie(nextId, year, title);
        }
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
    }
}
package dk.easv.mrs.BLL;
//Package imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.util.MovieSearcher;
import dk.easv.mrs.DAL.IMovieDataAccess;
import dk.easv.mrs.DAL.db.MovieDAO_DB;
//import dk.easv.mrs.DAL.MovieDAO_File;
//Java imports
import java.io.IOException;
import java.util.List;

public class MovieManager {

    private MovieSearcher movieSearcher = new MovieSearcher();
    private IMovieDataAccess movieDAO;

    public MovieManager() throws IOException {
        //movieDAO = new MovieDAO_File();
        movieDAO = new MovieDAO_DB();
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    public List<Movie> searchMovies(String query) throws Exception {
        List<Movie> allMovies = getAllMovies();
        List<Movie> searchResult = movieSearcher.search(allMovies, query);
        return searchResult;
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        return movieDAO.createMovie(newMovie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieDAO.deleteMovie(movie);
    }

    public void updateMovie(Movie movie) throws Exception {
        movieDAO.updateMovie(movie);
    }
}
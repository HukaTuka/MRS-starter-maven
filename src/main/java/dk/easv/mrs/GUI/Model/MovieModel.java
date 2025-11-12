package dk.easv.mrs.GUI.Model;
//Package imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.MovieManager;
//JavaFX imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//Java imports
import java.util.List;

public class MovieModel {

    private ObservableList<Movie> moviesToBeViewed;
    private MovieManager movieManager;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movie> getObservableMovies() {
        return moviesToBeViewed;
    }

    public void searchMovie(String query) throws Exception {
        List<Movie> searchResults = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResults);
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        return movieManager.createMovie(newMovie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        movieManager.deleteMovie(movie);
        moviesToBeViewed.remove(movie);
    }

    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie);

        // Update the observable list to refresh the UI
        int index = -1;
        for (int i = 0; i < moviesToBeViewed.size(); i++) {
            if (moviesToBeViewed.get(i).getId() == movie.getId()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            moviesToBeViewed.set(index, movie);
        }
    }
}
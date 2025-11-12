package dk.easv.mrs.GUI.Controller;
//Package imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
//JavaFX imports
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateNewViewController {
    public TextField txtNewMovieName;
    public TextField txtNewMovieYear;
    private MovieModel movieModel;

    // Method to set the model from the parent controller
    public void setModel(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    public void onBtnClickCreate(ActionEvent actionEvent) {
        try {
            String title = txtNewMovieName.getText();
            int year = Integer.parseInt(txtNewMovieYear.getText());

            Movie newMovie = new Movie(-1, year, title);
            Movie createdMovie = movieModel.createMovie(newMovie);

            // Add the created movie to the observable list so it shows up immediately
            movieModel.getObservableMovies().add(createdMovie);

            // Close the window after creating the movie
            Stage stage = (Stage) txtNewMovieName.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            displayError(new Exception("Please enter a valid year"));
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }
}
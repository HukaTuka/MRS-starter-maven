package dk.easv.mrs.GUI.Controller;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateMovieViewController {
    public TextField txtUpdateMovieName;
    public TextField txtUpdateMovieYear;

    private MovieModel movieModel;
    private Movie movieToUpdate;

    // Method to set the model from the parent controller
    public void setModel(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    // Method to set the movie to update and pre-fill the fields
    public void setMovie(Movie movie) {
        this.movieToUpdate = movie;

        // Pre-fill the text fields with current movie data
        if (movie != null) {
            txtUpdateMovieName.setText(movie.getTitle());
            txtUpdateMovieYear.setText(String.valueOf(movie.getYear()));
        }
    }

    public void onBtnClickUpdate(ActionEvent actionEvent) {
        try {
            if (movieToUpdate == null) {
                displayError(new Exception("No movie selected for update"));
                return;
            }

            String newTitle = txtUpdateMovieName.getText();
            int newYear = Integer.parseInt(txtUpdateMovieYear.getText());

            // Update the movie object with new values
            movieToUpdate.setTitle(newTitle);
            movieToUpdate.setYear(newYear);

            // Save the updated movie
            movieModel.updateMovie(movieToUpdate);

            // Show success message
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText("Movie updated");
            success.setContentText("The movie has been successfully updated.");
            success.showAndWait();

            // Close the window
            Stage stage = (Stage) txtUpdateMovieName.getScene().getWindow();
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
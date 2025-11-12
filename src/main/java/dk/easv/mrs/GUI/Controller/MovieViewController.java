package dk.easv.mrs.GUI.Controller;
//Package imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;
//JavaFX imports
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//Java imports
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {

    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;
    private MovieModel movieModel;

    public MovieViewController()  {
        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstMovies.setItems(movieModel.getObservableMovies());

        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    public void onBtnClickOpenCreate(ActionEvent actionEvent) {
        try {
            // Load the FXML for the new window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CreateNewView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the model
            CreateNewViewController controller = loader.getController();
            controller.setModel(movieModel);

            // Create and show the new stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Create New Movie");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    public void onBtnClickDeleteMovie(ActionEvent actionEvent) {
        try {
            // Get the selected movie from the list
            Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

            // Check if a movie is selected
            if (selectedMovie == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText("No movie selected");
                alert.setContentText("Please select a movie from the list to delete.");
                alert.showAndWait();
                return;
            }

            // Show confirmation dialog
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Movie");
            confirmation.setHeaderText("Delete \"" + selectedMovie.getTitle() + "\"?");
            confirmation.setContentText("Are you sure you want to delete this movie? This action cannot be undone.");

            Optional<ButtonType> result = confirmation.showAndWait();

            // If user confirms, delete the movie
            if (result.isPresent() && result.get() == ButtonType.OK) {
                movieModel.deleteMovie(selectedMovie);

                // Show success message
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText("Movie deleted");
                success.setContentText("The movie has been successfully deleted.");
                success.showAndWait();
            }

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }
}
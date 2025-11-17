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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
//Java imports
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {

    public TextField txtMovieSearch;
    public ListView<Movie> lstMovies;
    public MenuButton btnOptions;
    public TableView<Movie> tblViewMovies;
    public TableColumn<Movie, String> tblName;
    public TableColumn<Movie, Integer> tblYear;
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
        tblName.setCellValueFactory(new PropertyValueFactory<>("Title"));
        tblYear.setCellValueFactory(new PropertyValueFactory<>("Year"));

        lstMovies.setItems(movieModel.getObservableMovies());
        tblViewMovies.setItems(movieModel.getObservableMovies());

        // Hides the buttons when no movies are selected.
        btnOptions.visibleProperty().bind(
                tblViewMovies.getSelectionModel().selectedItemProperty().isNotNull()
        );


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

    /**
     * Open the menu to create a new movie
     * @param actionEvent on btn click
     */
    public void onBtnClickOpenCreate(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CreateNewView.fxml"));
            Parent root = loader.load();

            CreateNewViewController controller = loader.getController();
            controller.setModel(movieModel);

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

    /**
     * prompts confirmation to delete a selected movie, deletes on confirmation.
     * @param actionEvent event triggered by clicking delete button
     */
    public void onBtnClickDeleteMovie(ActionEvent actionEvent) {
        try {
            Movie selectedMovie = tblViewMovies.getSelectionModel().getSelectedItem();

            if (selectedMovie == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText("No movie selected");
                alert.setContentText("Please select a movie from the list to delete.");
                alert.showAndWait();
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Movie");
            confirmation.setHeaderText("Delete \"" + selectedMovie.getTitle() + "\"?");
            confirmation.setContentText("Are you sure you want to delete this movie? This action cannot be undone.");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                movieModel.deleteMovie(selectedMovie);

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

    public void onBtnClickOpenUpdate(ActionEvent actionEvent) {
        try {
            // Get the selected movie
            Movie selectedMovie = tblViewMovies.getSelectionModel().getSelectedItem();


            if (selectedMovie == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText("No movie selected");
                alert.setContentText("Please select a movie from the list to update.");
                alert.showAndWait();
                return;
            }

            // Load the FXML for the update window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdateMovieView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the model and selected movie
            UpdateMovieViewController controller = loader.getController();
            controller.setModel(movieModel);
            controller.setMovie(selectedMovie);

            // Create and show the new stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Update Movie");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }
}
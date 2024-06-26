package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.interfaces.ClickEventHandler;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;

// Zelle zur Darstellung eines Films in der Watchlist
public class WatchlistMovieCell extends ListCell<WatchlistMovieEntity> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final JFXButton removeBtn = new JFXButton("Remove from Watchlist");
    private final JFXButton detailBtn = new JFXButton("Show Details");
    private final HBox buttons = new HBox(removeBtn, detailBtn);
    private final VBox layout = new VBox(title, detail, genre, buttons);
    private boolean collapsedDetails = true;

    // Konstruktor für die WatchlistMovieCell
    public WatchlistMovieCell(ClickEventHandler removeFromWatchlist) {
        super();

        // Styling
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        removeBtn.setStyle("-fx-background-color: #f5c518;");
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        // Layout-Einstellungen
        title.setFont(title.getFont().font(20));
        detail.setWrapText(true);
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.TOP_RIGHT);

        // Ereignishandler für den Detail-Button
        detailBtn.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                detailBtn.setText("Hide Details");
            } else {
                layout.getChildren().remove(4);
                collapsedDetails = true;
                detailBtn.setText("Show Details");
            }
            setGraphic(layout);
        });

        // Ereignishandler für den Remove-Button
        removeBtn.setOnMouseClicked(mouseEvent -> {
            try {
                removeFromWatchlist.onClick(getItem());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Überschriebene Methode zum Aktualisieren der Zelleninhalte
    @Override
    protected void updateItem(WatchlistMovieEntity movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            genre.setText(String.join(",", getItem().getGenres()));

            setGraphic(layout);
        }
    }

    // Methode zur Erstellung der Detailansicht
    private VBox getDetails() {
        VBox details = new VBox();
        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label length = new Label("Length: " + getItem().getLengthInMinutes() + " minutes");
        Label rating = new Label("Rating: " + getItem().getRating() + "/10");

        releaseYear.getStyleClass().add("text-white");
        length.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        details.getChildren().add(releaseYear);
        details.getChildren().add(rating);
        details.getChildren().add(length);

        return details;
    }
}

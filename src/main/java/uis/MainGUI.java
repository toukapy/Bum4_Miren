package uis;

import controllers.MatchesController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class MainGUI {

    private Window managerWin;
    private FXMLLoader loader;
    private Stage stage;
    private Scene scene;

    /**
     * Class of the window
     */
    class Window{
        Parent ui;
        MatchesController c;
    }

    /**
     * Contructor of the class
     */
    public MainGUI() {
        Platform.startup(() -> {
            try {
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method that loads the fxml file
     * @param fxmlfile - The fxml file of the window
     * @return Window - The window that goes with the fxml file
     * @throws IOException
     */
    private Window load(String fxmlfile) throws IOException{
        Window window = new Window();
        loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile));
        loader.setControllerFactory(controllerClass -> {
            if(controllerClass == MatchesController.class) {
                return new MatchesController();
            }else{
                // default behavior for controllerFactory:
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        });

        window.ui = loader.load();
        window.c = loader.getController();

        return window;
    }

    /**
     * Method that initializes the windows with its fxml and constructor
     * @param stage - The stage in which the fxml has tp be loaded
     * @throws IOException
     */
    public void init(Stage stage) throws IOException{
        this.stage = stage;

        managerWin = load("/uis/matches.fxml");

        showMatches();
    }

    /**
     * Method that shows the manager window
     */
    private void showMatches() {
        setupScene(managerWin.ui, "Matches", 950, 555);
    }

    /**
     * Method that sets up the scene specified
     * @param ui Parent - The ui to be set in the scene
     * @param title String - The header of the window
     * @param width int - The width of the window
     * @param height int - The height of the window
     */
    private void setupScene(Parent ui, String title, int width, int height) {
        if (scene == null){
            scene = new Scene(ui, width, height);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
        }
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(title);
        scene.setRoot(ui);
        stage.show();;
    }


}

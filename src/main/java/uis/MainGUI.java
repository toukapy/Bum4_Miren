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
     *
     * @param fxmlfile
     * @return
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
     *
     * @param stage
     * @throws IOException
     */
    public void init(Stage stage) throws IOException{
        this.stage = stage;

        managerWin = load("/uis/matches.fxml");

        showMatches();
    }

    /**
     *
     */
    private void showMatches() {
        setupScene(managerWin.ui, "Matches", 950, 555);
    }

    /**
     *
     * @param ui
     * @param title
     * @param width
     * @param height
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

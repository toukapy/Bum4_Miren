package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dataAccess.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import javafx.embed.swing.SwingFXUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MatchesController {
    @FXML
    private TextField compIdField;

    @FXML
    private TextField compNameField;

    @FXML
    private TextField areaIdField;

    @FXML
    private TextField areaNameField;

    @FXML
    private TextField areaCountryField;

    @FXML
    private Button previousBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private ImageView imagePng = new ImageView();

    private int index = 0;
    private Manager manager = new Manager();
    private String body = manager.request("competitions");

    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
    List<Manager.Competition> competitions;

    /**
     *
     */
    public void initialize(){
        initializeCompetitions();
        initializeTextFields();
    }

    /**
     *
     */
    public void initializeCompetitions(){
        if(jsonObject != null){
            Type competitionListType = new TypeToken<ArrayList<Manager.Competition>>(){}.getType();
            competitions = gson.fromJson((jsonObject.get("competitions")), competitionListType);
        }else{
            jsonObject = gson.fromJson(body,JsonObject.class);
            Type competitionListType = new TypeToken<ArrayList<Manager.Competition>>(){}.getType();
            competitions = gson.fromJson((jsonObject.get("competitions")), competitionListType);
        }
    }

    /**
     *
     */
    public void initializeTextFields(){
        compIdField.setText(String.valueOf(competitions.get(0).id));
        compNameField.setText(competitions.get(0).name);
        areaIdField.setText(String.valueOf(competitions.get(0).area.id));
        areaNameField.setText(competitions.get(0).area.name);
        areaCountryField.setText(competitions.get(0).code);
        setImage();
    }

    /**
     *
     */
    private void setImage() {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        String emblem = competitions.get(index).getEmblemUrl();
        if (emblem != null && emblem.endsWith("svg")) {
            try (InputStream file = new URL(emblem).openStream()) {
                TranscoderInput transIn = new TranscoderInput(file);
                try {
                    transcoder.transcode(transIn, null);
                    Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                    imagePng.setImage(img);
                } catch (TranscoderException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }else if (emblem != null && (emblem.endsWith("png") || emblem.endsWith("jpg"))) {
            imagePng.setImage(new Image(emblem));
        }else
            imagePng.setImage(null);
    }

    /**
     *
     */
    @FXML
    void onClickNext(){
        if(index < competitions.size()-1){
            index++;
        }else{
            index = 0;
        }
        compIdField.setText(String.valueOf(competitions.get(index).id));
        compNameField.setText(competitions.get(index).name);
        areaIdField.setText(String.valueOf(competitions.get(index).area.id));
        areaNameField.setText(competitions.get(index).area.name);
        areaCountryField.setText(competitions.get(index).code);
        setImage();
    }

    /**
     *
     */
    @FXML
    void onClickPrevious(){
        if(index > 0){
            index--;
        }else if(index == 0){
            index = competitions.size()-1;
        }
        compIdField.setText(String.valueOf(competitions.get(index).id));
        compNameField.setText(competitions.get(index).name);
        areaIdField.setText(String.valueOf(competitions.get(index).area.id));
        areaNameField.setText(competitions.get(index).area.name);
        areaCountryField.setText(competitions.get(index).code);
        setImage();
    }

}

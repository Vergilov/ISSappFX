package issfx;

import issfx.datamodel.ISSCheck;
import issfx.datamodel.JSONCreator;
import issfx.datamodel.JSONDataOutput;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {
    @FXML
    private TextField distanceFromStartTextField;
    @FXML
    private TextField currentLongitudeTextField;
    @FXML
    private TextField currentSpeedTextField;
    @FXML
    private TextField currentLatitudeTextField;
    @FXML
    private TextArea resultTextArea;
    @FXML
    private Label runningLabel;

    private ISSCheck issCheck = new ISSCheck();

    private Thread thread = newThread();
    private volatile boolean stopThread = false;
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    @FXML
    public void initialize() {
        resultTextArea.clear();
        resultTextArea.setEditable(false);
        thread.setDaemon(true);
        thread.start();

    }


    public void addDataToArrayListButton() throws Exception {
        int count = 0;

        issCheck.addJSONtoArray();
        resultTextArea.clear();
        for (String str : issCheck.getArrayList()) {
            resultTextArea.appendText(count + ". " + str + "\n");
            count++;
        }
        resultTextArea.setEditable(false);
    }

    public void stopThreadButton() {
        if (!stopThread) {
            stopThread = true;
            runningLabel.setText("Stop");
        }
    }

    public void startThreadButton() {
        if (stopThread) {
            stopThread = false;
            thread = newThread();
            thread.start();
            runningLabel.setText("Start");
        }
    }

    private Thread newThread() {
        Thread t = new Thread(() -> {
            while (!stopThread) {
                Platform.runLater(() -> {
                    try {
                        resultTextArea.clear();
                        issCheck.addJSONtoArray();
                        currentLatitudeTextField.setText(JSONDataOutput.getLatitude(JSONCreator.buildJSON()).toString());
                        currentLongitudeTextField.setText(JSONDataOutput.getLongitude(JSONCreator.buildJSON()).toString());
                        currentSpeedTextField.setText(issCheck.calculateSpeedFromLastTwoPoints());
                        distanceFromStartTextField.setText(issCheck.calculateOverallDistance());
                        int count = 0;
                        for (String str : issCheck.getArrayList()) {
                            resultTextArea.appendText(count + ". " + str + "\n");
                            count++;
                        }
                        resultTextArea.setEditable(false);
                    } catch (Exception ex) {
                        LOGGER.log(Level.SEVERE, "Error: ", ex);
                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    LOGGER.log(Level.WARNING, "Caution! : ", ex);
                }
            }
        });
        return t;
    }

    @FXML
    private void closeButtonAction() {
        Platform.exit();
    }

    public void saveButtonAction() throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();
        File file = fileChooser.showSaveDialog(stage);
        FileWriter fileWriter = new FileWriter(file);
        try {
            fileWriter.write(resultTextArea.getText());
        } catch (IOException ex) {
            LOGGER.log(Level.FINE, "Message: ", ex);
        } finally {
            fileWriter.close();
        }
    }
}

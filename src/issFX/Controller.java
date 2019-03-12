package issFX;

import issFX.datamodel.ISSCheck;
import issFX.datamodel.JSONCreator;
import issFX.datamodel.JSONDataOutput;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField distanceFromStartTextField;
    @FXML
    private TextField currentLongtitudeTextField;
    @FXML
    private TextField currentSpeedTextField;
    @FXML
    private TextField currentLatitudeTextField;
    @FXML
    private TextArea resultTextArea;
    @FXML
    private Label statusLayout;

    private ISSCheck issCheck = new ISSCheck();
    private boolean running = true;
    private Thread t = newThread();


    public void initialize() {
        resultTextArea.clear();
        resultTextArea.setEditable(false);
        statusLayout.setText("Online");
        t.setDaemon(true);
        if (running) {
            t.start();
        } else {
            try {
                t.wait();
            } catch (InterruptedException e) {
            }
        }
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
        t.interrupt();
    }

    public void startThreadButton() {
        if (!t.isAlive()) {
            t = newThread();
            t.start();
        }
    }

    public Thread newThread() {
        Thread t = new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    try {
                        resultTextArea.clear();
                        issCheck.addJSONtoArray();
                        currentLatitudeTextField.setText(JSONDataOutput.getLatitude(JSONCreator.buildJSON()).toString());
                        currentLongtitudeTextField.setText(JSONDataOutput.getLongitude(JSONCreator.buildJSON()).toString());
                        currentSpeedTextField.setText(issCheck.calculateSpeedFromLastTwoPoints());
                        distanceFromStartTextField.setText(issCheck.calculateOverallDistance());
                        int count = 0;
                        for (String str : issCheck.getArrayList()) {
                            resultTextArea.appendText(count + ". " + str + "\n");
                            count++;
                        }
                        resultTextArea.setEditable(false);
                        wait();
                    } catch (Exception e) {

                    }
                });
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    break;
                }
            }
        });
        return t;
    }
}

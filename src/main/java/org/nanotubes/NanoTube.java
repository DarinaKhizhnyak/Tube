package org.nanotubes;

import javafx.application.Application;
import javafx.collections.ObservableList;

import javafx.geometry.HPos;
import javafx.geometry.VPos;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import javafx.stage.Stage;

import org.nanotubes.generation.Generation;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.minimization.Mapping;
import org.nanotubes.minimization.Minimization;

import java.util.Arrays;

/**
 * Класс воспроизводящий программу
 */
public class NanoTube extends Application {
    /**
     * Положение "мыши" по x и y в пикселях
     */
    private double anchorX, anchorY;
    /**
     * Ширина окна в пикселях
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int WIDTH = 800;
    /**
     * Высота окна в пикселях
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final int HEIGHT = 670;
    /**
     * Радиус определяющий резкость вращения камеры
     */
    private final int RADIUS = 100;
    /**
     * Положение камеры по x при открытии окна
     */
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    /**
     * Положение камеры по y при открытии окна
     */
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    /**
     * Метод содержащий элементы сцены
     * @param stage контйнер (окно)
     */
    @Override
    public void start(Stage stage) {

        var buttonEnter = new Button("Enter");
        var buttonEnergyMinimization = new Button("Minimization");
        var buttonEnergyMinimizationStress = new Button("Minimization Stress");
        var buttonDiagram = new Button("Diagram");
        var labelRadius = new Label("Cylinder's radius");
        var labelHeight = new Label("Cylinder's height");
        var labelNumber = new Label("Number Particle");
        var labelStress = new Label("Stress");
        var labelEnergy = new Label("Energy");
        var labelEnergyValue = new Label("0");
        var textFieldRadius = new TextField();
        var textFieldHeight = new TextField();
        var textNumber = new TextField();
        var textStress = new TextField();

        GridPane Top = new GridPane();
        for (int i : new int[]{120, 100, 70, 90, 60, 140, 150, 65}) {
            Top.getColumnConstraints().add(new ColumnConstraints(i));
        }
        for (int i = 0; i < 3; i++) {
            Top.getRowConstraints().add(new RowConstraints(30));
        }
        Top.getRowConstraints().add(new RowConstraints(560));

        Arrays.asList(labelRadius, labelHeight, labelNumber, labelStress, labelEnergy).forEach(label -> {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        });
        Arrays.asList(buttonEnter, buttonEnergyMinimizationStress, buttonEnergyMinimization, buttonDiagram).forEach(button -> {
            GridPane.setHalignment(button, HPos.CENTER);
            GridPane.setValignment(button, VPos.CENTER);
        });

        Top.add(labelRadius, 0, 0);
        Top.add(labelHeight, 0, 1);
        Top.add(labelNumber, 0, 2);
        Top.add(textFieldRadius, 1, 0);
        Top.add(textFieldHeight, 1, 1);
        Top.add(textNumber, 1, 2);
        Top.add(buttonEnter,2,0,1,3);
        Top.add(buttonEnergyMinimization,3,0,1,3);
        Top.add(labelStress, 4, 0);
        Top.add(textStress, 5, 0);
        Top.add(labelEnergy, 4, 1,1,2);
        Top.add(labelEnergyValue, 5, 1,1,2);
        Top.add(buttonEnergyMinimizationStress,6,0,1,3);
        Top.add(buttonDiagram,7,0,1,3);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(20);
        camera.getTransforms().addAll (rotateX, rotateY, new Translate(0, 0, -500));

        Tube tube = new Tube(80,80,Color.rgb(225,225,0));
        Group group = new Group(tube.getTube());
        SubScene subScene = new SubScene(group, 750, 550, true, SceneAntialiasing.BALANCED);
        subScene.setFill (Color.rgb (129, 129, 129));
        subScene.setCamera(camera);
        var root3d = new Group(subScene);

        initMouseControl(subScene);

        Top.add(root3d,0,3,8,1);
        GridPane.setHalignment(root3d, HPos.CENTER);
        GridPane.setValignment(root3d, VPos.CENTER);

        buttonEnter.setOnAction(e -> {
            int n = Integer.parseInt(textNumber.getText());
            tube.setHeight(Double.parseDouble(textFieldHeight.getText()));
            tube.setRadius(Double.parseDouble(textFieldRadius.getText()));
            ObservableList<Particle> particles = new Generation(tube, n, group).ParticlesGeneration();

            buttonEnergyMinimization.setOnAction(actionEvent -> {
                Minimization minimization = new Minimization(particles,2,tube);
                ObservableList<Particle> list = minimization.minimization();
                new Mapping(n,group,tube,list).MappingParticle();
            });
        });

        var scene = new Scene(Top, WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("NanoTube Student Project");
    }

    /**
     * Метод воспроизводящий сцену
     * @param args переменные
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Метод отвечающий за вращениее камеры с помощью "мыши"
     * @param scene сцена
     */
    private void initMouseControl(SubScene scene) {
        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            double dx = (anchorX - event.getSceneX());
            double dy = (anchorY - event.getSceneY());
            if (event.isPrimaryButtonDown()) {
                rotateX.setAngle(rotateX.getAngle() -
                        (dy /RADIUS  * 360) * (Math.PI / 180));
                rotateY.setAngle(rotateY.getAngle() -
                        (dx /RADIUS * -360) * (Math.PI / 180));
            }
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
        });
    }
}

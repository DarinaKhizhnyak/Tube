package org.nanotubes.generation.Mapping;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import org.nanotubes.generation.Geom.Tube;

/**
 * Класс создающий на основе математической модели цилиндра 3D-объект
 */
public class TubeView {

    /**
     * Математическая модель цилиндра
     */
    private final Tube tube ;

    /**
     * 3D-объект цилиндр
     */
    private final Cylinder view ;

    /**
     * Цвет цилиндра
     */
    private Color color;

    /**
     * Конструктор класса создающий 3D-объект на онснове математицеской модели цилиндра
     * @param tube 3D-объект цилиндр
     * @param color Цвет цилиндра
     */
    public TubeView(Tube tube, Color color) {
        this.tube = tube;
        this.view = new Cylinder();
        this.color = color;
        view.setMaterial(new PhongMaterial(color));
        view.heightProperty().bind(tube.heightProperty());
        view.radiusProperty().bind(tube.radiusProperty());
    }

    /**
     * Метод рисующий цилиндр в интерфейсе программы
     * @return 3D-объект цилиндр
     */
    public Node asNode() {
        return view;
    }
}

package org.nanotubes.generation.Geom;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

/**
 * Класс описывающий цилиндр, на котором распологаются частицы: радиус, высота, форма, цвет
 */
public class Tube {
    /**
     * Радиус цилиндра
     */
    private double radius;
    /**
     * Высота цилиндра
     */
    private double height;
    /**
     * Форма объекта: цилиндр
     */
    private final Cylinder tube;
    /**
     * Цвет цилиндра
     */
    private final Color color;

    /**
     * Конструктор класс создающий цилиндр
     * @param radius радиус цилиндра
     * @param height высота цилиндра
     * @param color цвет цилиндра
     */
    public Tube(double radius, double height, Color color) {
        this.height = height;
        this.color = color;
        this.radius = radius;
        this.tube = new Cylinder(radius,height);
        tube.setMaterial(new PhongMaterial(color));
    }

    /**
     * Метод возвращающий значение цвета цилиндра
     * @return цвет цилиндра
     */
    public Color getColor() {
        return color;
    }

    /**
     * Метод возвращающий значение радиуса цилиндра
     * @return радиус цилмндра
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Метод возвращающй значение высоты цилиндра
     * @return высота цилиндра
     */
    public double getHeight() {
        return height;
    }

    /**
     * Метод возвращающий объект цилиндра
     * @return цилиндр
     */
    public Cylinder getTube() {
        return tube;
    }

    /**
     * Метод изменяющий радиус цилиндра
     * @param radius радиус цилиндра
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Метож изменяющий высоту цилиндра
     * @param height высота цилиндра
     */
    public void setHeight(double height) {
        this.height = height;
    }
}

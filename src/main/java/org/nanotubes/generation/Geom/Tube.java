package org.nanotubes.generation.Geom;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Класс создающий математическую модель цилиндра со списком свойств
 */
public class Tube {
    /**
     * Свойство цилиндра - высота
     */
    private final DoubleProperty height = new SimpleDoubleProperty();

    /**
     * Метод возвращающий свойство цилиндра "высота"
     * @return свойство цилиндра "высота"
     */
    public DoubleProperty heightProperty() {
        return height ;
    }

    /**
     * Метод возвращающий численное значение свойства цилиндра "высота"
     * @return численное значение высоты цилиндра
     */
    public final double getHeight() {
        return heightProperty().get();
    }

    /**
     * Метод изменяющий численное значение свойства цилиндра "высота"
     * @param height численное значение высоты цилиндра
     */
    public final void setHeight(double height) {
        heightProperty().set(height);
    }

    /**
     * Свойство цилиндра - радиус
     */
    private final DoubleProperty radius = new SimpleDoubleProperty();

    /**
     Метод возвращающий свойство цилиндра "радиус"
     * @return свойство цилиндра "радиус"
     */
    public DoubleProperty radiusProperty() {
        return radius;
    }

    /**
     * Метод возвращающий численное значение свойства цилиндра "радиус"
     * @return численное значение радиуса цилиндра
     */
    public final double getRadius() {
        return radiusProperty().get();
    }

    /**
     * Метод изменяющий численное значение свойства цилиндра "радиус"
     * @param radius численное значение радиуса цилиндра
     */
    public final void setRadius(double radius) {
        radiusProperty().set(radius);
    }

    /**
     * Конструктор класса создающий математическую модель цилиндра и переприсваивающий численные значения свойств
     * @param radius численное значение радиуса цилиндра
     * @param height численное значение высоты цилиндра
     */
    public Tube(double radius, double height) {
        setRadius(radius);
        setHeight(height);
    }
}

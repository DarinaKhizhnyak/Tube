package org.nanotubes.generation.Geom;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Класс описывающий частицу
 * Радиус, форма, положение в цилиндрической системе координат
 */
public class Particle {
    /**
     * Радиус частицы
     */
   private final double radius;
    /**
     * Координата phi в цилиндрической системе
     */
   private double phi;
    /**
     * Координата rho в цилиндрической системе
     */
   private double rho;
    /**
     * Координата z в цилиндрической системе
     */
   private double z;
    /**
     * Форма частицы
     */
   private final Sphere particle;
   private final Color color;

    /**
     * Конструктор класса частица
     * @param phi координата phi в цилиндрической системе
     * @param rho координата rho в цилиндрической системе
     * @param z координата z в цилиндрической системе
     * @param radius радиус частицы
     * @param color цвет частицы
     */
   public Particle(double phi, double rho, double z, double radius, Color color) {
       this.particle = new Sphere(radius);
       this.radius = radius;
       this.phi = phi;
       this.rho = rho;
       this.z = z;
       this.color = color;
       particle.setMaterial(new PhongMaterial(color));
       particle.setTranslateX(rho * Math.cos(phi));
       particle.setTranslateY(z);
       particle.setTranslateZ(rho * Math.sin(phi));
   }

    /**
     * Метод возвращающий расстояние между двумя точками в двумерном пространстве
     * @param x1 координата первой точки двумерного пространства совподающая с осью абцисс декартовой системы координат
     * @param y1 координата первой точки двумерного пространства совподающая с осью ординат декартовой системы координат
     * @param x2 координата второй точки двумерного пространства совподающая с осью абцисс декартовой системы координат
     * @param y2 координата второй точки двумерного пространства совподающая с осью ординат декартовой системы координат
     * @return расстояние между двумя точками
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    private static double distanceWith2Pi(double x1, double y1, double x2, double y2) {
        x1 = 2*Math.PI - Math.abs(x1 - x2);
        y1 -= y2;
        return Math.sqrt(x1 * x1 + y1 * y1);
    }

    public double distanceWith2Pi(Particle p) { return distanceWith2Pi(getPhi()*getRho(), getZ(), p.getPhi()*p.getRho(),p.getZ()); }

    /**
     * Метод возвращающий расстояние от данной частицы к заданной частице в в плоскости развертки цилиндра
     * @param p данная точка
     * @return растояние межд двумя точками
     */
    public double distance(Particle p) {
        return distance(getPhi()*getRho(), getZ(), p.getPhi()*p.getRho(),p.getZ());
    }

    /**
     * Метод возвращающий радиус
     * @return радиус частицы
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Метод возвращающий значение координаты phi
     * @return координата phi в цилиндрической системе
     */
    public double getPhi() {
        return phi;
    }

    /**
     * Метод изменяющий значение координаты phi
     * @param phi координата phi в цилиндрической системе
     */
    public void setPhi(double phi) {
        this.phi = phi;
        particle.setTranslateX(rho * Math.cos(phi));
        particle.setTranslateZ(rho * Math.sin(phi));
    }

    /**
     * Метод возвращающий значение координаты rho
     * @return координата rho в цилиндрической системе
     */
    public double getRho() {
        return rho;
    }

    /**
     * Метод изменяющий значение координаты rho
     * @param rho координата rho в цилиндрической системе
     */
    public void setRho(double rho) {
        this.rho = rho;
        particle.setTranslateX(rho * Math.cos(phi));
        particle.setTranslateZ(rho * Math.sin(phi));
    }

    /**
     * Метод возвращающий значение координаты z
     * @return координата z в цилиндрической системе
     */
    public double getZ() {
        return z;
    }

    /**
     * Метод изменяющий значение координаты z
     * @param z координата z в цилиндрической системе
     */
    public void setZ(double z) {
        this.z = z;
        particle.setTranslateY(z);
    }

    /**
     * Метод возвращающий частицу
     * @return частица
     */
    public Sphere getParticle() {
        return particle;
    }

    public Color getColor() { return color; }
}

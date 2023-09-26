package org.nanotubes.generation;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import org.nanotubes.generation.Geom.Tube;
import org.nanotubes.generation.PoissonDisk.PoissonDiskIn2D;
import org.nanotubes.generation.PoissonDisk.Vector2DDouble;
import org.nanotubes.generation.Geom.Particle;

import java.util.Collections;
import java.util.List;

import static javafx.scene.paint.Color.*;

/**
 * Класс занимающийся генерацией частиц расположенных случайным образом
 */
public class Generation {

    /**
     * Цвета шириков
     */
    private static final Color[] COLORS = new Color[] { RED, YELLOW, GREEN,
            BROWN, BLUE, PINK, BLACK };
    /**
     * Цилиндр
     */
    private final Tube tube;
    /**
     * Колличество частиц
     */
    private final int numberOfParticle;
    /**
     * Радиус частицы
     */
    private final double radius;

    /**
     * Конструктор класса Ganeration создающий объект Ganeration
     * @param tube параметры цилиндра
     * @param numberOfParticle количество частиц
     */
    public Generation(Tube tube, int numberOfParticle) {
        this.tube = tube;
        this.numberOfParticle = numberOfParticle;
        radius = Math.sqrt(tube.getRadius()*tube.getHeight()/(2*numberOfParticle));
    }

    /**
     * Метод создающий псевдослучайным образом (расспределение Пуассона) заданное количество частиц рассположенных на боковой поверхности цилиндра
     * @return список частиц
     */
    public ObservableList<Particle> ParticlesGeneration(ObservableList<Particle> particlesList) {
        particlesList.clear();
        List <Vector2DDouble> list = new PoissonDiskIn2D(0,0,tube.getRadius()*2*Math.PI-2*radius,
                tube.getHeight(),radius*2, numberOfParticle).ListOfPointsPoissonDisk();
        Collections.shuffle(list);

        for (int i = 0; i < numberOfParticle; i++) {
            Particle particle = new Particle(list.get(i).getX()/tube.getRadius(), tube.getRadius(),
                    list.get(i).getY()-tube.getHeight()/2, radius, COLORS[i % COLORS.length]);
            particlesList.add(particle);
        }

        return particlesList;
    }
}

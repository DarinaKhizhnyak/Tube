package org.nanotubes.generation.Mapping;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

public class Mapping {
    /**
     * Частицы, которые необходимо отобразить
     */
    private final ObservableList<Particle> list;
    /**
     * Колличество частиц
     */
    private final int numberOfParticle;
    /**
     * Группа граффических объекто
     */
    private final Group group;
    /**
     * Цилиндр
     */
    private final Tube tube;

    public Mapping(int numberOfParticle, Group group, Tube tube, ObservableList<Particle> list) {
        this.numberOfParticle = numberOfParticle;
        this.group = group;
        this.list = list;
        this.tube = tube;
    }
    public void MappingParticle() {
        group.getChildren().clear();
        group.getChildren().add(new TubeView(tube, Color.YELLOW).asNode());
        for (int i = 0; i < numberOfParticle; i++) {
            group.getChildren().add(list.get(i).getParticle());
        }
    }
    public void Print() {
        for (int i = 0; i < numberOfParticle; i++) {
            System.out.println(i);
            System.out.println(list.get(i).getPhi());
            System.out.println(list.get(i).getZ());
        }
    }

}

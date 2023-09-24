package org.nanotubes.minimization;

import javafx.collections.ObservableList;
import javafx.scene.Group;
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

    public Mapping(int numberOfParticle, Group group, Tube tube, ObservableList<Particle> list) {
        this.numberOfParticle = numberOfParticle;
        this.group = group;
        this.list = list;
        group.getChildren().clear();
        group.getChildren().add(tube.getTube());
    }
    public void MappingParticle() {
        for (int i = 0; i < numberOfParticle; i++) {
            group.getChildren().add(list.get(i).getParticle());
        }
    }

}

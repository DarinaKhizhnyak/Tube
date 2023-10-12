package org.nanotubes.minimization;

import javafx.collections.ObservableList;

import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

import java.util.ArrayList;

import static java.lang.Math.pow;


/**
 * Класс, который минимизирует энергию системы частиц
 */
public class Minimization {
    /**
     * Эмперический коэффициент для угла
     */
    private double COEFFICIENT_FOR_ANGLE = 0.5;
    /**
     * Эмпирический коэффициент для координаты z
     */
    private double COEFFICIENT_FOR_Z = 1;

    /**
     * Допустимое значение коэффициента k для угла
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT_VALUE_K_ANGEL = 0.00001;
    /**
     * Допустимое значение коэффициента k для координаты
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT_VALUE_K_Z = 0.00001;
    /**
     * Допустимое значение коэффициента разницы энергий
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE = 0.00001;
    /**
     * Максимальное значение для типа double
     */
    @SuppressWarnings("FieldCanBeLocal")
    public static final double MAX_VALUE = 1.7976931348623157E308;

    /**
     * Исходные координаты частиц, выбранные с помощью распределения Пуассона
     */
    private final ObservableList<Particle> poissonDiskCoordinatesParticles;
    /**
     * Количество частиц
     */
    private final int numberOfParticle;
    /**
     * Степень (из теории)
     */
    private final int degree;
    /**
     * Параметры цилиндра
     */
    private final double heightTube;

    /**
     * Массив для записи значений энергий системы на каждом шаге минимизации
     */
    private final ArrayList<Double> arrayEnergy = new ArrayList<>();
    /**
     * Массив для записи значений коэффициента координаты на каждом шаге системы
     */
    private final ArrayList<Double> arrayCoefficientForZ = new ArrayList<>();

    /**
     * Массив для записи значений коэффициента угла на каждом шаге системы
     */
    private final ArrayList<Double> arrayCoefficientForAngel = new ArrayList<>();


    /**
     * Конструктор класса Minimization создающий обект Minimization
     * @param poissonDiskCoordinatesParticles исходные координаты частиц, выбранные с помощью распределения Пуассона
     * @param degree степень (из теории)
     * @param tube параметры цилиндра
     */
    public Minimization(ObservableList<Particle> poissonDiskCoordinatesParticles, int degree, Tube tube) {
        this.poissonDiskCoordinatesParticles = poissonDiskCoordinatesParticles;
        this.degree = degree;
        numberOfParticle = poissonDiskCoordinatesParticles.size();
        heightTube = tube.getHeight();
    }

    public ObservableList<Particle> minimization () {
        ObservableList<Particle> list = poissonDiskCoordinatesParticles;
        double energyOld = MAX_VALUE;
        double energyNew = energyOfSystem(list);

        arrayEnergy.add(energyNew);
        arrayCoefficientForZ.add(COEFFICIENT_FOR_Z);
        arrayCoefficientForAngel.add(COEFFICIENT_FOR_ANGLE);
        int iter = 10000;
        while (COEFFICIENT_FOR_ANGLE > ACCEPTABLE_COEFFICIENT_VALUE_K_ANGEL && COEFFICIENT_FOR_Z > ACCEPTABLE_COEFFICIENT_VALUE_K_Z && energyOld >= energyNew && iter > 0) {
            iter--;
            if (energyOld - energyNew < ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE) {
                COEFFICIENT_FOR_Z = COEFFICIENT_FOR_Z/2;
                COEFFICIENT_FOR_ANGLE = COEFFICIENT_FOR_ANGLE/2;
            }
            energyOld = energyNew;
            stepOfMinimization(list);
            energyNew = energyOfSystem(list);

            arrayEnergy.add(energyNew);
            arrayCoefficientForZ.add(COEFFICIENT_FOR_Z);
            arrayCoefficientForAngel.add(COEFFICIENT_FOR_ANGLE);
        }

        return list;
    }

    /**
     * Метод возвращающий чатицы после одного шага минимизации
     * @param list частицы до минимизации
     */
    private void stepOfMinimization (ObservableList<Particle> list) {
        for (int i = 0; i < numberOfParticle; i++) {
            Particle particle = newParticle(list, i);
            if (energyOfPartial(list,list.get(i),i) > energyOfPartial(list,particle,i)) {
                list.set(i, particle);
            }
        }
    }

    /**
     * Метод возвращающий частицу с новыми координатами, учитывая взаимодейтвие выбранной частицы с другими частицами
     * @param coordinates частицы
     * @param i номер выбранной частицы
     * @return выбранную частицу с новыми координатами
     */
    private Particle newParticle(ObservableList<Particle> coordinates, int i) {
        Particle particle = new Particle(coordinates.get(i).getPhi(),coordinates.get(i).getRho(),coordinates.get(i).getZ(),coordinates.get(i).getRadius(), coordinates.get(i).getColor());
        double ForcePhi = 0.0;
        double ForceZ = 0.0;
        for (int j = 0; j < numberOfParticle; j++) {
            Particle jParticle = coordinates.get(j);
            if (i != j) {
                if (Math.abs(particle.getPhi() - jParticle.getPhi()) <= Math.PI/particle.getRadius()) {
                    ForcePhi += (degree * particle.getRho() *
                            (particle.getPhi() - jParticle.getPhi())) / pow(particle.distance(jParticle), degree + 2);
                    ForceZ += (degree *
                            (particle.getZ() - jParticle.getZ())) / pow(particle.distance(jParticle), degree + 2);
                } else {
                    ForcePhi += (degree * particle.getRho() * (-1)*
                            (particle.getPhi() - jParticle.getPhi())) / pow(particle.distanceWith2Pi(jParticle), degree + 2);
                    ForceZ += (degree *
                            (particle.getZ() - jParticle.getZ())) / pow(particle.distanceWith2Pi(jParticle), degree + 2);
                }
            }
        }
        ForceZ += degree / pow(particle.getZ() - heightTube / 2, degree + 1) +
                degree / pow(particle.getZ() + heightTube / 2, degree + 1);

        double phi = particle.getPhi() + COEFFICIENT_FOR_ANGLE * ForcePhi;

        if (phi >= 2*Math.PI) {
            particle.setPhi(2*Math.PI - phi);
        } else if (phi <= 0) {
            particle.setPhi(0-phi);
        } else {
            particle.setPhi(phi);
        }

        double z = particle.getZ() + COEFFICIENT_FOR_Z * ForceZ;

        if (z > heightTube/2) {
            particle.setZ((-1) * particle.getZ());
            particle.setPhi((-1) * particle.getPhi());
        } else if (z < -heightTube/2) {
            particle.setZ((-1) * particle.getZ());
            particle.setPhi((-1)* particle.getPhi());
        } else {
            particle.setZ(z);
        }

        return particle;
    }

    /**
     * Метод возвращающий энергию системы в двумерном пространстве развертки боковой стороны цилиндра
     * @param coordinates частицы
     * @return энергию системы частиц
     */
    private double energyOfSystem (ObservableList<Particle> coordinates) {
        double Energy = 0;
        for (int i = 0; i < numberOfParticle; i++) {
            for (int j = 0; j < numberOfParticle; j++) {
                if (i != j) {
                    if (Math.abs(coordinates.get(i).getPhi() - coordinates.get(j).getPhi()) <= Math.PI) {
                        Energy += 1/pow(coordinates.get(i).distance(coordinates.get(j)),degree);
                    }  else {
                        Energy += 1/pow(coordinates.get(i).distanceWith2Pi(coordinates.get(j)),degree);
                    }
                }
            }
            Energy += 1 / pow(coordinates.get(i).getZ() - heightTube / 2, degree) +
                    1 / pow(coordinates.get(i).getZ() + heightTube / 2, degree);
        }
        return Energy;
    }

    /**
     * Метод возвращающий значение энергии системы при изменении координат одного элемета системы
     * @param coordinates частицы
     * @param particle измененная частица
     * @return значение энергии системы при изменении координат одного элемета системы
     */
    private double energyOfPartial (ObservableList<Particle> coordinates, Particle particle, int i) {
        double Energy = 0;
        for (int j = 0; j < numberOfParticle; j++) {
            if (i != j) {
                if (Math.abs(particle.getPhi() - coordinates.get(j).getPhi()) <= Math.PI) {
                    Energy += 1/pow(particle.distance(coordinates.get(j)),degree);
                }  else {
                    Energy += 1/pow(particle.distanceWith2Pi(coordinates.get(j)),degree);
                }
            }
        }
        Energy += 1 / pow(particle.getZ() - heightTube / 2, degree) +
                1 / pow(particle.getZ() + heightTube / 2, degree);
        return Energy;
    }

    /**
     * Метод возвращающий значения энергии системы на каждом шаге минимизации
     * @return массив значений энергии системы на каждом шаге минимизации
     */
    public ArrayList<Double> getArrayEnergy() {
        return arrayEnergy;
    }

    /**
     * Метод возвращающий значения коэффициента для координаты на каждом шаге минимизации
     * @return массив значений коэффициента на каждом шаге минимизации
     */
    public ArrayList<Double> getArrayCoefficientForZ() {
        return arrayCoefficientForZ;
    }

    /**
     * Метод возвращающий значения коэффициента для угла на каждом шаге минимизации
     * @return массив значений коэффициента на каждом шаге минимизации
     */
    public ArrayList<Double> getArrayCoefficientForAngel() {
        return arrayCoefficientForAngel;
    }

}

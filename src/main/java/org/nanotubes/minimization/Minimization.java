package org.nanotubes.minimization;

import javafx.collections.ObservableList;

import org.nanotubes.generation.Geom.Particle;
import org.nanotubes.generation.Geom.Tube;

import static java.lang.Math.pow;


/**
 * Класс, который минимизирует энергию системы частиц
 */
public class Minimization {
    /**
     * Эмперический коэффициент для угла
     */
    private double COEFFICIENT_FOR_ANGLE = 1;
    /**
     * Эмпирический коэффициент для координаты z
     */
    private double COEFFICIENT_FOR_Z = 1;

    /**
     * Допустимое значение коэффициента k для угла
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT_VALUE_K_ANGEL = 0.0001;
    /**
     * Допустимое значение коэффициента k для координаты
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_COEFFICIENT_VALUE_K_Z = 0.0001;
    /**
     * Допустимое значение коэффициента разницы энергий
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final double ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE = 0.0001;
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
    private double[] arrayEnergy = new double[0];
    /**
     * Массив для записи значений коэффициента координаты на каждом шаге системы
     */
    private double[] arrayCoefficientForZ = new double[0];

    /**
     * Массив для записи значений коэффициента угла на каждом шаге системы
     */
    private double[] arrayCoefficientForAngel = new double[0];


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

        arrayEnergy = extendArray(arrayEnergy, energyNew);
        arrayCoefficientForZ = extendArray(arrayCoefficientForZ, COEFFICIENT_FOR_Z);
        arrayCoefficientForAngel = extendArray(arrayCoefficientForAngel, COEFFICIENT_FOR_ANGLE);

        while (COEFFICIENT_FOR_ANGLE > ACCEPTABLE_COEFFICIENT_VALUE_K_ANGEL && COEFFICIENT_FOR_Z > ACCEPTABLE_COEFFICIENT_VALUE_K_Z && energyOld > energyNew) {
            if (energyOld - energyNew < ACCEPTABLE_VALUE_OF_ENERGY_DIFFERENCE) {
                COEFFICIENT_FOR_Z = COEFFICIENT_FOR_Z/2;
                COEFFICIENT_FOR_ANGLE = COEFFICIENT_FOR_ANGLE/2;
            }
            energyOld = energyNew;
            stepOfMinimization(list);
            energyNew = energyOfSystem(list);

            arrayEnergy = extendArray(arrayEnergy, energyNew);
            arrayCoefficientForZ = extendArray(arrayCoefficientForZ, COEFFICIENT_FOR_Z);
            arrayCoefficientForAngel = extendArray(arrayCoefficientForAngel, COEFFICIENT_FOR_ANGLE);
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
            if (energyOfSystem(list) < energyOfSystem(list,particle,i)) {
                list.set(i, particle);
            }
        }
        arrayEnergy = extendArray(arrayEnergy, energyOfSystem(list));
    }

    /**
     * Метод возвращающий частицу с новыми координатами, учитывая взаимодейтвие выбранной частицы с другими частицами
     * @param coordinates частицы
     * @param i номер выбранной частицы
     * @return выбранную частицу с новыми координатами
     */
    private Particle newParticle(ObservableList<Particle> coordinates, int i) {
        Particle particle = coordinates.get(i);
        double ForcePhi = 0.0;
        double ForceZ = 0.0;
        for (int j = 0; j < numberOfParticle; j++) {
            Particle jParticle = coordinates.get(j);
            if (i != j) {
                ForcePhi += (numberOfParticle * particle.getRho() *
                        (particle.getPhi() - jParticle.getPhi())) / pow(particle.distance(jParticle), degree + 2);
                ForceZ += (numberOfParticle *
                        (particle.getZ() - jParticle.getZ())) / pow(particle.distance(jParticle), degree + 2);
            }
        }
        ForceZ += degree / pow(particle.getZ() - heightTube / 2, degree + 1) +
                degree / pow(particle.getZ() + heightTube / 2, degree + 1);

        particle.setPhi(particle.getPhi() + COEFFICIENT_FOR_ANGLE * ForcePhi);
        particle.setZ(particle.getZ() + COEFFICIENT_FOR_Z * ForceZ);

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
                    Energy += 1/pow(coordinates.get(i).distance(coordinates.get(j)),degree);
                }
            }
            Energy += degree / pow(coordinates.get(i).getZ() - heightTube / 2, degree) +
                    degree / pow(coordinates.get(i).getZ() + heightTube / 2, degree);
        }
        return Energy;
    }

    /**
     * Метод возвращающий значение энергии системы при изменении координат одного элемета системы
     * @param coordinates частицы
     * @param particle измененная частица
     * @param i номер измененной частицы
     * @return значение энергии системы при изменении координат одного элемета системы
     */
    private double energyOfSystem (ObservableList<Particle> coordinates, Particle particle, int i) {
        coordinates.set(i,particle);
        return energyOfSystem(coordinates);
    }

    /**
     * Метод добавляющий в масив новое значение
     * @param arr массив, в который необходимо добавить новое значение
     * @param value значение, кторое необходимо добавить в массив
     * @return массив с новым значением
     */
    private static double[] extendArray(double[] arr, double value) {
        double[] newArray = new double[arr.length + 1];
        System.arraycopy(arr, 0, newArray, 0, arr.length);
        newArray[newArray.length - 1] = value;
        return newArray;
    }

    /**
     * Метод возвращающий значения энергии системы на каждом шаге минимизации
     * @return массив значений энергии системы на каждом шаге минимизации
     */
    public double[] getArrayEnergy() {
        return arrayEnergy;
    }

    /**
     * Метод возвращающий значения коэффициента для координаты на каждом шаге минимизации
     * @return массив значений коэффициента на каждом шаге минимизации
     */
    public double[] getArrayCoefficientForZ() {
        return arrayCoefficientForZ;
    }

    /**
     * Метод возвращающий значения коэффициента для угла на каждом шаге минимизации
     * @return массив значений коэффициента на каждом шаге минимизации
     */
    public double[] getArrayCoefficientForAngel() {
        return arrayCoefficientForAngel;
    }

}

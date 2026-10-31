package com.meza_x19; // Creado por MeZa_x19

import java.util.Scanner; // Importamos librería Scanner para tomar datos desde teclado

public class ProyectoHawkins {

    public static void main (String[] args) {

        Scanner escáner = new Scanner(System.in); // Creamos un objeto de tipo Scanner

        final double MAX_ENERGÍA = 200.0; // Capacidad máxima de energía que soporta el sensor en microteslas (µT)
        final double LÍMITE_RIESGO = 150.0; // Umbral crítico de microteslas permitidos antes de la brecha

        String operadorActual; // Identificador o nombre de usuario operario

        double sensorMedido; // Almacena última lectura obtenida del sensor en microteslas
        int códigoRiesgo; // Código de riesgo devuelto tras evaluar la lectura
        int porcentajeEstado; // Porcentaje truncado calculado respecto a la capacidad máxima

        boolean portalAbierto; // Estado del portal (indica si la brecha dimensional está abierta o no)

        int opciónMenú = -1; /* Opción del menú seleccionada por el usuario
        Inicializamos la variable con un valor de -1 por si, antes de elegir una opción, el usuario introduce un tipo de dato incorrecto
        De esta forma, es imposible que el bucle principal y, por consecuencia, el programa, termine de forma inesperada */


        System.out.println("");
        System.out.print("Introduce nombre de operario: ");
        operadorActual = escáner.nextLine(); // Tomamos nombre del operador

        do { // Bucle del menú

            mostrarEncabezado(operadorActual); // Mostramos menú

            try { // Tomamos datos desde teclado. Esperamos un número

                opciónMenú = escáner.nextInt();

                switch (opciónMenú) { // En caso de que funcione, ejecutaremos la lógica de la opción correspondiente

                    case 1: // Inspeccionar sensor individual

                        System.out.println("\n\t--- INSPECCIÓN DE SENSOR INDIVIDUAL ---");

                        sensorMedido = leerLecturaSegura(escáner, "Introduce lectura µT: ");

                        códigoRiesgo = evaluarNivelRiesgo(sensorMedido);

                        porcentajeEstado = calcularPorcentajeTruncado(sensorMedido, MAX_ENERGÍA);

                        portalAbierto = ejecutarProtocoloAcción(códigoRiesgo);

                        System.out.println("El sensor detecta un " + porcentajeEstado + "% de energía."); // Resumen de datos final
                        System.out.println("El portal está " + (portalAbierto ? "abierto." : "cerrado."));

                    break;

                    case 2: // Iniciar muestreo continuo

                        System.out.println("\n\t--- MUESTREO CONTINUO ---");

                        simularMuestreoContinuo(escáner, LÍMITE_RIESGO);

                    break;

                    case 0: // Salir de la consola

                        System.out.println("\nSaliendo del programa...\n");
                        escáner.close();

                    break;

                    default: // Cualquier otro número

                        System.out.println("\nOpción no válida.");

                }

            } catch (java.util.InputMismatchException e) { // El usuario introdujo un tipo de dato incorrecto

                System.out.println("\n¡ERROR! El valor introducido no es correcto. Inténtelo de nuevo...");
                escáner.nextLine(); // Limpiamos búfer

            } catch (Exception e) { // Cualquier otro error

                System.out.println("\n¡ERROR! Ha ocurrido una excepción inesperada (" + e + "). Inténtelo de nuevo...");
                escáner.nextLine(); // Limpiamos búfer

            }

        } while (opciónMenú != 0); // Condición del menú

    } // Fin del método main

    public static void mostrarEncabezado (String nombre) { // Muestra la cabecera del sistema

        System.out.printf("\nHola, \"%s\"\n\t--- SISTEMA HAWKINS ---\n", nombre);
        System.out.printf("%4d. %-50s\n", 1, "Inspeccionar sensor individual");
        System.out.printf("%4d. %-50s\n", 2, "Iniciar muestreo continuo");
        System.out.printf("%4d. %-50s\n", 0, "Salir de la consola");
        System.out.printf("\tIntroduce opción: ");

    }

    public static double leerLecturaSegura (Scanner sc, String txt) { // Muestra el mensaje indicado y lee un decimal

        while (true) { // Repetir hasta que salga

            System.out.print("\n" + txt); // Pedimos valor al usuario

            try { // Intentamos recibir un número decimal desde teclado

                return sc.nextDouble(); // Si lo conseguimos, retornamos el dato a donde fue llamado

            }  catch (java.util.InputMismatchException e) { // El usuario introdujo un tipo de dato incorrecto

                System.out.println("\n¡ERROR! El valor introducido no es correcto. Inténtelo de nuevo...");
                sc.nextLine(); // Limpiamos búfer

            } catch (Exception e) { // Cualquier otro error

                System.out.println("\n¡ERROR! Ha ocurrido una excepción inesperada (" + e + "). Inténtelo de nuevo...");
                sc.nextLine(); // Limpiamos búfer

            }

        }

    }

    public static int evaluarNivelRiesgo (double sensor) { // Evalúa la lectura dada

        System.out.print("\nNivel: "); // Indicamos por pantalla nivel de riesgo
        System.out.println((sensor < 50) ? "Seguro" : (sensor <= 150) ? "Alerta" : "Crítico");

        if (sensor < 50) { // Seguro
            return 1;
        } else if (sensor <= 150) { // Alerta
            return 2;
        } else { // Crítico
            return 3;
        }

    }

    public static int calcularPorcentajeTruncado (double sensor, double max) { // Calcula el porcentaje de la lectura respecto a la capacidad máxima del sensor

        return (int) ((sensor / max) * 100);

    }

    public static boolean ejecutarProtocoloAcción (int código) { // Evalúa el código de riesgo

        switch (código) { // Como siempre devolvemos valor, no incluimos break tras ningún caso

            case 1:
                System.out.println("Modo estable");
                return false;

            case 2:
                System.out.println("Anomalía detectada");
                return false;

            case 3:
                System.out.println("¡ALERTA MÁXIMA!");
                return true;

            default:
                System.out.println("Código de riesgo no válido");
                return false;

        }

    }

    public static void simularMuestreoContinuo (Scanner sc, double max) { // Mantiene un bucle interactivo de lectura

        double sumaLecturas = 0.0;
        double lecturaActual;
        int índice = 0;

        while (true) { // Tomar lecturas de forma continua

            System.out.println("\nLectura negativa - fin | Lectura nula - omitir | Lectura supera límite " + max + " - advertencia");

            lecturaActual = leerLecturaSegura(sc, "Introduce lectura µT: "); // Guardamos lectura

            if (lecturaActual < 0) { // Lectura negativa

                System.out.println("Finalizar muestreo continuo...");
                break; // Salir del bucle

            } else if (lecturaActual == 0) { // Lectura nula

                System.out.println("Omitir lectura...");
                continue; // Continuar a siguiente repetición

            } else if (lecturaActual > max) { // Lectura supera límite

                System.out.println("¡Advertencia! Umbral superado");

            }

            sumaLecturas += lecturaActual; // Suma acumulativa de todas las lecturas

            ++índice; // Mantenemos la cuenta del número total de veces que hemos tomado una lectura

        }

        // Fin del muestreo

        System.out.println("\nFin del muestreo. Resultados:");

        if (índice == 0) { // No hubo registro de lecturas

            System.out.println("No se registraron muestras válidas. Inténtelo de nuevo.");

        } else {

            System.out.printf("Número de muestras totales válidas: %d\n", índice);
            System.out.printf("Media de todas las muestras: %.2f µT\n", sumaLecturas/índice);

        }

    }

}

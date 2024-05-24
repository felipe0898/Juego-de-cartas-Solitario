import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Juego {
  private List<Carta> mazo;
  private List<List<Carta>> pilasJuego;
  private List<List<Carta>> fundaciones;

  // Constructor de la clase Juego
  public Juego() {
    mazo = new ArrayList<>();
    pilasJuego = new ArrayList<>();
    fundaciones = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      pilasJuego.add(new ArrayList<>()); // Creación de las pilas del juego como listas vacías
    }
    for (int i = 0; i < 4; i++) {
      fundaciones.add(new ArrayList<>()); // Creación de las fundaciones del juego como listas vacías
    }
    crearMazo();
    mezclarMazo();
  }

  public void crearMazo() {
    for (int palo = 1; palo <= 4; palo++) {
      for (int valor = 1; valor <= 13; valor++) {
        mazo.add(new Carta(valor, palo, false)); // Creación de una nueva carta y adición al mazo
      }
    }
  }

  public void mezclarMazo() {
    Collections.shuffle(mazo); // Barajado aleatorio de las cartas del mazo
  }

  // Método para crear las pilas del juego
  public void crearPilas() {
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j <= i; j++) {
        Carta carta = mazo.remove(mazo.size() - 1); // Eliminación de la carta del mazo
        carta.setVisible(j == i); // Establecimiento de la visibilidad de la carta
        pilasJuego.get(i).add(carta);
      }
    }
  }

  public boolean moverCarta(Carta carta, List<Carta> pilaOrigen, List<Carta> pilaDestino) {
    if (validarMovimiento(carta, pilaDestino)) { // Verificación del movimiento según las reglas del juego
      pilaOrigen.remove(carta);
      pilaDestino.add(carta);
      if (!pilaOrigen.isEmpty()) {
        pilaOrigen.get(pilaOrigen.size() - 1).setVisible(true);
      }
      return true;
    }
    return false;
  }

  public boolean validarMovimiento(Carta carta, List<Carta> pilaDestino) {
    if (pilaDestino.isEmpty()) {
      return carta.getValor() == 13; // Solo se puede mover un Rey a una pila vacía
    }
    Carta cartaTop = pilaDestino.get(pilaDestino.size() - 1); // Obtención de la carta superior de la pila de destino
    return (cartaTop.getValor() == carta.getValor() + 1 &&
        (cartaTop.getPalo() + carta.getPalo()) % 2 == 1);
  }

  public void jugar() {
    crearPilas();
    interaccionJugador();
  }

  public void interaccionJugador() {
    Scanner scanner = new Scanner(System.in);
    while (!condicionVictoria()) {
      mostrarTablero();
      System.out.println("Selecciona la pila de origen (1-7) o 0 para salir: ");
      int origen = scanner.nextInt();
      if (origen == 0) {
        System.out.println("Salio del juego.");
        break;
      }
      System.out.println("Selecciona la pila de destino (1-7): ");
      int destino = scanner.nextInt();
      if (destino == 0) {
        System.out.println("Salio del juego.");
        break;
      }

      if (origen >= 1 && origen <= 7 && destino >= 1 && destino <= 7) {
        List<Carta> pilaOrigen = pilasJuego.get(origen - 1);
        List<Carta> pilaDestino = pilasJuego.get(destino - 1);
        if (!pilaOrigen.isEmpty()) {
          Carta carta = pilaOrigen.get(pilaOrigen.size() - 1);
          if (!moverCarta(carta, pilaOrigen, pilaDestino)) {
            System.out.println("Movimiento no válido");
          }
        } else {
          System.out.println("Pila de origen vacía");
        }
      } else {
        System.out.println("Pilas inválidas");
      }
    }
    if (condicionVictoria()) {
      System.out.println("¡Has ganado!");
    }
    scanner.close();
  }

  // Método para mostrar el estado actual del tablero
  public void mostrarTablero() {
    for (int i = 0; i < pilasJuego.size(); i++) {
      System.out.print("Pila " + (i + 1) + ": ");
      for (Carta carta : pilasJuego.get(i)) { // Bucle para iterar sobre todas las cartas de la pila actual
        System.out.print(carta + " ");
      }
      System.out.println();
    }
  }

  // Método para verificar si se cumple la condición de victoria del juego
  public boolean condicionVictoria() {
    for (List<Carta> fundacion : fundaciones) {
      if (fundacion.size() != 13) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) {
    Juego juego = new Juego();
    juego.jugar();
  }
}

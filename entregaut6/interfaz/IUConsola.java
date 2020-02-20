package entregaut6.interfaz;
import entregaut6.modelo.PlataformaCursos;
import entregaut6.modelo.Nivel;

/**
 * Interfaz de texto
 *  
 */
import java.util.Scanner;
import java.util.TreeSet;

public class IUConsola
{

    private static final int MOSTRAR_CURSOS = 1;
    private static final int CURSOS_EN_CATEGORIA = 2;
    private static final int BORRAR_CURSOS = 3;
    private static final int CURSO_MAS_ANTIGUO = 4;
    private static final int SALIR = 5;

    private Scanner teclado;
    private PlataformaCursos plataforma;

    /**
     * Constructor de la clase IUConsola
     */
    public IUConsola() {

        teclado = new Scanner(System.in);
        plataforma = new PlataformaCursos();
        plataforma.leerDeFichero();

    }

    /**
     *  Incluye la lógica de la aplicación
     *  
     */
    public void iniciar() {

        int opcion = menu();
        while (opcion != SALIR) {
            switch (opcion) {
                case MOSTRAR_CURSOS:
                mostrarCursoss();
                break;
                case CURSOS_EN_CATEGORIA:
                cursosEnCategoria();
                break;
                case BORRAR_CURSOS:
                borrarCursos();
                break;
                case CURSO_MAS_ANTIGUO:
                cursoMasAntiguo();
                break;

            }
            pausa();
            opcion = menu();
        }
        salir();

    }

    /**
     * Presenta el menú
     * @return  la opción seleccionada
     */
    private int menu() {

        borrarPantalla();
        System.out.println("*****************************************");
        System.out.println("   Cursos en la plataforma online ");
        System.out.println("*****************************************");
        System.out.println("1. Mostrar cursos");
        System.out.println("2. Total cursos en categoría");
        System.out.println("3. Borrar cursos de una categoría y nivel");
        System.out.println("4. Curso más antiguo");
        System.out.println("5. Salir");
        System.out.print("Teclee opción: ");
        int opcion = teclado.nextInt();
        while (!opcionValida(opcion)) {
            System.out.print("Opción incorrecta, Teclee opción: ");
            opcion = teclado.nextInt();
        }
        teclado.nextLine();
        return opcion;
    }

    /**
     *  Devuelve true si la opción es correcta, false en otro caso
     *
     */
    private boolean opcionValida(int opcion) {

        return opcion >= MOSTRAR_CURSOS && opcion <= SALIR;
    }

    /**
     * Muestra los cursos en la plataforma
     */
    private void mostrarCursoss() {

        borrarPantalla();
        plataforma.escribir();
    }

    /**
     * Muestra el total de cursos de una  categoría previamente introducida por teclado
     */
    private void cursosEnCategoria() {

        System.out.print("Introduzca categoría: ");
        String categoria = teclado.nextLine().trim();
        int total = plataforma.totalCursosEn(categoria);
        if (total == -1) {
            System.out.println("Categoría " + categoria + " no existente");
        }
        else {
            System.out.println("Hay " + total +
                " curso/s de  " + categoria.toUpperCase());
        }

    }

    /**
     *  Devuelve true si la opción es correcta, false en otro caso
     *
     */
    private boolean categoriaNoValida(String categoria) {

        return !(plataforma.obtenerCategorias())
        .contains(categoria.toUpperCase());
    }

    /**
     * borra de la plataforma los cursos de categoria y nivel pedidos por teclado
     */
    private void borrarCursos() {

        System.out.print("Introduzca categoria: ");
        String categoria = teclado.nextLine().trim();
        while (categoriaNoValida(categoria)) {
            System.out.print(
                "Categoría incorrecta\nIntroduzca categoría: ");
            categoria = teclado.nextLine().trim();
        }
        System.out.print(
            "Introduzca nivel (0 - principiante, 1 - intermedio, 2 - avanzado) : ");
        int n = teclado.nextInt();
        while (noCorrectoNivel(n)) {
            System.out.print("Nivel incorrecto\nTeclee nivel: ");
            n = teclado.nextInt();
        }
        Nivel nivel = Nivel.values()[n];

        TreeSet<String> borrados = plataforma.borrarCursosDe(categoria, nivel);
        System.out.println("Se han borrado de la plataforma "
            + borrados.size() + " curso/s: " + borrados.toString());
        teclado.nextLine();
    }

    /**
     *  Devuelve true si el nivel es correcto, false en otro caso
     *
     */
    private boolean noCorrectoNivel(int n) {

        return n < Nivel.PRINCIPIANTE.ordinal() || 
        n > Nivel.AVANZADO.ordinal();
    }

    /**
     * muestra el curso más antiguo en la plataforma
     */
    private void cursoMasAntiguo() {

        System.out.print("El curso más antiguo es ");
        System.out.println(plataforma.cursoMasAntiguo());
    }

    /**
     * Borrar la pantalla
     */
    private void borrarPantalla() {

        System.out.println("\u000C");
    }

    /**
     * Hacer una pausa
     */
    private void pausa() {

        System.out.println("Pulse <Intro> para continuar");
        teclado.nextLine();
    }

    /**
     * salir de la aplicación
     */
    private void salir() {

        System.out.println("\n----- Finalizada la aplicación  ------\n");
    }
}

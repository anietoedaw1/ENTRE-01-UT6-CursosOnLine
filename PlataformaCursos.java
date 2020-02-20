import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Antonio Nieto
 * Un objeto de esta clase mantiene una 
 * colección map que asocia  categorías (las claves) con
 * la lista (una colección ArrayList) de cursos que pertenecen a esa categoría 
 * Por ej. una entrada del map asocia la categoría 'BASES DE DATOS"' con
 * una lista de cursos de esa categoría
 * 
 * Las claves en el map se recuperan en orden alfabético y 
 * se guardan siempre en mayúsculas
 */
public class PlataformaCursos
{

    private final String ESPACIO = " ";
    private final String SEPARADOR = ":";
    private TreeMap<String, ArrayList<Curso>> plataforma;

    /**
     * Constructor  
     */
    public PlataformaCursos() {
        plataforma = new TreeMap<>();
    }

    /**
     * añadir un nuevo curso al map en la categoría indicada
     * Si ya existe la categoría se añade en ella el nuevo curso
     * (al final de la lista)
     * En caso contrario se creará una nueva entrada en el map con
     * la nueva categoría y el curso que hay en ella
     * Las claves siempre se añaden en mayúsculas  
     *  
     */
    public void addCurso(String categoria, Curso curso) {
        ArrayList<Curso>cursos = new ArrayList<>();        
        if(plataforma.containsKey(categoria)){
            cursos = plataforma.get(categoria);
            cursos.add(curso);
        }
        else{
            cursos.add(curso);
            plataforma.put(categoria,cursos);
        }
    }

    /**
     *  Devuelve la cantidad de cursos en la categoría indicada
     *  Si no existe la categoría devuelve -1
     *
     */
    public int totalCursosEn(String categoria) {
        int acumulador = 0;
        boolean hayCurso = false;
        Set<String> totalCursos = plataforma.keySet();
        Iterator <String> it = totalCursos.iterator();
        while(it.hasNext()){
            String curso = it.next();
            if(curso.equalsIgnoreCase(categoria)){
                ArrayList<Curso> cursos = plataforma.get(curso);
                acumulador = cursos.size();
                hayCurso = true;
            }
        }
        if(hayCurso){
            return acumulador;
        }
        else{
            return -1;}
    }

    /**
     * Representación textual de la plataforma (el map), cada categoría
     * junto con el nº total de cursos que hay en ella y a continuación
     * la relación de cursos en esa categoría (ver resultados de ejecución)
     * 
     * De forma eficiente ya que habrá muchas concatenaciones
     * 
     * Usar el conjunto de entradas y un iterador
     */
    public String toString() {
        Set<Map.Entry<String,ArrayList<Curso>>> conjuntoCursos = plataforma.entrySet();
        Iterator<Map.Entry<String,ArrayList<Curso>>> it = conjuntoCursos.iterator();
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()){
            Map.Entry<String,ArrayList<Curso>> curso = it.next();
            int total = totalCursosEn(curso.getKey());
            sb.append(curso.getKey().toUpperCase() + "("+ total +")\n");
            ArrayList<Curso> totalCursos = new ArrayList<Curso>();
            totalCursos.addAll(curso.getValue());
            for(Curso tipoCurso:totalCursos){
                sb.append(tipoCurso.toString());
            }
        }
        return sb.toString();
    }

    /**
     * Mostrar la plataforma
     */
    public void escribir() {
        System.out.println(this.toString());
    }

    /**
     *  Lee de un fichero de texto la información de los cursos
     *  En cada línea del fichero se guarda la información de un curso
     *  con el formato "categoria:nombre:fecha publicacion:nivel"
     *  
     */
    public void leerDeFichero() {
        Scanner sc = new Scanner(
                this.getClass().getResourceAsStream("/cursos.csv"));
        while (sc.hasNextLine())  {
            String lineaCurso = sc.nextLine().trim();
            int p = lineaCurso.indexOf(SEPARADOR);
            String categoria = lineaCurso.substring(0, p).trim();
            Curso curso = obtenerCurso(lineaCurso.substring(p + 1));
            this.addCurso(categoria, curso);
        }
    }

    /**
     *  Dado un String con los datos de un curso
     *  obtiene y devuelve un objeto Curso
     *
     *  Ej. a partir de  "sql essential training: 3/12/2019 : principiante " 
     *  obtiene el objeto Curso correspondiente
     *  
     *  Asumimos todos los valores correctos aunque puede haber 
     *  espacios antes y después de cada dato
     */
    public Curso obtenerCurso(String lineaCurso) {
        String nombre = "";
        Nivel estado;
        String[]tokens = lineaCurso.split(":");
        nombre = tokens[0].trim();
        String nivel = tokens[2].trim().toUpperCase();
        estado = Nivel.valueOf(nivel);
        String [] fechasSeparadas = tokens[1].trim().split("/");
        LocalDate fecha = LocalDate.of(Integer.parseInt(fechasSeparadas[2]),Integer.parseInt(fechasSeparadas[1]),Integer.parseInt(fechasSeparadas[0]));
        return new Curso(nombre,fecha,estado);
    }

    /**
     * devuelve un nuevo conjunto con los nombres de todas las categorías  
     *  
     */
    public TreeSet<String> obtenerCategorias() {
        TreeSet<String> categoriasTot = new TreeSet<>();
        Set<String>conjuntoCategorias = plataforma.keySet();
        for(String categorias:conjuntoCategorias){
            categoriasTot.add(categorias.toUpperCase().trim());
        }
        return categoriasTot;
    }

    /**
     * borra de la plataforma los cursos de la categoría y nivel indicados
     * Se devuelve un conjunto (importa el orden) con los nombres de los cursos borrados 
     * 
     * Asumimos que existe la categoría
     *  
     */

    public TreeSet<String> borrarCursosDe(String categoria, Nivel nivel) {
        TreeSet<String> borrados = new TreeSet<>();
        ArrayList<Curso>paEliminar = new ArrayList<>();
        ArrayList<Curso> cursos = new ArrayList<>(plataforma.get(categoria));
        for(Curso curso:cursos){
            if(curso.getNivel().equals(nivel)){
                borrados.add(curso.getNombre());
                paEliminar.add(curso);

            }
        }
        plataforma.get(categoria).removeAll(paEliminar);
        return borrados;
    }

    /**
     *   Devuelve el nombre del curso más antiguo en la
     *   plataforma (el primero publicado)
     */

    public String cursoMasAntiguo() {
        String nombreMasAntiguo = "";        
        LocalDate fechaMasAntigua = LocalDate.MAX;
        Set<String>categorias = plataforma.keySet();
        for(String categoria :categorias){
            ArrayList<Curso> cursos = plataforma.get(categoria);
            for(Curso curso:cursos){
                if(curso.getFecha().compareTo(fechaMasAntigua) < 0){
                    fechaMasAntigua = curso.getFecha();
                    nombreMasAntiguo = curso.getNombre();
                }
            }
        }
        return nombreMasAntiguo;
    }

    /**
     *  
     */
    public static void main(String[] args) {

        PlataformaCursos plataforma = new PlataformaCursos();
        plataforma.leerDeFichero();
        plataforma.escribir();

        System.out.println(
            "Curso más antiguo: " + plataforma.cursoMasAntiguo()
            + "\n");

        String categoria = "bases de datos";
        Nivel nivel = Nivel.AVANZADO;
        System.out.println("------------------");
        System.out.println(
            "Borrando cursos de " + categoria.toUpperCase()
            + " y nivel "
            + nivel);
        TreeSet<String> borrados = plataforma.borrarCursosDe(categoria, nivel);

        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        categoria = "cms";
        nivel = Nivel.INTERMEDIO;
        System.out.println(
            "Borrando cursos de " + categoria.toUpperCase()
            + " y nivel "
            + nivel);
        borrados = plataforma.borrarCursosDe(categoria, nivel);
        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        System.out.println("------------------\n");
        System.out.println(
            "Después de borrar ....");
        plataforma.escribir();
        System.out.print(plataforma.obtenerCategorias());
    }
}


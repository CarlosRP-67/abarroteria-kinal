package main.java.com.programadoreschidos.abarroteria.kinal.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Asigna un ícono (emoji) a cada producto según palabras clave en su nombre.
 * No requiere imágenes reales: es un mapeo simple nombre -> ícono genérico.
 *
 * @author informatica
 */
public class IconoProductoUtil {

    private static final Map<String, String> ICONOS_POR_PALABRA_CLAVE = new LinkedHashMap<>();

    static {
        ICONOS_POR_PALABRA_CLAVE.put("leche", "\uD83E\uDD5B");
        ICONOS_POR_PALABRA_CLAVE.put("yogur", "\uD83E\uDD5B");
        ICONOS_POR_PALABRA_CLAVE.put("queso", "\uD83E\uDDC0");
        ICONOS_POR_PALABRA_CLAVE.put("mantequilla", "\uD83E\uDDC8");
        ICONOS_POR_PALABRA_CLAVE.put("huevo", "\uD83E\uDD5A");
        ICONOS_POR_PALABRA_CLAVE.put("pan", "\uD83C\uDF5E");
        ICONOS_POR_PALABRA_CLAVE.put("harina", "\uD83C\uDF3E");
        ICONOS_POR_PALABRA_CLAVE.put("azucar", "\uD83C\uDF6C");
        ICONOS_POR_PALABRA_CLAVE.put("sal", "\uD83E\uDDC2");
        ICONOS_POR_PALABRA_CLAVE.put("salsa", "\uD83C\uDF45");
        ICONOS_POR_PALABRA_CLAVE.put("tomate", "\uD83C\uDF45");
        ICONOS_POR_PALABRA_CLAVE.put("mayonesa", "\uD83E\uDD6B");
        ICONOS_POR_PALABRA_CLAVE.put("mostaza", "\uD83E\uDD6B");
        ICONOS_POR_PALABRA_CLAVE.put("atun", "\uD83D\uDC1F");
        ICONOS_POR_PALABRA_CLAVE.put("pescado", "\uD83D\uDC1F");
        ICONOS_POR_PALABRA_CLAVE.put("pollo", "\uD83C\uDF57");
        ICONOS_POR_PALABRA_CLAVE.put("carne", "\uD83E\uDD69");
        ICONOS_POR_PALABRA_CLAVE.put("jugo", "\uD83E\uDDC3");
        ICONOS_POR_PALABRA_CLAVE.put("agua", "\uD83D\uDCA7");
        ICONOS_POR_PALABRA_CLAVE.put("gaseosa", "\uD83E\uDD64");
        ICONOS_POR_PALABRA_CLAVE.put("cafe", "\u2615");
        ICONOS_POR_PALABRA_CLAVE.put("galleta", "\uD83C\uDF6A");
        ICONOS_POR_PALABRA_CLAVE.put("sopa", "\uD83C\uDF72");
        ICONOS_POR_PALABRA_CLAVE.put("fideo", "\uD83C\uDF5D");
        ICONOS_POR_PALABRA_CLAVE.put("pasta", "\uD83C\uDF5D");
        ICONOS_POR_PALABRA_CLAVE.put("arroz", "\uD83C\uDF5A");
        ICONOS_POR_PALABRA_CLAVE.put("frijol", "\uD83E\uDED8");
        ICONOS_POR_PALABRA_CLAVE.put("manzana", "\uD83C\uDF4E");
        ICONOS_POR_PALABRA_CLAVE.put("banano", "\uD83C\uDF4C");
        ICONOS_POR_PALABRA_CLAVE.put("naranja", "\uD83C\uDF4A");
        ICONOS_POR_PALABRA_CLAVE.put("limon", "\uD83C\uDF4B");
        ICONOS_POR_PALABRA_CLAVE.put("coliflor", "\uD83E\uDD66");
        ICONOS_POR_PALABRA_CLAVE.put("verdura", "\uD83E\uDD66");
        ICONOS_POR_PALABRA_CLAVE.put("cebolla", "\uD83E\uDDC5");
        ICONOS_POR_PALABRA_CLAVE.put("ajo", "\uD83E\uDDC4");
        ICONOS_POR_PALABRA_CLAVE.put("jabon", "\uD83E\uDDFC");
        ICONOS_POR_PALABRA_CLAVE.put("detergente", "\uD83E\uDDF4");
        ICONOS_POR_PALABRA_CLAVE.put("papel", "\uD83E\uDDFB");
        ICONOS_POR_PALABRA_CLAVE.put("chocolate", "\uD83C\uDF6B");
        ICONOS_POR_PALABRA_CLAVE.put("dulce", "\uD83C\uDF6C");
        ICONOS_POR_PALABRA_CLAVE.put("cerveza", "\uD83C\uDF7A");
    }

    private static final String ICONO_POR_DEFECTO = "\uD83D\uDED2"; // carrito de compras

    private IconoProductoUtil() {
    }

    public static String obtenerIcono(String nombreProducto) {
        if (nombreProducto == null) {
            return ICONO_POR_DEFECTO;
        }
        String nombreNormalizado = nombreProducto.toLowerCase();

        for (Map.Entry<String, String> entry : ICONOS_POR_PALABRA_CLAVE.entrySet()) {
            if (nombreNormalizado.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return ICONO_POR_DEFECTO;
    }
}
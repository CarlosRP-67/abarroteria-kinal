package main.java.com.programadoreschidos.abarroteria.kinal.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import main.java.com.programadoreschidos.abarroteria.kinal.model.Factura;
import main.java.com.programadoreschidos.abarroteria.kinal.model.ItemCarrito;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 * Genera el PDF de una factura ya guardada en la base de datos y lo abre
 * con el visor de PDF por defecto del sistema, para que el usuario pueda
 * revisarlo o imprimirlo desde ahí.
 *
 * Requiere agregar la librería PDFBox al proyecto (ver instrucciones aparte).
 *
 * @author informatica
 */
public class FacturaPdfGenerator {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String CARPETA_SALIDA = "facturas_pdf";

    private FacturaPdfGenerator() {
    }

    public static File generarYAbrirPdf(Factura factura, List<ItemCarrito> items) throws IOException {
        File carpeta = new File(CARPETA_SALIDA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        File archivo = new File(carpeta, factura.getIdFactura() + ".pdf");

        try (PDDocument documento = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.LETTER);
            documento.addPage(pagina);

            try (PDPageContentStream contenido = new PDPageContentStream(documento, pagina)) {
                float margenIzquierdo = 50;
                float y = 740;
                float interlineado = 18;

                PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                PDType1Font helveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

                contenido.setFont(helveticaBold, 16);
                contenido.beginText();
                contenido.newLineAtOffset(margenIzquierdo, y);
                contenido.showText("Abarroteria Kinal");
                contenido.endText();
                y -= interlineado * 1.5f;

                contenido.setFont(helvetica, 11);
                y = escribirLinea(contenido, margenIzquierdo, y, "Factura: " + factura.getIdFactura(), interlineado);
                y = escribirLinea(contenido, margenIzquierdo, y, "Cliente: " + factura.getIdCliente(), interlineado);
                y = escribirLinea(contenido, margenIzquierdo, y, "Fecha: " + factura.getFecha().format(FORMATO_FECHA), interlineado);
                y -= interlineado / 2f;

                contenido.setFont(helveticaBold, 11);
                y = escribirLinea(contenido, margenIzquierdo, y,
                        String.format("%-28s %8s %10s %10s", "Producto", "Cant.", "Precio", "Subtotal"), interlineado);
                contenido.setFont(helvetica, 11);

                for (ItemCarrito item : items) {
                    String linea = String.format("%-28s %8d %10s %10s",
                            recortar(item.getProducto().getNombreProducto(), 28),
                            item.getCantidad(),
                            "Q" + item.getProducto().getPrecio().toPlainString(),
                            "Q" + item.getSubtotal().toPlainString());
                    y = escribirLinea(contenido, margenIzquierdo, y, linea, interlineado);
                }

                y -= interlineado / 2f;
                contenido.setFont(helveticaBold, 13);
                escribirLinea(contenido, margenIzquierdo, y, "TOTAL: Q" + factura.getMonto().toPlainString(), interlineado);
            }

            documento.save(archivo);
        }

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(archivo);
        }

        return archivo;
    }

    private static float escribirLinea(PDPageContentStream contenido, float x, float y, String texto, float interlineado) throws IOException {
        contenido.beginText();
        contenido.newLineAtOffset(x, y);
        contenido.showText(texto);
        contenido.endText();
        return y - interlineado;
    }

    private static String recortar(String texto, int maxLargo) {
        return texto.length() > maxLargo ? texto.substring(0, maxLargo) : texto;
    }
}
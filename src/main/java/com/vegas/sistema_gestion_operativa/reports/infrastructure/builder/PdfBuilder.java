package com.vegas.sistema_gestion_operativa.reports.infrastructure.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vegas.sistema_gestion_operativa.reports.domain.builder.IPdfBuilder;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Builder para crear documentos PDF de manera sencilla.
 */
@Component
public class PdfBuilder implements IPdfBuilder {

    private Document document;
    private ByteArrayOutputStream outputStream;
    private String font = FontFactory.HELVETICA;

    public PdfBuilder() {
    }

    /**
     * Inicializa el builder y prepara el documento PDF.
     *
     * @return
     */
    @Override
    public IPdfBuilder init() {
        this.outputStream = new ByteArrayOutputStream();
        this.document = new Document();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        return this;
    }

    /**
     * Establece la fuente predeterminada para el documento.
     *
     * @param font Nombre de la fuente (por ejemplo, FontFactory.HELVETICA)
     * @return El mismo PdfBuilder para encadenar llamadas.
     */
    @Override
    public IPdfBuilder setFont(String font) {
        this.font = font;
        return this;
    }

    /**
     * Agrega un título al documento.
     *
     * @param text Texto del título.
     * @return El mismo PdfBuilder para encadenar llamadas.
     * @throws DocumentException Si ocurre un error al agregar el título.
     */
    @Override
    public IPdfBuilder addTitle(String text) {
        Font titleFont = FontFactory.getFont(this.font, 22, Font.BOLD);
        Paragraph paragraph = new Paragraph(text, titleFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(6);
        paragraph.setSpacingBefore(12);
        document.add(paragraph);
        return this;
    }

    @Override
    public IPdfBuilder addSubtitle(String text) {
        Font subtitleFont = FontFactory.getFont(this.font, 16);
        Paragraph paragraph = new Paragraph(text, subtitleFont);
        paragraph.setSpacingAfter(4);
        paragraph.setSpacingBefore(8);
        document.add(paragraph);
        return this;
    }

    /**
     * Agrega un párrafo al documento.
     *
     * @param text Texto del párrafo.
     * @return El mismo PdfBuilder para encadenar llamadas.
     */
    @Override
    public IPdfBuilder addParagraph(String text) {
        Font paragraphFont = FontFactory.getFont(this.font, 12);
        document.add(new Paragraph(text, paragraphFont));
        return this;
    }

    /**
     * Agrega una etiqueta y su descripción en el mismo párrafo.
     *
     * @param label       Etiqueta (por ejemplo, "Nombre").
     * @param description Descripción asociada a la etiqueta.
     * @return El mismo PdfBuilder para encadenar llamadas.
     */
    @Override
    public IPdfBuilder addLabelAndDescription(String label, String description) {
        Font labelFont = FontFactory.getFont(this.font, 12, Font.BOLD);
        Font descriptionFont = FontFactory.getFont(this.font, 12);
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(label + ": ", labelFont));
        paragraph.add(new Chunk(description, descriptionFont));
        document.add(paragraph);
        return this;
    }

    /**
     * Agrega espacio en blanco al documento.
     *
     * @param lines Número de líneas de espacio en blanco a agregar.
     * @return El mismo PdfBuilder para encadenar llamadas.
     */
    @Override
    public IPdfBuilder addSpace(int lines) {
        for (int i = 0; i < lines; i++) {
            document.add(new Paragraph(" "));
        }
        return this;
    }


    /**
     * Agrega una tabla al documento.
     *
     * @param table Tabla PDF a agregar.
     * @return El mismo PdfBuilder para encadenar llamadas.
     */
    @Override
    public IPdfBuilder addTable(PdfPTable table) {
        document.add(table);
        return this;
    }

    /**
     * Construye una tabla PDF de manera reutilizable.
     *
     * @param headers      Lista de encabezados de la tabla.
     * @param rows         Lista de filas, donde cada fila es una lista de Strings.
     * @param columnWidths (Opcional) Anchos de columnas. Si es null, usa distribución uniforme.
     * @return PdfPTable generado.
     */
    @Override
    public PdfPTable buildTable(List<String> headers, List<List<String>> rows, float[] columnWidths) {
        PdfPTable table = new PdfPTable(headers.size());
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        if (columnWidths != null) {
            try {
                table.setWidths(columnWidths);
            } catch (DocumentException e) {
                // Si falla se ignora y continúa con distribución estándar
            }
        }

        // Fuente para encabezados
        Font headerFont = FontFactory.getFont(this.font, 12, Font.BOLD);

        // Agregar encabezados
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setPadding(6);
            table.addCell(cell);
        }

        // Fuente para contenido
        Font cellFont = FontFactory.getFont(this.font, 11);

        // Agregar filas
        for (List<String> row : rows) {
            for (String col : row) {
                PdfPCell cell = new PdfPCell(new Phrase(col, cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(4);
                table.addCell(cell);
            }
        }

        return table;
    }

    /**
     * Construye el documento PDF y devuelve su contenido como un arreglo de bytes.
     *
     * @return Arreglo de bytes que representa el documento PDF.
     */
    @Override
    public byte[] build() {
        document.close();
        return outputStream.toByteArray();
    }
}

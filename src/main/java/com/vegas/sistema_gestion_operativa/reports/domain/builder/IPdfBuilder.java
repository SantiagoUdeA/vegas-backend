package com.vegas.sistema_gestion_operativa.reports.domain.builder;

import com.lowagie.text.pdf.PdfPTable;

import java.util.List;

/**
 * Interfaz que define el contrato para construir documentos PDF.
 * Esta interfaz actúa como puente entre las entidades de dominio y las implementaciones específicas de generación de PDF.
 */
public interface IPdfBuilder {

    /**
     * Establece la fuente predeterminada para el documento.
     *
     * @param font Nombre de la fuente
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder setFont(String font);

    /**
     * Agrega un título al documento.
     *
     * @param text Texto del título
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addTitle(String text);

    /**
     * Agrega un subtítulo al documento.
     *
     * @param text Texto del subtítulo
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addSubtitle(String text);

    /**
     * Agrega un párrafo al documento.
     *
     * @param text Texto del párrafo
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addParagraph(String text);

    /**
     * Agrega una etiqueta y su descripción en el mismo párrafo.
     *
     * @param label       Etiqueta (por ejemplo, "Nombre")
     * @param description Descripción asociada a la etiqueta
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addLabelAndDescription(String label, String description);

    /**
     * Agrega espacio en blanco al documento.
     *
     * @param lines Número de líneas de espacio en blanco a agregar
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addSpace(int lines);

    /**
     * Agrega una tabla al documento.
     *
     * @param table Tabla PDF a agregar
     * @return El mismo IPdfBuilder para encadenar llamadas
     */
    IPdfBuilder addTable(PdfPTable table);

    /**
     * Construye una tabla PDF de manera reutilizable.
     *
     * @param headers      Lista de encabezados de la tabla
     * @param rows         Lista de filas, donde cada fila es una lista de Strings
     * @param columnWidths (Opcional) Anchos de columnas. Si es null, usa distribución uniforme
     * @return PdfPTable generado
     */
    PdfPTable buildTable(List<String> headers, List<List<String>> rows, float[] columnWidths);

    /**
     * Construye el documento PDF y devuelve su contenido como un arreglo de bytes.
     *
     * @return Arreglo de bytes que representa el documento PDF
     */
    byte[] build();
}


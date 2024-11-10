package pdf

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import spec.ReportInterface
import java.io.FileOutputStream

class PdfReportImpl : ReportInterface {
    override val implName: String = "PDF"

    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?,
        style: List<Pair<String, String>?>?
    ) {
        // Create a new document
        val document = Document()

        try {
            // Initialize PdfWriter
            PdfWriter.getInstance(document, FileOutputStream(destination))

            // Open the document for writing
            document.open()

            // Add title if provided
            title?.let {
                var titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA, 18f))
                if(!style.isNullOrEmpty() ) {
                    for(elemnt in style){
                        if (elemnt != null) {
                            if(elemnt.first == "title"){
                                if(elemnt.second == "i"){
                                    titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 18f))
                                } else if(elemnt.second == "b"){
                                    titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f))
                                } else {
                                    titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18f))
                                }
                            }
                        }
                    }
                }
                titleParagraph.alignment = Element.ALIGN_CENTER
                document.add(titleParagraph)
                document.add(Chunk.NEWLINE)
                // =================================================================
                // Mora da se menja
                // =================================================================

                //val titleFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 18f) // Italic font
                //val titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f))
                //titleParagraph.alignment = Element.ALIGN_CENTER
                //document.add(titleParagraph)
                //document.add(Chunk.NEWLINE)  // Add a new line after the title
            }

            // Create a table based on the number of columns in the data
            val columns = data.keys.toList()
            val numColumns = columns.size
            val table = PdfPTable(numColumns)

            // Add header row if necessary
            if (header) {
                columns.forEach { column ->
                    val cell = PdfPCell(Paragraph(column, FontFactory.getFont(FontFactory.HELVETICA_BOLD)))
                    cell.horizontalAlignment = Element.ALIGN_CENTER
                    table.addCell(cell)
                }
            }

            // Add data rows
            //val numRows = data.values.first().size
            //for (i in 0 until numRows) {
              //  columns.forEach { column ->
                //    val cellData = data[column]?.get(i) ?: ""
                  //  table.addCell(cellData)
                //}
            //}
            // Add data rows
            val numRows = data.values.first().size
            for (i in 0 until numRows) {
                columns.forEach { column ->
                    val cellData = data[column]?.get(i) ?: ""
                    // Create a bold font for the cell data
                    val boldFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE)
                    val cell = PdfPCell(Paragraph(cellData, boldFont))  // Create a bold cell
                    table.addCell(cell)
                }
            }
            // =================================================================
            // Promenio da se sve u koloni predstavi kao italic
            // =================================================================




            // Add the table to the document
            document.add(table)

            // Add summary if provided
            summary?.let {
                document.add(Chunk.NEWLINE)
                var summaryParagraph = Paragraph("Summary: $summary", FontFactory.getFont(FontFactory.HELVETICA))
                if(!style.isNullOrEmpty() ) {
                    for(elemnt in style){
                        if (elemnt != null) {
                            if(elemnt.first == "title"){
                                if(elemnt.second == "i"){
                                    summaryParagraph = Paragraph("Summary: $summary", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE))
                                } else if(elemnt.second == "b"){
                                    summaryParagraph = Paragraph("Summary: $summary", FontFactory.getFont(FontFactory.HELVETICA_BOLD))
                                } else {
                                    summaryParagraph = Paragraph("Summary: $summary", FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE))
                                }
                            }
                        }
                    }
                }
                document.add(summaryParagraph)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the document
            document.close()
        }
    }

}
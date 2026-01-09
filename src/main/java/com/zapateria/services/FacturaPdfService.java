package com.zapateria.services;

import com.zapateria.models.Pedido;
import com.zapateria.models.Pedido.ItemPedido;
import com.zapateria.repositories.PedidoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

@Service
public class FacturaPdfService {

    private final PedidoRepository pedidoRepository;

    public FacturaPdfService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public byte[] generarFacturaPedido(String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, baos);
            doc.open();

            // Fuente básica
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font negrita = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 12, Font.NORMAL);

            // Título
            Paragraph titulo = new Paragraph("FACTURA DE COMPRA", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Zapatería Online", negrita));
            doc.add(new Paragraph("RUC: 9999999999", normal));
            doc.add(new Paragraph("Dirección: Quito - Ecuador", normal));
            doc.add(new Paragraph("Teléfono: 0999999999", normal));
            doc.add(new Paragraph(" "));

            // Datos de la factura
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaStr = pedido.getFecha() != null ? sdf.format(pedido.getFecha()) : "-";

            PdfPTable tablaCabecera = new PdfPTable(2);
            tablaCabecera.setWidthPercentage(100);
            tablaCabecera.setWidths(new float[]{1, 1});

            // Columna izquierda: cliente + seguimiento + pago
            Paragraph clienteInfo = new Paragraph();
            clienteInfo.add(new Chunk("Cliente: ", negrita));
            clienteInfo.add(new Chunk(pedido.getClienteNombre(), normal));
            clienteInfo.add(Chunk.NEWLINE);
            clienteInfo.add(new Chunk("ID Cliente: ", negrita));
            clienteInfo.add(new Chunk(pedido.getClienteId(), normal));
            clienteInfo.add(Chunk.NEWLINE);

            if (pedido.getEmpleadoAsignadoNombre() != null) {
                clienteInfo.add(new Chunk("Atendido por: ", negrita));
                clienteInfo.add(new Chunk(pedido.getEmpleadoAsignadoNombre(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            // ========= SEGUIMIENTO / DESPACHO (COMPLETO) =========
            if (pedido.getDespachoNombre() != null && !pedido.getDespachoNombre().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Despachador: ", negrita));
                clienteInfo.add(new Chunk(pedido.getDespachoNombre(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getCodigoSeguimiento() != null && !pedido.getCodigoSeguimiento().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Código Seguimiento: ", negrita));
                clienteInfo.add(new Chunk(pedido.getCodigoSeguimiento(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getEstadoDespacho() != null && !pedido.getEstadoDespacho().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Estado Despacho: ", negrita));
                clienteInfo.add(new Chunk(pedido.getEstadoDespacho(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getUbicacionActual() != null && !pedido.getUbicacionActual().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Ubicación Actual: ", negrita));
                clienteInfo.add(new Chunk(pedido.getUbicacionActual(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getFechaDespacho() != null) {
                clienteInfo.add(new Chunk("Fecha Despacho: ", negrita));
                clienteInfo.add(new Chunk(sdf.format(pedido.getFechaDespacho()), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getFechaEntrega() != null) {
                clienteInfo.add(new Chunk("Fecha Entrega: ", negrita));
                clienteInfo.add(new Chunk(sdf.format(pedido.getFechaEntrega()), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            // ========= PAGO (METODO + ESTADO + ULTIMOS 4) =========
            if (pedido.getMetodoPago() != null && !pedido.getMetodoPago().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Método de Pago: ", negrita));
                clienteInfo.add(new Chunk(pedido.getMetodoPago(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getEstadoPago() != null && !pedido.getEstadoPago().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Estado de Pago: ", negrita));
                clienteInfo.add(new Chunk(pedido.getEstadoPago(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            if (pedido.getTarjetaUltimos4() != null && !pedido.getTarjetaUltimos4().trim().isEmpty()) {
                clienteInfo.add(new Chunk("Tarjeta (últimos 4): ", negrita));
                clienteInfo.add(new Chunk("**** " + pedido.getTarjetaUltimos4(), normal));
                clienteInfo.add(Chunk.NEWLINE);
            }

            // Columna derecha: datos factura
            Paragraph facturaInfo = new Paragraph();
            facturaInfo.add(new Chunk("Nro. Factura: ", negrita));
            facturaInfo.add(new Chunk(
                    pedido.getNumeroPedido() != null ? pedido.getNumeroPedido() : pedido.getId(), normal));
            facturaInfo.add(Chunk.NEWLINE);
            facturaInfo.add(new Chunk("Fecha: ", negrita));
            facturaInfo.add(new Chunk(fechaStr, normal));
            facturaInfo.add(Chunk.NEWLINE);
            facturaInfo.add(new Chunk("Estado del Pedido: ", negrita));
            facturaInfo.add(new Chunk(pedido.getEstado(), normal));

            tablaCabecera.addCell(clienteInfo);
            tablaCabecera.addCell(facturaInfo);

            doc.add(tablaCabecera);
            doc.add(new Paragraph(" "));

            // Tabla de items
            PdfPTable tablaItems = new PdfPTable(4);
            tablaItems.setWidthPercentage(100);
            tablaItems.setWidths(new float[]{4, 1, 2, 2});

            // Encabezados
            tablaItems.addCell(new Phrase("Producto", negrita));
            tablaItems.addCell(new Phrase("Cant.", negrita));
            tablaItems.addCell(new Phrase("P. Unitario", negrita));
            tablaItems.addCell(new Phrase("Subtotal", negrita));

            double subtotal = 0.0;

            if (pedido.getItems() != null) {
                for (ItemPedido item : pedido.getItems()) {
                    double sub = item.getCantidad() * item.getPrecio();
                    subtotal += sub;

                    tablaItems.addCell(new Phrase(item.getNombreProducto(), normal));
                    tablaItems.addCell(new Phrase(String.valueOf(item.getCantidad()), normal));
                    tablaItems.addCell(new Phrase(String.format("$ %.2f", item.getPrecio()), normal));
                    tablaItems.addCell(new Phrase(String.format("$ %.2f", sub), normal));
                }
            }

            doc.add(tablaItems);
            doc.add(new Paragraph(" "));

            // ========= TOTALES (TOMA DATOS DEL PEDIDO SI YA EXISTEN) =========
            // Si tu controller ya guardó subtotal/iva/envio/totalFinal, se usan esos.
            double ivaPorcentaje = (pedido.getIvaPorcentaje() > 0) ? pedido.getIvaPorcentaje() : 0.15;
            double ivaValor = (pedido.getIvaValor() > 0) ? pedido.getIvaValor() : (subtotal * ivaPorcentaje);

            double envio = (pedido.getEnvio() > 0) ? pedido.getEnvio() : 5.00;

            double totalFinal = (pedido.getTotalFinal() > 0)
                    ? pedido.getTotalFinal()
                    : (subtotal + ivaValor + envio);

            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(45);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setWidths(new float[]{2, 1});

            tablaTotales.addCell(new Phrase("SUBTOTAL:", negrita));
            tablaTotales.addCell(new Phrase(String.format("$ %.2f", subtotal), normal));

            tablaTotales.addCell(new Phrase(String.format("IVA (%.0f%%):", ivaPorcentaje * 100), negrita));
            tablaTotales.addCell(new Phrase(String.format("$ %.2f", ivaValor), normal));

            tablaTotales.addCell(new Phrase("ENVÍO:", negrita));
            tablaTotales.addCell(new Phrase(String.format("$ %.2f", envio), normal));

            tablaTotales.addCell(new Phrase("TOTAL FINAL:", negrita));
            tablaTotales.addCell(new Phrase(String.format("$ %.2f", totalFinal), negrita));

            doc.add(tablaTotales);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Gracias por su compra.", normal));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la factura PDF", e);
        }
    }
}

package com.qsolutions.gpsclient.ui.view;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StatisticsCardView {

    private final HBox root;

    private final Label lblPulsaciones = stat("48",     "-fx-text-fill: -color-text-primary; -fx-font-size: 22px; -fx-font-weight: 500;");
    private final Label lblTasa        = stat("100%",   "-fx-text-fill: -color-accent-success; -fx-font-size: 22px; -fx-font-weight: 500;");
    private final Label lblErrores     = stat("0",      "-fx-text-fill: -color-text-primary; -fx-font-size: 22px; -fx-font-weight: 500;");
    private final Label lblUptime      = stat("8h 32m", "-fx-text-fill: -color-text-primary; -fx-font-size: 22px; -fx-font-weight: 500;");

    public StatisticsCardView() {
        root = new HBox(24,
                miniCard("Pulsaciones hoy", lblPulsaciones),
                separator(),
                miniCard("Tasa de éxito",   lblTasa),
                separator(),
                miniCard("Errores",         lblErrores),
                separator(),
                miniCard("Uptime",          lblUptime)
        );
        root.setAlignment(Pos.CENTER_LEFT);
    }

    public HBox getRoot() { return root; }

    public void actualizarPulsaciones(int total)        { lblPulsaciones.setText(String.valueOf(total)); }
    public void actualizarTasaExito(double porcentaje)  { lblTasa.setText(String.format("%.0f%%", porcentaje)); }
    public void actualizarErrores(int errores)          { lblErrores.setText(String.valueOf(errores)); }
    public void actualizarUptime(String uptime)         { lblUptime.setText(uptime); }

    private VBox miniCard(String titulo, Label valor) {
        Label lbl = new Label(titulo);
        lbl.getStyleClass().add("label-secondary");
        lbl.setStyle("-fx-font-size: 11px;");
        return new VBox(2, lbl, valor);
    }

    private static Label stat(String text, String style) {
        Label l = new Label(text);
        l.setStyle(style);
        return l;
    }

    private static Separator separator() {
        Separator s = new Separator(Orientation.VERTICAL);
        s.setStyle("-fx-pref-height: 36px;");
        return s;
    }
}

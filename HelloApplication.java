package com.example.practica3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {

    private Juego juego;
    private Canvas tableroCanvas;
    private Image[] imagenesDados;
    private Label ronda;
    private Button btnIniciar, btnLanzar, btnMover;

    @Override
    public void start(Stage stage) {
        juego = new Juego();
        cargarDados();

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #c9a97a;");

        // Header
        Label header = new Label("THE DICE GAME");
        header.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        header.setStyle("-fx-background-color: #8b5e3c; -fx-text-fill: #fff8f0; -fx-padding: 10 0;");
        header.setMaxWidth(Double.MAX_VALUE);
        header.setAlignment(Pos.CENTER);

        // Contenido
        HBox contenido = new HBox(8);
        contenido.setPadding(new Insets(8, 8, 8, 8));

        tableroCanvas = new Canvas(720, 390);
        dibujarTableroVacio();

        VBox panelDer = crearPanel();

        contenido.getChildren().addAll(tableroCanvas, panelDer);
        root.getChildren().addAll(header, contenido);

        Scene scene = new Scene(root, 910, 460);
        stage.setTitle("The Dice Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //imagenes dados
    private void cargarDados() {
        imagenesDados = new Image[6];
        for (int i = 1; i <= 6; i++) {
            try {
                imagenesDados[i - 1] = new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/dados/" + i + ".png")));
            } catch (Exception e) {
                imagenesDados[i - 1] = null;
            }
        }
    }

    private Image getImg(int v) {
        if (v >= 1 && v <= 6 && imagenesDados[v - 1] != null) return imagenesDados[v - 1];
        return null;
    }

    // ── Panel derecho ──────────────────────────────────────────────────────────
    private VBox crearPanel() {
        VBox p = new VBox(12);
        p.setPrefWidth(165);
        p.setAlignment(Pos.TOP_CENTER);

        btnIniciar = boton("Iniciar", "#5a3e28");
        btnLanzar  = boton("Lanzar",  "#7a1a00");
        btnMover   = boton("Mover",   "#2d5a1a");

        btnLanzar.setDisable(true);
        btnMover.setDisable(true);

        btnIniciar.setOnAction(e -> accionIniciar());
        btnLanzar.setOnAction(e  -> accionLanzar());
        btnMover.setOnAction(e   -> accionMover());

        Label titRonda = new Label("RONDAS");
        titRonda.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        titRonda.setStyle("-fx-text-fill: #3b2410;");

        ronda = new Label("0");
        ronda.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        ronda.setStyle("-fx-text-fill: #5a2800;");

        p.getChildren().addAll(btnIniciar, btnLanzar, btnMover, titRonda, ronda);
        return p;
    }

    private Button boton(String texto, String bg) {
        Button b = new Button(texto);
        b.setPrefWidth(140);
        b.setStyle(
                "-fx-background-color: " + bg + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 16; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand;"
        );
        b.disabledProperty().addListener((ob, o, dis) -> {
            if (dis)
                b.setStyle("-fx-background-color: #aaa090; -fx-text-fill: #d0c8b8; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-padding: 8 16; -fx-background-radius: 6;");
            else
                b.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: white; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        });
        return b;
    }

    private void accionIniciar() {
        juego.iniciar();
        btnLanzar.setDisable(false);
        btnMover.setDisable(true);
        ronda.setText("0");
        dibujarTablero();
    }

    private void accionLanzar() {
        if (!juego.isIniciado()) return;
        juego.lanzarDados();
        dibujarTablero();
        btnLanzar.setDisable(true);
        btnMover.setDisable(false);
    }

    private void accionMover() {
        if (!juego.isIniciado()) return;
        juego.moverRecursos();
        int ronda = juego.getRonda();
        this.ronda.setText(String.valueOf(ronda));
        dibujarTablero();

        if (ronda >= 20) {
            btnLanzar.setDisable(true);
            btnMover.setDisable(true);
            mostrarAlertaFinal();
        } else {
            btnLanzar.setDisable(false);
            btnMover.setDisable(true);
        }
    }

    private void mostrarAlertaFinal() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin del juego");
        alert.setHeaderText("20 rondas completadas");
        alert.setContentText(
                "Procesados: " + juego.getSalidaRecursos().size() +
                        "\nEn sistema: " + juego.getTotalEnSistema() +
                        "\n\n¿Otra partida?"
        );
        alert.showAndWait();
        accionIniciar();
    }

    private void dibujarTableroVacio() {
        GraphicsContext gc = tableroCanvas.getGraphicsContext2D();
        double w = tableroCanvas.getWidth(), h = tableroCanvas.getHeight();
        gc.setFill(Color.web("#e8d5b7"));
        gc.fillRect(0, 0, w, h);
        gc.setFill(Color.web("#9b6a3a"));
        gc.setFont(Font.font("Georgia", 14));
        gc.fillText("Presiona Iniciar para comenzar", 20, 30);
    }

    private void dibujarTablero() {
        if (!juego.isIniciado()) { dibujarTableroVacio(); return; }

        GraphicsContext gc = tableroCanvas.getGraphicsContext2D();
        double w = tableroCanvas.getWidth(), h = tableroCanvas.getHeight();

        gc.setFill(Color.web("#e8d5b7"));
        gc.fillRect(0, 0, w, h);

        Estacion[] ests  = juego.getEstaciones();
        int[]      dados = juego.getUltimosValoresDados();

        double margen = 10;
        double paso   = (w - margen * 2) / 5;
        double yTop   = 110;
        double yBot   = 290;
        double cW     = paso - 10;
        double cH     = 70;

        for (int i = 0; i < 5; i++) {
            double cx = margen + i * paso + paso / 2;
            dibujarEstacion(gc, ests[i],     dados[i],     cx, yTop, cW, cH);
            dibujarEstacion(gc, ests[5 + i], dados[5 + i], cx, yBot, cW, cH);
        }
        gc.setFill(Color.web("#c4924a"));
        gc.fillRect(0, h - 28, w, 28);
        gc.setFill(Color.web("#3b1a00"));
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        gc.fillText("Ronda " + juego.getRonda()
                        + "   |   Procesados: " + juego.getSalidaRecursos().size()
                        + "   |   En sistema: " + juego.getTotalEnSistema(),
                14, h - 10);
    }

    private void dibujarEstacion(GraphicsContext gc, Estacion est, int valorDado, double cx, double cy, double cW, double cH) {
        double x0 = cx - cW / 2, y0 = cy - cH / 2;

        //rectangulo grande estacion
        gc.setFill(Color.web("#ddc49a"));
        //rectangulo
        gc.fillRoundRect(x0, y0, cW, cH, 8, 8);
        gc.setStroke(Color.web("#9b6a3a")); gc.setLineWidth(1.2);

        gc.setFill(Color.web("#6a3a10"));
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 9));
        //identificador de c/u estacion
        gc.fillText("E" + est.getId(), x0 + 3, y0 + 10);
        //ds size dado
        double ds = 28, dx = x0 + 3, dy = y0 + 12;
        if (valorDado >= 1 && valorDado <= 6) {
            Image img = getImg(valorDado);
            if (img != null) gc.drawImage(img, dx, dy, ds, ds);
            gc.setFill(Color.web("#5a2a00"));
            gc.setFont(Font.font("Georgia", 9));
        } else {
            gc.setFill(Color.web("#d4a96a"));
            gc.fillRoundRect(dx, dy, ds, ds, 4, 4);
            gc.setFill(Color.web("#9b6a3a"));
            gc.setFont(Font.font("Georgia", 9));
            gc.fillText("?", dx + 9, dy + 18);
        }
        //bolitas recursos
        int en = est.getCantidadEnCola();
        int maxM = 4;
        int limite;

        //det. cantidad de circulos a dibujar
        if (en < maxM) {
            limite = en;
        } else {
            limite = maxM;
        }
        //rc radio sepc separacion
        double rC = 4.5, sepC = 11;
        double iniX = x0 + 36, circY = cy + 8;
        for (int j = 0; j < limite; j++) {
            double xc = iniX + j * sepC;
            gc.setFill(Color.web("#8b2500"));
            gc.fillOval(xc - rC, circY - rC, rC * 2, rC * 2);
            gc.setStroke(Color.web("#c04020")); gc.setLineWidth(0.7);
            gc.strokeOval(xc - rC, circY - rC, rC * 2, rC * 2);
        }
        //indicador de bolitas extras que no caben
        if (en > maxM) {
            gc.setFill(Color.web("#7a3a10"));
            gc.setFont(Font.font("Georgia", 9));
            gc.fillText("+" + (en - maxM), iniX + maxM * sepC + 2, circY + 4);
        }
        //total bolitas en cola en la parte inferiror derecha
        gc.setFill(Color.web("#7a4a20"));
        gc.setFont(Font.font("Georgia", 9));
        gc.fillText("(" + en + ")", x0 + cW - 20, y0 + cH - 3);
    }

    public static void main(String[] args) { launch(); }
}
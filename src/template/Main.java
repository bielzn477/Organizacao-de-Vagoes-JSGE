package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;
import java.util.Random;

public class Main extends EngineFrame {

    private Deque<Integer> trem;
    private int[] chegada;
    private int[] objetivo;
    private Random random;
    private int proximo;
    private boolean[] colocadoNaFrente;
    private boolean terminou;
    private String mensagem;

    public Main() {
        
        super(800, 500, "Organizacao de Vagoes", 60,
                true, false, false, false, false, false);
    }

    @Override
    public void create() {
        
        trem = new ArrayDeque<>();
        random = new Random();
        reiniciar();
        
    }

    private void reiniciar() {
        
        trem.clear();
        sortearObjetivo();
        sortearChegada();
        proximo = 0;
        colocadoNaFrente = new boolean[6];
        terminou = false;
        mensagem = "Monte o trem na ordem do objetivo.";
        
    }

    private void sortearObjetivo() {
        
        int[] anterior = objetivo;
        do {
            objetivo = new int[]{ 1, 2, 3, 4, 5, 6 };
            for ( int i = 5; i > 0; i-- ) {
                int j = random.nextInt(i + 1);
                int temporario = objetivo[i];
                objetivo[i] = objetivo[j];
                objetivo[j] = temporario;
            }
        } while ( Arrays.equals(objetivo, anterior) );
    }

    private void sortearChegada() {
        
        int[] anterior = chegada;
        do {
            chegada = new int[6];
            Deque<Integer> numeros = new ArrayDeque<>();
            for (int numero : objetivo) {
                numeros.addLast(numero);
            }
            for (int i = 5; i >= 0; i--) {
                if (random.nextBoolean()) {
                    chegada[i] = numeros.removeFirst();
                } else {
                    chegada[i] = numeros.removeLast();
                }
            }
        } while (Arrays.equals(chegada, anterior));
    }

    @Override
    public void update( double delta ) {
        
        if ( isKeyPressed( KEY_R ) ) {
            reiniciar();
        
            return;
        }
        if ( isKeyPressed( KEY_BACKSPACE ) ) {
            if (proximo > 0) {
                proximo--;
                if (colocadoNaFrente[proximo]) {
                    trem.removeFirst();
                } else {
                    trem.removeLast();
                }
                terminou = false;
                mensagem = "Jogada desfeita. Escolha a ponta novamente.";
            }
            return;
        }
        if (terminou) {
            return;
        }

        if ( isKeyPressed( KEY_LEFT ) ) {
            colocadoNaFrente[proximo] = true;
            trem.addFirst(chegada[proximo]);
            proximo++;
        } else if ( isKeyPressed( KEY_RIGHT ) ) {
            colocadoNaFrente[proximo] = false;
            trem.addLast(chegada[proximo]);
            proximo++;
        }

        if (proximo == chegada.length) {
            verificarOrdem();
        }
    }

    private void verificarOrdem() {
        
        int indice = 0;
        boolean correto = true;
        for ( int vagao : trem ) {
            if ( vagao != objetivo[indice] ) {
                correto = false;
            }
            indice++;
        }
        terminou = true;
        if ( correto ) {
            mensagem = "Voce conseguiu! R para jogar novamente.";
        } else {
            mensagem = "A ordem ficou errada. R para tentar novamente.";
        }
    }

    @Override
    public void draw() {
        
        clearBackground(new Color(220, 220, 220));
        drawText( "ORGANIZACAO DE VAGOES", 200, 30, 25, BLACK );
        String textoObjetivo = "Objetivo: ";
        for (int numero : objetivo) {
            textoObjetivo += numero + "  ";
        }
        drawText( textoObjetivo, 245, 80, 22, BLACK );

        if (!terminou) {
            drawText( "Proximo vagao:", 300, 125, 20, BLACK );
            desenharVagao( chegada[proximo], 350, 160 );
        }

        fillRectangle( 60, 325, 680, 5, DARKGRAY );
        fillRectangle( 60, 345, 680, 5, DARKGRAY );
        for (int x = 70; x < 740; x += 30) {
            fillRectangle( x, 321, 8, 34, DARKGRAY );
        }

        drawText( "FRENTE", 65, 250, 18, BLACK );
        drawText( "FIM", 650, 250, 18, BLACK );
        int x = 70;
        for (int vagao : trem) {
            desenharVagao( vagao, x, 280 );
            x += 110;
        }

        drawText( mensagem, 65, 385, 19, BLACK );
        drawText( "ESQUERDA: colocar na frente", 65, 425, 18, BLACK );
        drawText( "DIREITA: colocar no fim", 435, 425, 18, BLACK );
        drawText( "R: reiniciar", 65, 465, 18, BLACK );
        drawText( "BACKSPACE: desfazer", 435, 465, 18, BLACK );
    }

    private void desenharVagao( int numero, int x, int y ) {
        
        fillRectangle( x, y, 90, 42, BLUE );
        drawText( "" + numero, x + 37, y + 9, 24, WHITE );
        fillCircle( x + 18, y + 44, 8, BLACK );
        fillCircle( x + 72, y + 44, 8, BLACK );
        fillRectangle( x + 90, y + 28, 20, 5, BLACK );
        
    }

    public static void main( String[] args ) {
        
        new Main();
    }
}

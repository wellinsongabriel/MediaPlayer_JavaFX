package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {

	@FXML
	private MediaView mediaview;

	@FXML
	private AnchorPane telaApp;
	@FXML
	private Slider tempoMusica;
	@FXML
	private Slider seletorVolume;
	@FXML
	private Label tempoAtual;
	@FXML
	private Label tempoTotal;
	@FXML
	private Label nomeMusica;
	@FXML
	private ImageView audio;

	private MediaPlayer mediaPlayer;
	private List<String> musicas;
	private int indiceMusicaAtual;
	private double volume = 30;

	public void initialize() {
		
		seletorVolume.setValue(volume);

		carregarMusicas();
		
		formartarNomeMusica();
		
		movimentarNomeMusica();
		
		definirTempo();
		
		ajustarVolume();

	}

	private void definirTempo() {
		
		mediaPlayer.setOnReady(() -> {
			Duration tempoTotalMusica = mediaPlayer.getTotalDuration();
			int totalSegundos = (int) tempoTotalMusica.toSeconds();
			int minutos = totalSegundos / 60;
			int segundos = totalSegundos % 60;
			String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
			tempoTotal.setText(tempoFormatado);
		});
		
		tempoMusica.valueProperty().addListener((obs, valorAntigo, valorNovo) ->{
			if(tempoMusica.isValueChanging()) {
				double posicao = valorNovo.doubleValue() / 100;
				alterarTempoMusica(posicao);
			}
		});
		
		mediaPlayer.currentTimeProperty().addListener((obs, valorAntigo, valorNovo) ->{
			Duration tempo = mediaPlayer.getCurrentTime();
			int segundosAtual = (int) tempo.toSeconds();
			int minutos = segundosAtual / 60;
			int segundos = segundosAtual % 60;
			String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
			tempoAtual.setText(tempoFormatado);
			
			if(!tempoMusica.isValueChanging()) {
				tempoMusica.setValue(valorNovo.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 100);
			}
		});
		
		mediaPlayer.setOnEndOfMedia(() ->{
			proxima();
		});
		
	}

	private void alterarTempoMusica(double posicao) {
		Duration duracao = mediaPlayer.getTotalDuration();
		Duration novaDuracao = duracao.multiply(posicao);
		
		mediaPlayer.seek(novaDuracao);
		
	}

	private void movimentarNomeMusica() {
		TranslateTransition transition = new TranslateTransition(Duration.seconds(30),nomeMusica);
		
		transition.setFromX(350);
		transition.setToX(-670);
		
		transition.setCycleCount(TranslateTransition.INDEFINITE);
		
		transition.play();
		
	}

	private void formartarNomeMusica() {
		String nome = musicas.get(indiceMusicaAtual);
		nomeMusica.setText(nome.replace("music/", ""));
		
	}

	private void carregarMusicas() {
		// carregar as musicas
		musicas = new ArrayList<>();
		musicas.add("music/I Feel It All So Deeply - Bail Bonds.mp3");
		musicas.add("music/Select - Patrick Patrikios.mp3");
		indiceMusicaAtual = 0;
		String musicaAtual = musicas.get(indiceMusicaAtual);

		Media media = new Media(new File(musicaAtual).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaview.setMediaPlayer(mediaPlayer);
		mediaPlayer.setVolume(volume);
	}

	public void minimizar() {
		((Stage) telaApp.getScene().getWindow()).toBack();
	}

	public void fechar() {
		System.exit(0);
	}

	public void anterior() {
		stop();

		indiceMusicaAtual--;
		if (indiceMusicaAtual < 0) {
			indiceMusicaAtual = musicas.size() - 1;
		}

		alterarMusicaAtual();
	}

	private void alterarMusicaAtual() {
		String musicaAtual = musicas.get(indiceMusicaAtual);

		Media media = new Media(new File(musicaAtual).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaview.setMediaPlayer(mediaPlayer);
		
		definirTempo();
		formartarNomeMusica();
		play();

	}

	public void play() {
		mediaPlayer.play();
		verificarMudo();

	}

	public void pause() {
		mediaPlayer.pause();

	}

	public void stop() {
		mediaPlayer.stop();

	}

	public void proxima() {
		stop();
		
		indiceMusicaAtual++;
		if (indiceMusicaAtual >= musicas.size()) {
			indiceMusicaAtual = 0;
		}

		alterarMusicaAtual();
		
	}

	public void mudo() {
		if(mediaPlayer.isMute()) {
			mediaPlayer.setMute(false);
			audio.setImage(new Image("audioOn.png"));
			mediaPlayer.setVolume(seletorVolume.getValue());
			volume = seletorVolume.getValue();
		}else {
			mediaPlayer.setMute(true);
			audio.setImage(new Image("audioOff.png"));
			volume = 0;
		}

	}
	
	private void ajustarVolume() {
		
		seletorVolume.valueProperty().addListener((obs, valorAntigo, valorNovo) -> {
			this.volume = valorNovo.doubleValue() / 100;
			mediaPlayer.setVolume(volume);
			
			if(this.volume!=0)
				mediaPlayer.setMute(false);
			
			audio.setImage(volume > 0 ? new Image("audioOn.png") : new Image("audioOff.png"));
		});
	}
	
	private void verificarMudo() {
		
		if(volume==0) {
			mediaPlayer.setMute(true);
			mediaPlayer.setVolume(0);
		}
		
	}

}

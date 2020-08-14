package br.com.laboon.sw.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.laboon.sw.LaboonSW;
import br.com.laboon.sw.state.State;
import br.com.laboon.sw.state.StateEnd;
import br.com.laboon.sw.state.StateGame;
import br.com.laboon.sw.state.StateWaiting;
import lombok.Getter;

@Getter
public class GameManager {

	@Getter
	private List<Player> players;

	private State state;

	public GameManager() {
		players = new ArrayList<>();
		state = new StateWaiting(this);
	}

	/**
	 * Ir para o proximo estagio do jogo
	 */
	public void nextState() {
		state.stopState();
		if (state instanceof StateWaiting)
			state = new StateGame(this);
		else if(state instanceof StateGame)
			state = new StateEnd(this);
	}

	/**
	 * Checar se o jogador esta no jogo
	 * 
	 * @param player jogador selecionado
	 * @return true se estiver no jogo, false se não estiver
	 */
	public boolean inGame(Player player) {
		return players.contains(player);
	}

	/**
	 * Adicionar o jogador no jogo
	 * 
	 * @param player jogador selecionado
	 * @return true se adicionou, false se ja esta no jogo
	 */
	public boolean addPlayer(Player player) {
		if(inGame(player))
			return false;
		players.add(player);
		return true;
	}

	/**
	 * Remover jogador do jogo
	 * 
	 * @param player jogador selecionado
	 * @return true se removeu, false se não estiver na lista
	 */
	public boolean removePlayer(Player player) {
		if(!inGame(player))
			return false;
		players.remove(player);
		return true;
	}
	
	/**
	 * Enviar o jogador selecionado para outra sala
	 * 
	 * @param player o jogador selecionado
	 */
	public void redirectSW(Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("SkyWars");
		player.sendPluginMessage(LaboonSW.getInstance(), "BungeeCord", out.toByteArray());
	}



}

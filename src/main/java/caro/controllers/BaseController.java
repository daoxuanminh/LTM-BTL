package caro.controllers;

import caro.player.Player;
import client.Client;

public abstract class BaseController {
	private Player player;
	private Client client;
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void openModal(String fxmlFileName) {		
		
	    
	}
}

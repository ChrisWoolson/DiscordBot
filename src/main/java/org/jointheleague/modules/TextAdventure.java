package org.jointheleague.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.javacord.api.event.message.MessageCreateEvent;

public class TextAdventure extends CustomMessageCreateListener {
	String[][] cells;
	ArrayList<ArrayList<String>> formats;

	int x = 9;
	int y = 9;
	boolean playing = false;
	boolean materials = false;
	boolean needMaterials = false;
	boolean needLava = false;
	boolean lava = false;
	boolean inHouse = false;

	public TextAdventure(String channelName) {
		super(channelName);
		cells = new String[10][10];

		// Formats represents the formats cells can be stored in.
		// start,m,o,s,g,f,h,end,lo
		formats = new ArrayList<ArrayList<String>>();

		formats.add(new ArrayList<String>(Arrays.asList(
				new String[] { "start", "m", "o", "s", "g", "f", "h", "end", "lo", "1", "2", "3", "4", "5", "6" })));

		formats.add(new ArrayList<String>(Arrays.asList(
				new String[] { "🏁", "🗺", "🌊", "🏜", "🌾", "🌲", "🏚", "⛵", "🌋", "1", "2", "3", "4", "5", "6" })));

		formats.add(new ArrayList<String>(Arrays.asList(new String[] { ":checkered_flag:", ":map:", ":ocean:",
				":desert:", ":ear_of_rice:", ":evergreen_tree:", ":house_abandoned:", ":sailboat:", ":volcano:",
				":skull:", ":skull:", ":skull:", ":skull:", ":skull:", ":skull:" })));
	}

	public void setup() {
		materials = false;
		needMaterials = false;
		needLava = false;
		lava = false;
		inHouse = false;
		
		String[][] map = importMap("src/main/resources/TextAdventureMap.txt");
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.println(map[i][j]);
			}
		}

		x = 9;
		y = 9;
		for (int i = 0; i < 10; i++) {
			cells[0][i] = "o";
		}
		for (int i = 0; i < 10; i++) {
			cells[6][i] = "f";
			cells[7][i] = "f";
			cells[8][i] = "f";
			cells[9][i] = "f";
		}
		cells[1][0] = "o";
		cells[2][0] = "o";
		cells[1][2] = "o";
		cells[1][3] = "o";
		cells[1][0] = "o";
		cells[1][1] = "s";
		cells[2][1] = "s";
		cells[2][2] = "s";
		cells[1][4] = "s";
		cells[1][5] = "s";
		cells[1][7] = "s";
		cells[1][8] = "s";
		cells[1][9] = "s";
		cells[2][3] = "s";
		cells[2][6] = "s";
		cells[3][0] = "s";
		cells[3][1] = "s";
		cells[4][0] = "s";
		cells[5][0] = "s";
		cells[6][0] = "s";
		cells[7][0] = "s";
		cells[2][4] = "g";
		cells[2][4] = "g";
		cells[2][5] = "g";
		cells[2][8] = "g";
		cells[2][9] = "g";
		cells[2][7] = "lo";
		cells[3][2] = "g";
		cells[3][3] = "g";
		cells[3][4] = "g";
		cells[3][5] = "6";
		cells[3][6] = "g";
		cells[3][7] = "g";
		cells[3][8] = "g";
		cells[3][9] = "g";
		cells[4][1] = "g";
		cells[4][2] = "g";
		cells[5][1] = "g";
		cells[5][2] = "f";
		for (int i = 3; i < 10; i++) {
			cells[4][i] = "f";
			cells[5][i] = "f";
		}
		cells[6][3] = "h";
		cells[6][5] = "1";
		cells[6][7] = "2";
		cells[7][4] = "3";
		cells[7][7] = "m";
		cells[8][1] = "4";
		cells[9][8] = "5";
		cells[9][9] = "start";
		cells[1][6] = "end";
		// house gives materials for boat

	}

	@Override
	public void handle(MessageCreateEvent event) {
		// TODO Auto-generated method stub
		String a = event.getMessageContent();

		if (a.equalsIgnoreCase("!textadventure")) {
			setup();
			playing = true;
			event.getChannel().sendMessage(
					"Welcome to text adventure! \nYou wake up in a dark forest of a deserted island and don't know how to escape. You feel hesitant to explore south or east.\nType \"w\" to go west, \"n\" to go north, and etc.\nTo quit the game, type 'stop'");

		}
		
		// If the user wants to go in a direction
		if (playing && !inHouse && !a.isEmpty()) {
			showMap(event);
			switch (a.toLowerCase()) {
			case "w":
				if (x != 0) {
					y--;
					currentCell(event);
				} else {
					event.getChannel().sendMessage("you explored too far and died!");
					event.getChannel().sendMessage("type !textadventure to play again!");
					playing = false;

				}

				break;

			case "n":
				if (y != 0) {
					x--;
					currentCell(event);
				} else {
					event.getChannel().sendMessage("you explored too far and died!");
					event.getChannel().sendMessage("type !textadventure to play again!");
					playing = false;

				}

				break;

			case "s":
				if (y != 9) {
					y++;
					currentCell(event);
				} else {
					event.getChannel().sendMessage("you explored too far and died!");
					event.getChannel().sendMessage("type !textadventure to play again!");
					playing = false;

				}

				break;

			case "e":
				if (x != 9) {
					x++;
					currentCell(event);
				} else {
					event.getChannel().sendMessage("you explored too far and died!");
					event.getChannel().sendMessage("type !textadventure to play again!");
					playing = false;

				}

				break;

			default:
				event.getChannel().sendMessage("Type N, W, S, or E to go North, West, South, or East.");
				break;
			}
		}

		if (playing && a.equalsIgnoreCase("stop")) {
			event.getChannel().sendMessage("Thanks for playing! If you want to play again, type !textadventure");
			playing = false;
		}

		if (playing && inHouse && a.equalsIgnoreCase("yes")) {
			event.getChannel().sendMessage(
					"You creak open the door. It's dark and scary, but you find some wood and building tools. You take them.");
			materials = true;
			inHouse = false;
		}

		if (playing && inHouse && a.equalsIgnoreCase("no")) {
			event.getChannel().sendMessage("You decide not to go in. Maybe it's for the better.");
			inHouse = false;
		}

	}

	public void currentCell(MessageCreateEvent e) {
		if (cells[x][y].equals("start")) {
			e.getChannel().sendMessage("This area seems familiar. It must be where you woke up earlier");
		}
		if (cells[x][y].equals("m")) {
			e.getChannel().sendMessage(
					"You find a map covered in dirt. You pick it up. It's mostly unclear and faded, except for the location of the buried map and a drawing of a house west and slightly north of it. Type map to look at it.");

		}
		if (cells[x][y].equals("o")) {
			e.getChannel().sendMessage(
					"You wade in a shallow ocean. It seems to get deeper pretty close by and you can't swim");

		}
		if (cells[x][y].equals("s")) {
			e.getChannel().sendMessage(
					"You walk in scorching sand that burns your feet. There might be water nearby. But why is the sand so hot? Its 75 degrees at most");

		}
		if (cells[x][y].equals("g")) {
			e.getChannel().sendMessage("You're in a grass field. There's something uneasy about it. You're on guard");
		}
		if (cells[x][y].equals("f")) {
			e.getChannel().sendMessage(
					"You're in a deep, dark forest. If you didn't have a compass, you'd definetly get lost in here. There's no way to get around with just your five senses");
		}
		if (cells[x][y].equals("1")) {
			playing = false;
			e.getChannel().sendMessage(
					"You accidentally graze your leg on a rare type of poison ivy. You are instantly thrown to the ground due to immense pain, and you die a few minutes later. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("2")) {
			playing = false;
			e.getChannel().sendMessage(
					"Blinded by the darkness, you can't see the black cobra that sneaks towards you. It takes one leap and bites your neck before you know it, and you die of the venom in under an hour. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("3")) {
			playing = false;
			e.getChannel().sendMessage(
					"As you walk through the forest, you catch your feet in what seems to be a small hole. however, as you trip and fall, you realize that it's a deep well. You fall in and scream for seconds, then minutes, then hours. It's a bottomless well, you conclude. You're stuck there forever. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("4")) {
			playing = false;
			e.getChannel().sendMessage(
					"You've been walking for hours and you're starving. Luckily, you find some bright red berries on a nearby shrub. Instinctively and naively, you reach for the berries and devour them. Unlucky for you, they were highly poisonous. In just minutes you crumble to your stomach and vomit everything you have, but it's too late. The poison is in you and kills you in less than an hour. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("5")) {
			playing = false;
			e.getChannel().sendMessage(
					"Blinded by the dark forest but confindent in finding your way out, you stumble into a cliff and fall to your death. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("6")) {
			playing = false;
			e.getChannel().sendMessage(
					"In the distance of the grass field you're in, you see a massive feline creature. You quickly run into the forest south of you, but the feline is too fast, catches you, and eats you for breakfast. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("h")) {
			e.getChannel().sendMessage(
					"As you explore the forest, you find a house! It's very old and looks kind of haunted. Enter it?");
			e.getChannel().sendMessage("Type 'yes' for yes and 'no' for no");
			inHouse = true;

		}
		if (cells[x][y].equals("end") && !materials) {
			e.getChannel().sendMessage(
					"You find a dock, some oars, and a bucket. It looks like you could escape the island if you had a boat. Somehow.");
			needMaterials = true;
		}
		if (cells[x][y].equals("end") && materials && !lava && !needLava) {
			if (needMaterials) {
				e.getChannel().sendMessage(
						"You have materials for a boat!\nYou quikcly build a boat, but as you put it into the water, you realize there's a small hole! If only there was some type of liquid that solifidied quickly, to patch up the hole. You take the bucket on the dock, just in case.");
			} else {
				e.getChannel().sendMessage(
						"You find a dock, some oars, and a bucket. It looks like you could escape the island if you had a boat.\nWait, you have wood and building tools!\nYou quikcly build a boat, but as you put it into the water, you realize there's a small hole! If only there was some type of liquid that solifidied quickly, to patch up the hole");
			}
			needLava = true;
		}
		if (cells[x][y].equals("end") && materials && !lava && needLava) {
			e.getChannel()
					.sendMessage("If only there was some type of liquid that solifidied quickly, to patch up the hole");
		}
		if (cells[x][y].equals("end") && materials && lava && needLava) {
			e.getChannel().sendMessage(
					"You place the lava carefully in the hole. It's cooled down enough to not burn the wood, but not enough to solidify. It solidifies when you use the bucket to collect water and cool the lava down. The boat is fixed!\nYou depart on the boat, and use the oars to travel away from the island to find some land with people. As you disappear into the horizon, you smile, relieved that the dangerous adventure has come to a close.");
			e.getChannel().sendMessage(
					"Congratulations! You escaped the island and won the game! Type !textadventure to play again");
			playing = false;
		}
		if (cells[x][y].equals("lo") && !needLava) {
			playing = false;
			e.getChannel().sendMessage(
					"You trip and fall into a small oasis of lava next to the sand. No wonder it was so hot. Better luck next time!");
			e.getChannel().sendMessage("type !textadventure to play again!");
		}
		if (cells[x][y].equals("lo") && needLava && !lava) {
			e.getChannel().sendMessage(
					"You walk through the sand and ALMOST fall into a lava oasis in the sand, but catch yourself just in time. There's some cool, solid lava next to it too.\nWait. You can grab some of this in the bucket and use it to patch up the boat! You grab some lava.");
			lava = true;
		}
		if (cells[x][y].equals("lo") && needLava && lava) {
			e.getChannel().sendMessage("You grab some lava for the boat.");

		}
	}// fix house yes and no, need to put the listners for yes and no in handle
		// method, wont work if its in CurrentCell, figure it out, use more booleans

	public void importMap(String filename, String[][] cells) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
			
			while (br.ready()) {
				String s = br.readLine();
				String[] row = new String[10];
				
				for (int i = 0; i < s.length(); i++) {
					row[i] = convert(""+s.charAt(i), 1, 0);
				}
			
			br.close();
			
			return (String[][]) res.toArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void showMap(MessageCreateEvent e) {
		for (int i = 0; i < cells.length; i++) {
			String s = "";
			for (int j = 0; j < cells[i].length; j++) {
				if (i == x && j == y) {
					s += ":slight_smile: ";
				} else {
					s += convert(cells[i][j], 0, 2) + " ";
				}
			}

			e.getChannel().sendMessage(s + Math.floor(((double) i + 2 / cells.length) * 10) + "% complete");
		}
	}

	public String convert(String value, int format1, int format2) {
		ArrayList<String> f1 = formats.get(format1);
		ArrayList<String> f2 = formats.get(format2);
		return f2.get(f1.indexOf(value));
	}

}

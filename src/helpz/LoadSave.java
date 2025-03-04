package helpz;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {

	// Background and environment stuff
	public static final String TITLE_BG = 			  "a_title_background.png";
	public static final String PLAYING_BG =           "a_playing_bg_img.png";
	public static final String LEVEL_MENU_BG =        "a_level_select_bg - Kopie.png";
	
	public static final String LEVEL_SELECT_MENU_BG = "b_level_select_menu_bg.png";
	public static final String TOOLBAR_BG = 		  "b_toolbar_bg.png";
	
	public static final String PLAYING_BIG_CLOUD =    "a_big_clouds.png";
	public static final String PLAYING_SMALL_CLOUD =  "a_small_clouds.png";
	
	public static final String LEVEL_SPRITES =        "a_outside_Sprites.png";
	public static final String START_POS =            "a_start_pos.png";
	
	public static final String WATER_IMG =            "d_water - Kopie.png";
	public static final String WATER_SPRITES =        "d_water_sprites - Kopie.png";
	public static final String RAIN_DROP =            "d_rain_particle.png";
	
	public static final String TREE_SPRITES =         "d_tree_straight_sprites.png";
	public static final String CURVED_TREE_SPRITES =  "d_tree_curved_sprites.png";
	public static final String GRASS_SPRITES =        "d_grass_sprites - Kopie.png";
	
	// GUI
	public static final String TITLE_TITLE =          "b_title.png";
	public static final String TITLE_MENU =           "b_title_menu.png";
	public static final String COMPLETED_MENU =       "b_completed_sprite.png";
	public static final String DEATH_SCREEN_MENU =    "b_death_screen.png";
	public static final String OPTIONS_MENU =         "b_options_menu.png";
	public static final String PAUSE_MENU =           "b_pause_background.png";
	
	public static final String TITLE_BUTTONS =        "b_button_sprites.png";
	public static final String LEVEL_BUTTONS =        "b_level_select_buttons.png";
	public static final String PAUSE_SOUND_BUTTON =   "b_sound_button.png";
	public static final String PAUSE_VOLUME_SLIDER =  "b_volume_slider.png";
	public static final String PAUSE_URM_BUTTONS =    "b_urm_buttons.png";
	public static final String EDIT_BUTTON_BG =       "b_edit_button_bg.png";
	public static final String EDIT_BUTTON_BG_MARK =  "b_edit_button_bg_mark.png";
	
	public static final String PLAYER_HP_POWER_BARS = "b_health_power_bar.png";
	
	public static final String LEVEL_MISSION_CHECKBOX = "b_mission_checkbox.png";
	public static final String LEVEL_MISSION_INFO_BG = "b_mission_info.png";
	
 	// Enemies and Objects
	public static final String PLAYER_SPRITES =            "c_player_sprites_meins.png";
	public static final String CRABBY_SPRITES =            "c_crabby_sprites.png";
	public static final String PINKSTAR_SPRITES =          "c_pinkstar_sprites.png";
	public static final String SHARK_SPRITES =             "c_shark_sprites.png";
	
	public static final String OBJECT_BOX_BARREL =         "d_objects_sprites.png";
	public static final String OBJECT_POTIONS =            "d_potions_sprites.png";
	public static final String OBJECT_SPIKE =              "d_trap_sprites.png";
	public static final String OBJECT_CANNON =             "d_cannon_sprites.png";
	public static final String OBJECT_CANNONBALL =         "d_ball.png";
	public static final String OBJECT_BOAT =               "d_ship.png";
	public static final String OBJECT_COIN =               "d_coin.png";
	public static final String OBJECT_COIN_BORDER =        "d_coin_border.png";
	public static final String OBJECT_COIN_BORDER_FILLED = "d_coin_border_filled.png";
	public static final String EXCLAMATION_SPRITES =       "d_exclamation_sprites.png";
	public static final String QUESTIONMARK_SPRITES =      "d_question_sprites.png";
	
	public static final String ATLAS_CONTAINER =           "e_atlas_container.png";
	public static final String ATLAS_POTION =              "e_atlas_potion.png";
	public static final String ATLAS_CANNON =              "e_atlas_cannon.png";
	public static final String ATLAS_CRABBY =              "e_atlas_crabby.png";
	public static final String ATLAS_PINKSTAR =            "e_atlas_pinkstar.png";
	public static final String ATLAS_SHARK =               "e_atlas_shark.png";
	public static final String ATLAS_CURVED_TREE =         "e_atlas_curved_tree.png";
	public static final String ATLAS_STRAIGHT_TREE =       "e_atlas_straight_tree.png";
	
	
	
	// Unused
	public static final String CREDITS =             "b_credits_list.png";
	public static final String GAME_COMPLETED_MENU = "b_game_completed.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;
		
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		File[] files = file.listFiles();
		BufferedImage[] imgs = new BufferedImage[files.length];
		
		for (int i = 0; i < files.length; i++) {
			try {
				imgs[i] = ImageIO.read(files[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imgs;		
	}
	
	public static void SaveLevel(int lvl, BufferedImage img) {
		String homePath = System.getProperty("user.dir");
		String fs = File.separator;
		String filePath = homePath + fs + "resource" + fs + "lvls" + fs + lvl + ".png";
		
		File lvlFile = new File(filePath);
		try {
			ImageIO.write(img, "png", lvlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

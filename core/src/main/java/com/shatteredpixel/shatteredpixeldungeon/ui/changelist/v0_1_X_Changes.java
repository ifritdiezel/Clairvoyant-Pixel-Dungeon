/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_1_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo( "0.1", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);

		add_v0_1_0_Changes(changeInfos);
	}

	public static void add_v0_1_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo("ALPHA", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(Icons.get(Icons.IFRIT), "Developer Commentary",
				"_-_ The initial release.\n"));

		changes.addButton( new ChangeButton( new Image(Assets.Sprites.APPRENTICE, 0, 90, 12, 15), "Apprentice",
				"Warrior replaced by Apprentice: a new character based around magic and underused content of the game.\n\n Only 1 subclass is available for now."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TOMB, new ItemSprite.Glowing( 0x666666 )), "Crypt room buff",
				"_-_ Crypt room equipment is now better by 2 stages instead of 1."));

		changes.addButton( new ChangeButton(new Image(Assets.Environment.TERRAIN_FEATURES, 112, 64, 16, 16), "Trap Adjustments",
				"_-_ Blinding traps cannot be hidden anymore."));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARPACCIO, null), "Potion Changes",
				"_-_ Steaks can be frozen to turn them into carpaccios."));
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_BERKANAN, null), "UI changes",
				"_-_ You already noticed."));

		changes.addButton( new ChangeButton(Icons.get(Icons.DATA), "Online functionality",
				"_-_ Updates and news completely removed."));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "Strong hits",
				"_-_ Dealing over 100 damage is twice as loud."));


	}
	
}

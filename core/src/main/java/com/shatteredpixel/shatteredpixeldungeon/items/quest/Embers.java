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

package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Embers extends Item {

	{
		image = ItemSpriteSheet.EMBER;
		defaultAction = ABSORB;
		unique = true;
	}
	private static final String ABSORB = "ABSORB";

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(ABSORB);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(ABSORB)) {
			Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
			int cell = curUser.pos;
			int damage = 0;
			boolean firePresent = curUser.buff( Burning.class ) != null;

			for (int i : PathFinder.NEIGHBOURS9){
				if (fire != null && fire.volume > 0 && fire.cur[cell + i] > 0) {
					fire.clear(i + cell);
					CellEmitter.get( cell+i ).burst(SmokeParticle.FACTORY, 3 );
					firePresent = true;
					damage +=1;
				}
			}
			if (firePresent){
				GLog.h("The embers absorb the flames around you, burning your hands!");
				curUser.damage(Dungeon.depth/5*damage + Random.Int(4), this);
				Buff.detach( curUser, Burning.class );
				if (!curUser.isAlive()) {
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "ondeath") );
				}
			}
			else {
				GLog.i("There is no fire nearby to absorb.");
			}
		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return new ItemSprite.Glowing(0x660000, 3f);
	}
}

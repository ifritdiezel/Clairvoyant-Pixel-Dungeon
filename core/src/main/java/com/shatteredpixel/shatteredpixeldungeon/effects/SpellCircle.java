package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

public class SpellCircle extends Image {

    private Image circleT2;

    private final CharSprite target;
    private static int color = 1;
    int tier = 3;

    public SpellCircle (CharSprite sprite) {
        super(new Image(Assets.Effects.SPELLCIRCLE));
        frame(new RectF(0.25f, 0, 0.5f, 1));
        this.target = sprite;
        angularSpeed = 20;
        scale.set(2);
        origin.set( width / 2, height / 2 );
        x = target.x;
        y = target.y;
        GameScene.spellcircle(this);
    }

    public void reset() {
        revive();
    }

    @Override
    public void update() {
        super.update();
        color(color, color, color);
        color += 1;
    }

    @Override
    public void draw() {

        float targetCenterX = target.x-target.width()/2;

        if (angularSpeed > 20) angularSpeed -= 1;
        if (Math.abs(x-targetCenterX) > 0.05) {x -= (x-targetCenterX)/20; angularSpeed += 0.5;}
        if (y != target.y) y -= (y-target.y)/20;
        super.draw();
    }


}

package com.ferox.game.world.entity.masks.animations;

public class AnimationLoop {

    private final Animation anim;
    private final int loopDelay;

    public AnimationLoop(Animation anim, int loopDelay) {
        this.anim = anim;
        this.loopDelay = loopDelay;
    }

    public Animation getAnim() {
        return anim;
    }

    public int getLoopDelay() {
        return loopDelay;
    }
}

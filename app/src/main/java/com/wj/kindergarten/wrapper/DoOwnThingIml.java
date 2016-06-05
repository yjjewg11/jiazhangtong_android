package com.wj.kindergarten.wrapper;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by tangt on 2016/1/5.
 */
public class DoOwnThingIml implements DoOwnThing {

    private ObjectAnimator animator;

    public DoOwnThingIml(ObjectAnimator animator) {
        this.animator = animator;
    }

    @Override
    public void pullFromTop() {
        animator.reverse();
    }

    @Override
    public void pullFromEnd() {
        animator.start();
    }
}

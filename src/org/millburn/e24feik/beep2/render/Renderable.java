package org.millburn.e24feik.beep2.render;


import org.millburn.e24feik.beep2.core.Cleanable;
import org.millburn.e24feik.beep2.core.Window;
import org.millburn.e24feik.beep2.scene.Scene;

public interface Renderable extends Cleanable {
    void render(Window window, Scene scene);
}

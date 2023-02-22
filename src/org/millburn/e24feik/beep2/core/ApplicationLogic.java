package org.millburn.e24feik.beep2.core;

import org.millburn.e24feik.beep2.render.Renderer;
import org.millburn.e24feik.beep2.scene.Scene;

public interface ApplicationLogic extends Cleanable {
    void initialize(Window window, Scene scene, Renderer renderer);

    void input(Window window, Scene scene, float deltaMillis);

    void update(Window window, Scene scene, float deltaMillis);

    void clean();
}

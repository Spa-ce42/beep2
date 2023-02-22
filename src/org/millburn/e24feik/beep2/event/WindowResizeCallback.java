package org.millburn.e24feik.beep2.event;

import org.millburn.e24feik.beep2.core.Window;

public interface WindowResizeCallback {
    void onResize(Window window, int nWidth, int nHeight);
}

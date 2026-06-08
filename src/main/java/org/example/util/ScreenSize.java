package org.example.util;

import java.awt.*;

public class ScreenSize{

    private static final Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static final int WIDTH = scSize.width;
    public static final int HEIGHT = scSize.height;

}

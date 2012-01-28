package com.jirge.client;

import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.Image;

public interface Animate {
    static final CssColor originColor = CssColor.make("rgba(255,255,100,0.6)");
    static final CssColor redrawColor = CssColor.make("rgba(255,255,255,1.0)");

    static final Image imgDog = new Image("images/JiregeeDog.png");
    static final Image imgDear = new Image("images/JiregeeDeer.png");
}